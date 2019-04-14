package com.cucumber.utils.engineering.placeholders;


import java.util.regex.Pattern;

public class ScenarioPropertiesParser extends AbstractPropertiesParser {

    public static final String SYMBOL_START = "#[";
    public static final String SYMBOL_END = "]";
    public static final String SYMBOL_REGEX =
            "\\Q" + SYMBOL_START + "\\E" + "(.*?)" + "\\Q" + SYMBOL_END + "\\E";

    public static final Pattern SYMBOL_PATTERN = Pattern.compile(SYMBOL_REGEX,
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);


    public ScenarioPropertiesParser(String stringWithPlaceholders) {
        super(stringWithPlaceholders);
    }

    @Override
    protected String getSymbolStart() {
        return "\\Q" + SYMBOL_START + "\\E";
    }

    @Override
    protected String getSymbolEnd() {
        return "\\Q" + SYMBOL_END + "\\E";
    }

    @Override
    protected Pattern getSymbolPattern() {
        return SYMBOL_PATTERN;
    }
}
