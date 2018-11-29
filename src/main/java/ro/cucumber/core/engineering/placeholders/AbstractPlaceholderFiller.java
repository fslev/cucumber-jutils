package ro.cucumber.core.engineering.placeholders;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractPlaceholderFiller {

    protected String stringWithPlaceholders;

    public AbstractPlaceholderFiller(String stringWithPlaceholders) {
        if (stringWithPlaceholders == null) {
            throw new RuntimeException("I don't do NULLs here...");
        }
        this.stringWithPlaceholders = stringWithPlaceholders;
    }

    public String fill(Map<String, String> values) {
        String str = stringWithPlaceholders;
        for (Map.Entry<String, String> e : values.entrySet()) {
            str = str.replaceAll(getPlaceholderStart() + e.getKey() + getPlaceholderEnd(), e.getValue());
        }
        return str;
    }

    protected abstract String getPlaceholderStart();

    protected abstract String getPlaceholderEnd();

    protected abstract Pattern getPlaceholderPattern();

    public Set<String> searchForPlaceholders() {
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
