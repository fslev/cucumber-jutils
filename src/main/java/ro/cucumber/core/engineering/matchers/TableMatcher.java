package ro.cucumber.core.engineering.matchers;

import ro.cucumber.core.engineering.matchers.exceptions.MatcherException;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;

public class TableMatcher implements SymbolsAssignMatchable {

    private List<Map<String, String>> expected;
    private List<Map<String, String>> actual;

    public TableMatcher(Object expected, Object actual) throws MatcherException {
        if (expected instanceof List ^ actual instanceof List) {
            throw new MatcherException("No list");
        }
        List expectedList = (List) expected;
        List actualList = (List) actual;
        if (!expectedList.isEmpty()) {
            if (expectedList.get(0) instanceof Map) {
                throw new MatcherException("Elements must be of type Map");
            }
        }
        if (!actualList.isEmpty()) {
            if (actualList.get(0) instanceof Map) {
                throw new MatcherException("Elements must be of type Map");
            }
        }
        this.expected = (List<Map<String, String>>) expected;
        this.actual = (List<Map<String, String>>) actual;
    }

    @Override
    public Map<String, String> match() {
        if (expected.isEmpty() ^ !actual.isEmpty()) {
            fail(String.format("One list is empty. Expected: %s But got: %s", expected, actual));
        }
        if (expected.size() > actual.size()) {
            fail(String.format("Expected list has more elements than the actual one. Size: %s But got: %s",
                    expected.size(), actual.size()));
        }
        for (int i = 0; i < expected.size(); i++) {
            Map<String, String> expectedMap = expected.get(i);
            boolean foundExpectedMap = false;
            for (int j = 0; j < actual.size(); j++) {
                Map<String, String> actualMap = actual.get(j);
                for (Map.Entry<String, String> expectedEntry : expectedMap.entrySet()) {
                    String expectedKey = expectedEntry.getKey();
                    String expectedValue = expectedEntry.getValue();
                    if (actualMap.get(expectedKey) == null) {
                        break;
                    }
                    String actualValue = actualMap.get(expectedKey);
                    Matcher matcher = new Matcher(expectedValue, actualValue);
                    try {
                        matcher.match();
                    } catch (AssertionError e) {
                        break;
                    }
                }
                foundExpectedMap = false;
            }
        }
    }
}
