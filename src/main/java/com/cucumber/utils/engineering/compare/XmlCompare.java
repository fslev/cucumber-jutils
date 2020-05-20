package com.cucumber.utils.engineering.compare;

import com.cucumber.utils.engineering.compare.comparators.xml.CustomXmlComparator;
import com.cucumber.utils.engineering.compare.comparators.xml.XmlMatchException;
import com.cucumber.utils.engineering.compare.exceptions.CompareException;
import com.cucumber.utils.engineering.utils.RegexUtils;
import com.cucumber.utils.engineering.utils.XmlUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.DifferenceEvaluators;
import org.xmlunit.diff.ElementSelector;
import org.xmlunit.util.Nodes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cucumber.utils.engineering.utils.XmlUtils.isValid;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

public class XmlCompare implements Placeholdable {
    private final Logger log = LogManager.getLogger();

    private final String expected;
    private final String actual;
    private final CustomXmlComparator comparator;
    private final String message;

    public XmlCompare(Object expected, Object actual) throws CompareException {
        this(null, expected, actual);
    }

    public XmlCompare(String message, Object expected, Object actual) throws CompareException {
        this(message, expected, actual, false, false, false);
    }

    public XmlCompare(String message, Object expected, Object actual, boolean childNodeListLength, boolean childNodeListSequence, boolean elementNumAttributes) throws CompareException {
        this.expected = expected.toString();
        this.actual = actual.toString();
        this.comparator = new CustomXmlComparator(childNodeListLength, childNodeListSequence, elementNumAttributes);
        if (!isValid(this.expected) || !isValid(this.actual)) {
            throw new CompareException("Malformed XML");
        }
        this.message = message == null ? "" : message;
    }

    @Override
    public Map<String, Object> compare() {
        try {
            assertThat(message, actual, isSimilarTo(expected).ignoreWhitespace()
                    .withNodeMatcher(new DefaultNodeMatcher(new ByNameAttrAndTextSelector()))
                    .withDifferenceEvaluator(
                            DifferenceEvaluators.chain(comparator)));
        } catch (AssertionError e) {
            checkXmlContainsSpecialRegexCharsAndDebug(expected);
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

    private void checkXmlContainsSpecialRegexCharsAndDebug(String xml) {
        if (!log.isDebugEnabled()) {
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
                log.debug(" \n\n Comparison mechanism failed while comparing XMLs." +
                                " \n One reason for this, might be that XML may have unintentional regex special characters. " +
                                "\n If so, try to quote them by using \\Q and \\E or simply \\" +
                                "\n Found the following list of special regex characters inside expected:\n\n{}\n\nExpected:\n{}\n",
                        prettyResult, expected);
            }
        } catch (Exception e) {
            log.debug("Cannot extract special regex characters from xml");
        }
    }
}
