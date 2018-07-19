package ro.cucumber.core.matchers;

import ro.cucumber.core.matchers.exceptions.MatcherException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.fail;

public class Matcher implements SymbolsAssignMatchable {
    protected Object expected;
    protected Object actual;
    private Map<String, String> assignSymbols = new HashMap<>();

    public Matcher(Object expected, Object actual) throws MatcherException {
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public void match() {
        if ((actual == null) != (expected == null)) {
            fail(String.format("Expected: [%s] But found: [%s]", expected, actual));
        }
        SymbolsAssignMatchable matcher;
        try {
            matcher = new JsonMatcher(expected, actual);
        } catch (MatcherException e) {
            matcher = new StringRegexMatcher(expected, actual);
        }
        matcher.match();
        assignSymbols = matcher.getAssignSymbols();
    }

    @Override
    public Map<String, String> getAssignSymbols() {
        return assignSymbols;
    }
}
