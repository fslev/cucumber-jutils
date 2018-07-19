package ro.cucumber.core.matchers;

import ro.cucumber.core.matchers.exceptions.MatcherException;
import java.util.Map;
import static org.junit.Assert.fail;

public class Matcher implements SymbolsAssignMatchable {
    protected Object expected;
    protected Object actual;

    public Matcher(Object expected, Object actual) {
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public Map<String, String> match() {
        if ((actual == null) != (expected == null)) {
            fail(String.format("Expected: [%s] But found: [%s]", expected, actual));
        }
        SymbolsAssignMatchable matcher;
        try {
            matcher = new JsonMatcher(expected, actual);
        } catch (MatcherException e) {
            matcher = new StringRegexMatcher(expected, actual);
        }
        return matcher.match();
    }
}
