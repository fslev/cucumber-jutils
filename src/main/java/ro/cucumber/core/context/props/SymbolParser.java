package ro.cucumber.core.context.props;

import ro.cucumber.core.context.config.CustomInjectorSource;
import ro.cucumber.core.engineering.symbols.GlobalSymbolReplaceParser;
import ro.cucumber.core.engineering.symbols.ScenarioSymbolReplaceParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SymbolParser {

    public static String parse(String str) {
        return getParsedStringWithScenarioValues(getParsedStringWithGlobalValues(str));
    }

    private static ScenarioProps getScenarioProps() {
        return CustomInjectorSource.getContextInjector().getInstance(ScenarioProps.class);
    }

    private static String getParsedStringWithGlobalValues(String s) {
        GlobalSymbolReplaceParser parser = new GlobalSymbolReplaceParser(s);
        Set<String> symbols = parser.searchForSymbols();
        Map<String, String> values = new HashMap();
        symbols.forEach((String name) -> {
            String val = GlobalProps.get(name);
            if (val != null) {
                values.put(name, val);
            }
        });
        return parser.parse(values);
    }

    private static String getParsedStringWithScenarioValues(String s) {
        ScenarioSymbolReplaceParser parser = new ScenarioSymbolReplaceParser(s);
        Set<String> symbols = parser.searchForSymbols();
        Map<String, String> values = new HashMap();
        symbols.forEach((String name) -> {
            Object val = getScenarioProps().get(name);
            if (val != null) {
                values.put(name, val.toString());
            }
        });
        return parser.parse(values);
    }
}
