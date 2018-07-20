package ro.cucumber.core.engineering.symbols;


import java.util.regex.Pattern;

public class GlobalSymbolReplaceParser extends AbstractSymbolReplaceParser {

    private static final String SYMBOL_START = "\\$\\{";
    private static final String SYMBOL_END = "\\}";
    private static final String SYMBOL_REGEX =
            SYMBOL_START + "(.*?)" + SYMBOL_END;

    private static final Pattern SYMBOL_PATTERN = Pattern.compile(SYMBOL_REGEX,
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);


    public GlobalSymbolReplaceParser(String stringWithSymbols) {
        super(stringWithSymbols);
    }

    @Override
    protected String getSymbolStart() {
        return SYMBOL_START;
    }

    @Override
    protected String getSymbolEnd() {
        return SYMBOL_END;
    }

    @Override
    protected Pattern getSymbolRetrievePattern() {
        return SYMBOL_PATTERN;
    }
}
