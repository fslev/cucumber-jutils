package ro.cucumber.core.engineering.matchers;

import ro.cucumber.core.engineering.matchers.exceptions.MatcherException;

import java.util.HashMap;
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
        if (nullsMatch()) {
            return new HashMap<>();
        }
        SymbolsAssignMatchable matcher;
        try {
            matcher = new JsonMatcher(expected, actual);
        } catch (MatcherException e) {
            try {
                matcher = new XmlMatcher(expected, actual);
            } catch (MatcherException e1) {
                matcher = new StringRegexMatcher(expected, actual);
            }
        }
        return matcher.match();
    }

    private boolean nullsMatch() {
        if (expected == null ^ actual == null) {
            fail(String.format("Expected: [%s] But found: [%s]", expected, actual));
        } else if (expected == null && actual == null) {
            return true;
        }
        return false;
    }
}
