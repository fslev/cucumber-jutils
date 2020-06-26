package com.cucumber.utils.context.props.internal;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StringParser {

    static List<String> captureValues(String source, Pattern captureGroupPattern) {
        List<String> values = new ArrayList<>();
        Matcher matcher = captureGroupPattern.matcher(source);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                values.add(matcher.group(i));
            }
        }
        return values;
    }

    static String captureStandalonePlaceholderName(String source, String prefix, String suffix, Pattern captureGroupPattern) {
        if (!source.startsWith(prefix) || !source.endsWith(suffix)) {
            return null;
        }
        List<String> placeholderNames = captureValues(source, captureGroupPattern);
        if (placeholderNames.size() != 1) {
            return null;
        }
        String placeholderName = placeholderNames.iterator().next();
        return source.substring(prefix.length(), source.lastIndexOf(suffix)).equals(placeholderName) ? placeholderName : null;
    }

    static Object replacePlaceholders(String source, String prefix, String suffix, Pattern captureGroupPattern,
                                      Function<String, Object> placeholderValue, Predicate<String> placeholderHasValue) {
        if (source == null || source.isEmpty()) {
            return source;
        }

        String standalonePlaceholderName = captureStandalonePlaceholderName(source, prefix, suffix, captureGroupPattern);
        if (standalonePlaceholderName != null) {
            if (!placeholderHasValue.test(standalonePlaceholderName)) {
                return source;
            }
            return placeholderValue.apply(standalonePlaceholderName);
        }
        String str = source;
        List<String> placeholderNames = captureValues(source, captureGroupPattern);
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