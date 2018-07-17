package ro.cucumber.core.symbols;


import ro.cucumber.core.utils.RegexUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SymbolsAssignParser {

    private static final String SYMBOL_ASSIGN_START = "~\\[";
    private static final String SYMBOL_ASSIGN_END = "\\]";
    private static final String SYMBOL_ASSIGN_REGEX =
            SYMBOL_ASSIGN_START + "(.*?)" + SYMBOL_ASSIGN_END;

    private static final Pattern SYMBOL_ASSIGN_PATTERN = Pattern.compile(SYMBOL_ASSIGN_REGEX,
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

    private String stringWithSymbols;
    private String stringWithValues;
    private Map<String, String> assignSymbols = new LinkedHashMap<>();

    public SymbolsAssignParser(String stringWithSymbols, String stringWithValues) {
        this.stringWithSymbols = stringWithSymbols;
        this.stringWithValues = stringWithValues;
        setAssignSymbols();
    }

    public String getStringWithAssignValues() {
        String str = stringWithSymbols;
        for (Map.Entry<String, String> e : assignSymbols.entrySet()) {
            str = str.replaceAll(SYMBOL_ASSIGN_START + e.getKey() + SYMBOL_ASSIGN_END,
                    e.getValue());
        }
        return str;
    }

    public Map<String, String> getAssignSymbols() {
        return this.assignSymbols;
    }

    public void setAssignSymbols() {
        List<String> symbolNames = getAssignSymbolNames(stringWithSymbols);
        if (symbolNames.isEmpty()) {
            return;
        }
        boolean isRegex = RegexUtils.isRegex(stringWithSymbols);
        String str = !isRegex ? "\\Q" + stringWithSymbols + "\\E" : stringWithSymbols;
        for (String name : symbolNames) {
            str = str
                    .replaceAll(SYMBOL_ASSIGN_START + name + SYMBOL_ASSIGN_END, !isRegex ? "\\\\E(.*)\\\\Q" : "(.*)");
        }
        Pattern pattern = Pattern.compile(str,
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(stringWithValues);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                assignSymbols.put(symbolNames.get(i - 1), matcher.group(i));
            }
        }
    }

    public static List<String> getAssignSymbolNames(String str) {
        List<String> names = new ArrayList<>();
        Matcher matcher = SYMBOL_ASSIGN_PATTERN.matcher(str);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                names.add(matcher.group(i));
            }
        }
        return names;
    }
}
