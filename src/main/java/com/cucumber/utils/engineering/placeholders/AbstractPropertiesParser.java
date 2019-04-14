package com.cucumber.utils.engineering.placeholders;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractPropertiesParser {

    protected String stringWithProperties;

    public AbstractPropertiesParser(String stringWithProperties) {
        if (stringWithProperties == null) {
            throw new RuntimeException("I don't do NULLs here...");
        }
        this.stringWithProperties = stringWithProperties;
    }

    public String parse(Map<String, String> properties) {
        String str = stringWithProperties;
        for (Map.Entry<String, String> e : properties.entrySet()) {
            str = str.replaceAll(getSymbolStart() + e.getKey() + getSymbolEnd(), Matcher.quoteReplacement(e.getValue()));
        }
        return str;
    }

    protected abstract String getSymbolStart();

    protected abstract String getSymbolEnd();

    protected abstract Pattern getSymbolPattern();

    public Set<String> getPropertyKeys() {
        Set<String> names = new HashSet<>();
        Matcher matcher = getSymbolPattern().matcher(stringWithProperties);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                names.add(matcher.group(i));
            }
        }
        return names;
    }
}
