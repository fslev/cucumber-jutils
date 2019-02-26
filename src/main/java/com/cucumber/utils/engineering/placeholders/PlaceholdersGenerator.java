package com.cucumber.utils.engineering.placeholders;


import com.cucumber.utils.engineering.utils.RegexUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholdersGenerator {

    private static final String PLACEHOLDER_START = "~\\[";
    private static final String PLACEHOLDER_END = "\\]";
    private static final String PLACEHOLDER_REGEX =
            PLACEHOLDER_START + "(.*?)" + PLACEHOLDER_END;

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile(PLACEHOLDER_REGEX,
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

    private String stringWithPlaceholders;
    private String stringWithValues;
    private Map<String, String> placeholdersMap = new LinkedHashMap<>();

    public PlaceholdersGenerator(String stringWithPlaceholders, String stringWithValues) {
        this.stringWithPlaceholders = stringWithPlaceholders;
        this.stringWithValues = stringWithValues;
        assignValuesToPlaceholders();
    }

    public String parse() {
        String str = stringWithPlaceholders;
        for (Map.Entry<String, String> e : placeholdersMap.entrySet()) {
            str = str.replaceAll(PLACEHOLDER_START + e.getKey() + PLACEHOLDER_END,
                    Matcher.quoteReplacement(e.getValue()));
        }
        return str;
    }

    public String parseQuoted() {
        String str = stringWithPlaceholders;
        for (Map.Entry<String, String> e : placeholdersMap.entrySet()) {
            str = str.replaceAll(PLACEHOLDER_START + e.getKey() + PLACEHOLDER_END,
                    Matcher.quoteReplacement(Pattern.quote(e.getValue())));
        }
        return str;
    }

    public Map<String, String> getPlaceholdersMap() {
        return this.placeholdersMap;
    }

    private void assignValuesToPlaceholders() {
        List<String> placeholderNames = getPlaceholderNames();
        if (placeholderNames.isEmpty()) {
            return;
        }
        boolean isRegex = RegexUtils.isRegex(stringWithPlaceholders);
        String str = !isRegex ? "\\Q" + stringWithPlaceholders + "\\E" : stringWithPlaceholders;
        for (String name : placeholderNames) {
            str = str.replaceAll(PLACEHOLDER_START + name + PLACEHOLDER_END,
                    !isRegex ? "\\\\E(.*)\\\\Q" : "(.*)");
        }
        Pattern pattern =
                Pattern.compile(str, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(stringWithValues);
        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                placeholdersMap.put(placeholderNames.get(i - 1), matcher.group(i));
            }
        }
    }

    private List<String> getPlaceholderNames() {
        List<String> names = new ArrayList<>();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(stringWithPlaceholders);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                names.add(matcher.group(i));
            }
        }
        return names;
    }
}
