package ro.cucumber.core.matchers;

import java.util.Map;
import static org.junit.Assert.fail;

public class Matcher extends AbstractMatcher {
    protected Object expected;
    protected Object actual;

    public Matcher(Object expected, Object actual) throws MatcherException {
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    protected Map<String, String> matches() {
        if ((actual == null) != (expected == null)) {
            fail(String.format("Expected: [%s] But found: [%s]", expected, actual));
        }
        return null;
    }
}
