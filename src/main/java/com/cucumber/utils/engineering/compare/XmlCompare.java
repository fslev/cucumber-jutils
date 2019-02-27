package com.cucumber.utils.engineering.compare;

import com.cucumber.utils.engineering.compare.comparators.CustomXmlComparator;
import com.cucumber.utils.engineering.compare.exceptions.CompareException;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.DifferenceEvaluators;
import org.xmlunit.diff.ElementSelectors;

import java.util.Map;

import static com.cucumber.utils.engineering.utils.XmlUtils.isValid;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

public class XmlCompare implements Placeholdable {

    private String expected;
    private String actual;
    private CustomXmlComparator comparator = new CustomXmlComparator();
    private String message;

    public XmlCompare(Object expected, Object actual) throws CompareException {
        this(null, expected, actual);
    }

    public XmlCompare(String message, Object expected, Object actual) throws CompareException {
        this.expected = expected.toString();
        this.actual = actual.toString();
        if (!isValid(this.expected) || !isValid(this.actual)) {
            throw new CompareException("Malformed XML");
        }
        this.message = message == null ? "" : message;
    }

    @Override
    public Map<String, String> compare() {
        assertThat(message, actual, isSimilarTo(expected).ignoreWhitespace()
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
                .withDifferenceEvaluator(
                        DifferenceEvaluators.chain(DifferenceEvaluators.Default, comparator)));
        return comparator.getGeneratedProperties();
    }
}
