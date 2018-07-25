package ro.cucumber.core.engineering.placeholder;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractSymbolParser {

    protected String stringWithPlaceholders;

    public AbstractSymbolParser(String stringWithPlaceholders) {
        if (stringWithPlaceholders == null) {
            throw new RuntimeException("I don't do NULLs here...");
        }
        this.stringWithPlaceholders = stringWithPlaceholders;
    }

    public String parse(Map<String, String> values) {
        String str = stringWithPlaceholders;
        for (Map.Entry<String, String> e : values.entrySet()) {
            str = str.replaceAll(getSymbolStart() + e.getKey() + getSymbolEnd(), e.getValue());
        }
        return str;
    }

    protected abstract String getSymbolStart();

    protected abstract String getSymbolEnd();

    protected abstract Pattern getSymbolPattern();

    public Set<String> searchForSymbols() {
        Set<String> names = new HashSet<>();
        Matcher matcher = getSymbolPattern().matcher(stringWithPlaceholders);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                names.add(matcher.group(i));
            }
        }
        return names;
    }
}
