package ro.cucumber.core.comparison;

import java.util.Map;

import static org.junit.Assert.fail;
//TO do: change name
public class Comparison {
    protected Object expected;
    protected Object actual;

    public Comparison(Object expected, Object actual) {
        this.expected = expected;
        this.actual = actual;
    }

    /**
     * @return A map of assign symbols<br>
     * Empty, if no assign symbols are defined inside expected
     */
    Map<String, String> evaluate() {
        if ((actual == null) != (expected == null)) {
            fail(String.format("Expected: [%s] But found: [%s]", expected, actual));
        }
        return null;
    }
}
