package com.cucumber.utils.engineering.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public static List getRegexCharsFromString(String str) {
        List regexCharsFromString = new ArrayList();
        ArrayList<String> specialRegexCharacters = new
                ArrayList(Arrays.asList("\\", "^", "$", ".", "|", "!", "?", "*", "+", "(", ")", "[", "{", "<", ">"));
        specialRegexCharacters.forEach(specialRegexCharacter -> {
            if (str.contains(specialRegexCharacter)) {
                regexCharsFromString.add(specialRegexCharacter);
            }
        });
        return regexCharsFromString;
    }
}
