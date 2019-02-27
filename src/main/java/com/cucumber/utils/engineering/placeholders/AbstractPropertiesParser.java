package com.cucumber.utils.engineering.placeholders;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractPropertiesParser {

    protected String stringWithPlaceholders;

    public AbstractPropertiesParser(String stringWithPlaceholders) {
        if (stringWithPlaceholders == null) {
            throw new RuntimeException("I don't do NULLs here...");
        }
        this.stringWithPlaceholders = stringWithPlaceholders;
    }

    public String parse(Map<String, String> properties) {
        String str = stringWithPlaceholders;
        for (Map.Entry<String, String> e : properties.entrySet()) {
            str = str.replaceAll(getPlaceholderStart() + e.getKey() + getPlaceholderEnd(), Matcher.quoteReplacement(e.getValue()));
        }
        return str;
    }

    protected abstract String getPlaceholderStart();

    protected abstract String getPlaceholderEnd();

    protected abstract Pattern getPlaceholderPattern();

    public Set<String> getPropertyNames() {
        Set<String> names = new HashSet<>();
        Matcher matcher = getPlaceholderPattern().matcher(stringWithPlaceholders);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                names.add(matcher.group(i));
            }
        }
        return names;
    }
}
