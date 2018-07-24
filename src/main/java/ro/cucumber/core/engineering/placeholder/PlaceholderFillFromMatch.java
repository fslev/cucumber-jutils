package ro.cucumber.core.engineering.placeholder;


import ro.cucumber.core.engineering.utils.RegexUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderFillFromMatch {

    private static final String PLACEHOLDER_START = "~\\[";
    private static final String PLACEHOLDER_END = "\\]";
    private static final String PLACEHOLDER_REGEX =
            PLACEHOLDER_START + "(.*?)" + PLACEHOLDER_END;

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile(PLACEHOLDER_REGEX,
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

    private String stringWithPlaceholders;
    private String stringWithValues;
    private Map<String, String> placeholderValues = new LinkedHashMap<>();

    public PlaceholderFillFromMatch(String stringWithPlaceholders, String stringWithValues) {
        this.stringWithPlaceholders = stringWithPlaceholders;
        this.stringWithValues = stringWithValues;
        fillPlaceholders();
    }

    public String getResult() {
        String str = stringWithPlaceholders;
        for (Map.Entry<String, String> e : placeholderValues.entrySet()) {
            str = str.replaceAll(PLACEHOLDER_START + e.getKey() + PLACEHOLDER_END,
                    e.getValue());
        }
        return str;
    }

    public Map<String, String> getPlaceholderValues() {
        return this.placeholderValues;
    }

    private void fillPlaceholders() {
        List<String> symbolNames = searchForPlaceholders();
        if (symbolNames.isEmpty()) {
            return;
        }
        boolean isRegex = RegexUtils.isRegex(stringWithPlaceholders);
        String str = !isRegex ? "\\Q" + stringWithPlaceholders + "\\E" : stringWithPlaceholders;
        for (String name : symbolNames) {
            str = str.replaceAll(PLACEHOLDER_START + name + PLACEHOLDER_END,
                    !isRegex ? "\\\\E(.*)\\\\Q" : "(.*)");
        }
        Pattern pattern =
                Pattern.compile(str, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(stringWithValues);
        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                placeholderValues.put(symbolNames.get(i - 1), matcher.group(i));
            }
        }
    }

    private List<String> searchForPlaceholders() {
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
