package ro.cucumber.core.matchers;

import java.util.Map;

import static org.junit.Assert.fail;

public class Matcher extends MatcherWithAssignableSymbols {
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
