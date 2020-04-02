package com.cucumber.utils.engineering.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

public class RegexUtils {
    final static ArrayList<String> specialRegexCharacters = new
            ArrayList<>(Arrays.asList("\\", "^", "$", ".", "|", "!", "?", "*", "+", "(", ")", "[", "{", "<", ">"));

    public static boolean isRegex(String str) {
        try {
            Pattern.compile(str);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    public static List<String> getRegexCharsFromString(String str) {
        if (str != null && !str.isEmpty()) {
            return Collections.emptyList();
        } else {
            return specialRegexCharacters.stream().filter(str::contains).collect(Collectors.toList());
        }
    }
}
