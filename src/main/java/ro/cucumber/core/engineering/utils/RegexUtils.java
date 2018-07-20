package ro.cucumber.core.engineering.utils;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexUtils {

    public static boolean isRegex(String str) {
        try {
            Pattern.compile(str);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }
}
