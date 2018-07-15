package ro.cucumber.core.comparison;

import java.util.Map;

import static org.junit.Assert.fail;

public class Comparison {
    private String expected;
    private String actual;

    public Comparison(String expected, String actual) {
        this.expected = expected;
        this.actual = actual;
    }

    Map<String, String> compare() {
        if ((actual == null) != (expected == null)) {
            fail(String.format("Expected: [%s] But found: [%s]", expected, actual));
        }
    }
}
