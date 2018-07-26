package ro.cucumber.core.engineering.symbols;


import ro.cucumber.core.engineering.utils.RegexUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SymbolDefineFromMatch {

    private static final String SYMBOL_START = "~\\[";
    private static final String SYMBOL_END = "\\]";
    private static final String SYMBOL_REGEX =
            SYMBOL_START + "(.*?)" + SYMBOL_END;

    private static final Pattern SYMBOL_PATTERN = Pattern.compile(SYMBOL_REGEX,
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

    private String stringWithSymbols;
    private String stringWithValues;
    private Map<String, String> symbolValues = new LinkedHashMap<>();

    public SymbolDefineFromMatch(String stringWithSymbols, String stringWithValues) {
        this.stringWithSymbols = stringWithSymbols;
        this.stringWithValues = stringWithValues;
        parseSymbols();
    }

    public String parse() {
        String str = stringWithSymbols;
        for (Map.Entry<String, String> e : symbolValues.entrySet()) {
            str = str.replaceAll(SYMBOL_START + e.getKey() + SYMBOL_END,
                    e.getValue());
        }
        return str;
    }

    public Map<String, String> getSymbolValues() {
        return this.symbolValues;
    }

    private void parseSymbols() {
        List<String> symbolNames = getSymbolNames();
        if (symbolNames.isEmpty()) {
            return;
        }
        boolean isRegex = RegexUtils.isRegex(stringWithSymbols);
        String str = !isRegex ? "\\Q" + stringWithSymbols + "\\E" : stringWithSymbols;
        for (String name : symbolNames) {
            str = str.replaceAll(SYMBOL_START + name + SYMBOL_END,
                    !isRegex ? "\\\\E(.*)\\\\Q" : "(.*)");
        }
        Pattern pattern =
                Pattern.compile(str, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(stringWithValues);
        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                symbolValues.put(symbolNames.get(i - 1), matcher.group(i));
            }
        }
    }

    private List<String> getSymbolNames() {
        List<String> names = new ArrayList<>();
        Matcher matcher = SYMBOL_PATTERN.matcher(stringWithSymbols);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                names.add(matcher.group(i));
            }
        }
        return names;
    }
}
