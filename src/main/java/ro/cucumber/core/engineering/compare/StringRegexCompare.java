package ro.cucumber.core.engineering.compare;

import ro.cucumber.core.engineering.placeholder.PlaceholderFillFromMatch;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.fail;

public class StringRegexCompare implements SymbolsAssignComparable {

    private String expected;
    private String actual;
    private Map<String, String> assignSymbols = new HashMap<>();

    public StringRegexCompare(Object expected, Object actual) {
        this.expected = expected.toString();
        this.actual = actual.toString();
    }

    @Override
    public Map<String, String> compare() {

        PlaceholderFillFromMatch parser = new PlaceholderFillFromMatch(expected, actual);
        boolean hasAssignSymbols = !parser.getPlaceholderValues().isEmpty();
        String parsedString = expected;
        if (hasAssignSymbols) {
            parsedString = parser.getResult();
        }
        try {
            Pattern pattern = Pattern.compile(parsedString, Pattern.DOTALL | Pattern.MULTILINE);
            if (pattern.matcher(actual).matches()) {
                if (hasAssignSymbols) {
                    this.assignSymbols.putAll(parser.getPlaceholderValues());
                }
            } else {
                fail(String.format("Expected: %s, But got: %s", parsedString, actual));
            }
        } catch (PatternSyntaxException e) {
            if (parsedString.equals(actual)) {
                if (hasAssignSymbols) {
                    this.assignSymbols.putAll(parser.getPlaceholderValues());
                }
            } else {
                fail(String.format("Expected: %s, But got: %s", parsedString, actual));
            }
        }
        return assignSymbols;
    }
}
