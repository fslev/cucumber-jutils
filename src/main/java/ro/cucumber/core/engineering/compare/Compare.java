package ro.cucumber.core.engineering.compare;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.fail;

public class Compare implements SymbolsDefineComparable {
    protected Object expected;
    protected Object actual;

    public Compare(Object expected, Object actual) {
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public Map<String, String> compare() {
        if (nullsMatch()) {
            return new HashMap<>();
        }
        SymbolsDefineComparable matcher;
        try {
            matcher = new JsonCompare(expected, actual);
        } catch (Exception e) {
            try {
                matcher = new JsonConvertibleObjectCompare(expected, actual);
            } catch (Exception e1) {
                try {
                    matcher = new XmlCompare(expected, actual);
                } catch (Exception e2) {
                    matcher = new StringRegexCompare(expected, actual);
                }
            }
        }
        return matcher.compare();
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
