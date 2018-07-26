package ro.cucumber.core.engineering.symbols;


import java.util.regex.Pattern;

public class EnvironmentSymbolParser extends AbstractSymbolParser {

    public static final String SYMBOL_START = "${";
    public static final String SYMBOL_END = "}";
    public static final String SYMBOL_REGEX =
            "\\Q" + SYMBOL_START + "\\E" + "(.*?)" + "\\Q" + SYMBOL_END + "\\E";

    public static final Pattern SYMBOL_PATTERN = Pattern.compile(SYMBOL_REGEX,
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);


    public EnvironmentSymbolParser(String stringWithSymbols) {
        super(stringWithSymbols);
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
