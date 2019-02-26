package com.cucumber.utils.engineering.placeholders;


import java.util.regex.Pattern;

public class ScenarioPlaceholderFiller extends AbstractPlaceholderFiller {

    public static final String PLACEHOLDER_START = "#[";
    public static final String PLACEHOLDER_END = "]";
    public static final String PLACEHOLDER_REGEX =
            "\\Q" + PLACEHOLDER_START + "\\E" + "(.*?)" + "\\Q" + PLACEHOLDER_END + "\\E";

    public static final Pattern PLACEHOLDER_PATTERN = Pattern.compile(PLACEHOLDER_REGEX,
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);


    public ScenarioPlaceholderFiller(String stringWithSymbols) {
        super(stringWithSymbols);
    }

    @Override
    protected String getPlaceholderStart() {
        return "\\Q" + PLACEHOLDER_START + "\\E";
    }

    @Override
    protected String getPlaceholderEnd() {
        return "\\Q" + PLACEHOLDER_END + "\\E";
    }

    @Override
    protected Pattern getPlaceholderPattern() {
        return PLACEHOLDER_PATTERN;
    }
}
