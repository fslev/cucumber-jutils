package ro.cucumber.core.engineering.compare;

import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.DifferenceEvaluators;
import org.xmlunit.diff.ElementSelectors;
import ro.cucumber.core.engineering.compare.exceptions.CompareException;
import ro.cucumber.core.engineering.compare.comparators.CustomXmlComparator;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;
import static ro.cucumber.core.engineering.utils.XmlUtils.isValid;

public class XmlCompare implements SymbolsAssignComparable {

    private String expected;
    private String actual;
    private CustomXmlComparator comparator = new CustomXmlComparator();

    public XmlCompare(Object expected, Object actual) throws CompareException {
        this.expected = expected.toString();
        this.actual = actual.toString();
        if (!isValid(this.expected) || !isValid(this.actual)) {
            throw new CompareException("Malformed XML");
        }
    }

    @Override
    public Map<String, String> compare() {
        assertThat(actual, isSimilarTo(expected).ignoreWhitespace()
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
                .withDifferenceEvaluator(
                        DifferenceEvaluators.chain(DifferenceEvaluators.Default, comparator)));
        return comparator.getAssignSymbols();
    }
}
