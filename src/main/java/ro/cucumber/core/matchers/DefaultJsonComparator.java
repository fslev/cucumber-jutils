package ro.cucumber.core.matchers;

import ro.skyah.comparator.JsonComparator;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class DefaultJsonComparator implements JsonComparator {

    public boolean compareValues(Object expected, Object actual) {
        try {
            Pattern pattern = Pattern.compile(expected.toString());
            return pattern.matcher(actual.toString()).matches();
        } catch (PatternSyntaxException e) {
            return expected.equals(actual);
        }
    }

    public boolean compareFields(String expected, String actual) {
        return expected.equals(actual);
    }
}
