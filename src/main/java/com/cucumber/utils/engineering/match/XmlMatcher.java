package com.cucumber.utils.engineering.match;

import com.cucumber.utils.engineering.match.comparators.xml.CustomXmlComparator;
import com.cucumber.utils.engineering.match.comparators.xml.XmlMatchException;
import com.cucumber.utils.engineering.match.condition.MatchCondition;
import com.cucumber.utils.engineering.utils.RegexUtils;
import com.cucumber.utils.engineering.utils.XmlUtils;
import com.cucumber.utils.exceptions.InvalidTypeException;
import org.w3c.dom.Element;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.DifferenceEvaluators;
import org.xmlunit.diff.ElementSelector;
import org.xmlunit.util.Nodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cucumber.utils.engineering.utils.XmlUtils.isValid;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

public class XmlMatcher extends AbstractMatcher<String> {

    private final CustomXmlComparator comparator;

    public XmlMatcher(String message, Object expected, Object actual, Set<MatchCondition> matchConditions) throws InvalidTypeException {
        super(message, expected, actual, matchConditions);
        this.comparator = new CustomXmlComparator(this.matchConditions);
    }

    @Override
    protected String convert(Object value) throws InvalidTypeException {
        if (value == null) {
            throw new InvalidTypeException("Invalid XML");
        }
        String stringXML = value.toString();
        if (!isValid(stringXML)) {
            throw new InvalidTypeException("Invalid XML");
        }
        return stringXML;
    }

    @Override
    public Map<String, Object> match() {
        if (matchConditions.contains(MatchCondition.DO_NOT_MATCH)) {
            matchConditions.remove(MatchCondition.DO_NOT_MATCH);
            try {
                positiveMatch();
            } catch (AssertionError e) {
                return new HashMap<>();
            }
            fail(negativeMatchMessage);
        }
        return positiveMatch();
    }

    private Map<String, Object> positiveMatch() {
        try {
            assertThat(message, actual, isSimilarTo(expected).ignoreWhitespace()
                    .withNodeMatcher(new DefaultNodeMatcher(new ByNameAttrAndTextSelector()))
                    .withDifferenceEvaluator(
                            DifferenceEvaluators.chain(comparator)));
        } catch (AssertionError e) {
            debugIfXmlContainsUnintentionalRegexChars(expected);
            throw e;
        }
        return comparator.getGeneratedProperties();
    }

    class ByNameAttrAndTextSelector implements ElementSelector {

        @Override
        public boolean canBeCompared(Element controlElement, Element testElement) {
            if (controlElement == null || testElement == null || !Nodes.getQName(controlElement).equals(Nodes.getQName(testElement))) {
                return false;
            }
            try {
                comparator.match(Nodes.getAttributes(controlElement), Nodes.getAttributes(testElement));
                comparator.match(Nodes.getMergedNestedText(controlElement), Nodes.getMergedNestedText(testElement));
                return true;
            } catch (XmlMatchException e) {
                return false;
            }
        }
    }

    private static void debugIfXmlContainsUnintentionalRegexChars(String xml) {
        if (!LOG.isDebugEnabled()) {
            return;
        }
        try {
            Map<String, List<String>> specialRegexChars = XmlUtils.walkXmlAndProcessNodes(xml, nodeValue -> {
                List<String> regexChars = RegexUtils.getRegexCharsFromString(nodeValue);
                return regexChars.isEmpty() ? null : regexChars;
            });
            if (!specialRegexChars.isEmpty()) {
                String prettyResult = specialRegexChars.entrySet().stream().map(e -> e.getKey() + " contains: " + e.getValue().toString())
                        .collect(Collectors.joining("\n"));
                LOG.debug(" \n\n Comparison mechanism failed while comparing XMLs." +
                                " \n One reason for this, might be that XML may have unintentional regex special characters. " +
                                "\n If so, try to quote them by using \\Q and \\E or simply \\" +
                                "\n Found the following list of special regex characters inside expected:\n\n{}\n\nExpected:\n{}\n",
                        prettyResult, xml);
            }
        } catch (Exception e) {
            LOG.debug("Cannot extract special regex characters from xml");
        }
    }
}
