package ro.cucumber.core.matchers;

import ro.cucumber.core.matchers.exceptions.MatcherException;
import ro.cucumber.core.symbols.SymbolAssignable;
import java.util.Map;
import static org.junit.Assert.fail;

public class Matcher implements SymbolAssignable, Matchable {
    protected Object expected;
    protected Object actual;

    public Matcher(Object expected, Object actual) throws MatcherException {
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public void matches() {
        if ((actual == null) != (expected == null)) {
            fail(String.format("Expected: [%s] But found: [%s]", expected, actual));
        }
    }

    @Override
    public Map<String, String> getAssignSymbols() {
        return null;
    }
}
