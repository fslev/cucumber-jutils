package ro.cucumber.core.matchers;

import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.DifferenceEvaluators;
import org.xmlunit.diff.ElementSelectors;
import ro.cucumber.core.matchers.comparators.CustomXmlComparator;
import ro.cucumber.core.matchers.exceptions.MatcherException;
import ro.cucumber.core.symbols.SymbolAssignable;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;
import static ro.cucumber.core.utils.XmlUtils.isValid;

public class XmlMatcher implements SymbolAssignable, Matchable {

    private String expected;
    private String actual;
    private CustomXmlComparator comparator = new CustomXmlComparator();

    public XmlMatcher(Object expected, Object actual) throws MatcherException {
        this.expected = expected.toString();
        this.actual = actual.toString();
        if (!isValid(this.expected) || !isValid(this.actual)) {
            throw new MatcherException("Malformed XML");
        }
    }

    @Override
    public void matches() {
        assertThat(actual, isSimilarTo(expected).ignoreWhitespace()
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
                .withDifferenceEvaluator(
                        DifferenceEvaluators.chain(DifferenceEvaluators.Default, comparator)));
    }

    @Override
    public Map<String, String> getAssignSymbols() {
        return comparator.getAssignSymbols();
    }
}
