package ro.cucumber.core.engineering.compare;

import ro.cucumber.core.engineering.placeholders.PlaceholdersGenerator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import static org.junit.Assert.fail;

public class StringRegexCompare implements SymbolsExtractable {

    private String expected;
    private String actual;
    private Map<String, String> assignSymbols = new HashMap<>();

    public StringRegexCompare(Object expected, Object actual) {
        this.expected = expected.toString();
        this.actual = actual.toString();
    }

    @Override
    public Map<String, String> compare() {

        PlaceholdersGenerator parser = new PlaceholdersGenerator(expected, actual);
        boolean hasAssignSymbols = !parser.getPlaceholdersMap().isEmpty();
        String parsedString = expected;
        if (hasAssignSymbols) {
            parsedString = parser.parse();
        }
        try {
            Pattern pattern = Pattern.compile(parsedString, Pattern.DOTALL | Pattern.MULTILINE);
            if (pattern.matcher(actual).matches()) {
                if (hasAssignSymbols) {
                    this.assignSymbols.putAll(parser.getPlaceholdersMap());
                }
            } else {
                fail(String.format("Expected: %s, But got: %s", parsedString, actual));
            }
        } catch (PatternSyntaxException e) {
            if (parsedString.equals(actual)) {
                if (hasAssignSymbols) {
                    this.assignSymbols.putAll(parser.getPlaceholdersMap());
                }
            } else {
                fail(String.format("Expected: %s, But got: %s", parsedString, actual));
            }
        }
        return assignSymbols;
    }
}
