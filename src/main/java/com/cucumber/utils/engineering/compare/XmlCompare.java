package com.cucumber.utils.engineering.compare;

import com.cucumber.utils.engineering.compare.comparators.CustomXmlComparator;
import com.cucumber.utils.engineering.compare.exceptions.CompareException;
import org.w3c.dom.Element;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.DifferenceEvaluators;
import org.xmlunit.diff.ElementSelector;
import org.xmlunit.util.Nodes;

import java.util.Map;

import static com.cucumber.utils.engineering.utils.XmlUtils.isValid;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

public class XmlCompare implements Placeholdable {

    private String expected;
    private String actual;
    private CustomXmlComparator comparator;
    private String message;

    public XmlCompare(Object expected, Object actual) throws CompareException {
        this(null, expected, actual);
    }

    public XmlCompare(String message, Object expected, Object actual) throws CompareException {
        this(message, expected, actual, false, false);
    }

    public XmlCompare(String message, Object expected, Object actual, boolean nonExtensible, boolean strictOrder) throws CompareException {
        this.expected = expected.toString();
        this.actual = actual.toString();
        this.comparator = new CustomXmlComparator(nonExtensible, strictOrder, nonExtensible);
        if (!isValid(this.expected) || !isValid(this.actual)) {
            throw new CompareException("Malformed XML");
        }
        this.message = message == null ? "" : message;
    }

    @Override
    public Map<String, String> compare() {
        assertThat(message, actual, isSimilarTo(expected).ignoreWhitespace()
                .withNodeMatcher(new DefaultNodeMatcher(new CustomElementSelector()))
                .withDifferenceEvaluator(
                        DifferenceEvaluators.chain(DifferenceEvaluators.Default, comparator)));
        return comparator.getGeneratedProperties();
    }

    class CustomElementSelector implements ElementSelector {

        @Override
        public boolean canBeCompared(Element controlElement, Element testElement) {
            return controlElement != null && testElement != null
                    && Nodes.getQName(controlElement).equals(Nodes.getQName(testElement))
                    && comparator.match(Nodes.getMergedNestedText(controlElement), Nodes.getMergedNestedText(testElement));
        }
    }
}
