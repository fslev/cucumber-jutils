package com.cucumber.utils.engineering.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringParser {

    public static List<String> captureValues(String source, Pattern captureGroupPattern) {
        return captureValues(source, captureGroupPattern, false);
    }

    public static List<String> captureValues(String source, Pattern captureGroupPattern, boolean matchEntirely) {
        List<String> values = new ArrayList<>();
        if (source == null) {
            return values;
        }
        Matcher matcher = captureGroupPattern.matcher(source);
        while (matcher.find()) {
            if (matchEntirely && !matcher.group(0).equals(source)) {
                return values;
            }
            for (int i = 1; i <= matcher.groupCount(); i++) {
                values.add(matcher.group(i));
            }
        }
        return values;
    }

    public static Object replacePlaceholders(List<String> placeholderNames, String source, String prefix, String suffix,
                                             Function<String, Object> placeholderValue, Predicate<String> placeholderHasValue) {
        if (source == null || source.isEmpty()) {
            return source;
        }

        if (placeholderNames.size() == 1 && source.equals(prefix + placeholderNames.get(0) + suffix)) {
            String standalonePlaceholder = placeholderNames.get(0);
            if (!placeholderHasValue.test(standalonePlaceholder)) {
                return source;
            }
            return placeholderValue.apply(standalonePlaceholder);
        }

        String str = source;
        for (String placeholderName : placeholderNames) {
            if (!placeholderHasValue.test(placeholderName)) {
                continue;
            }
            Object val = placeholderValue.apply(placeholderName);
            str = str.replaceFirst(Pattern.quote(prefix + placeholderName + suffix), val != null ? Matcher.quoteReplacement(val.toString()) : "null");

        }
        return str;
    }
}