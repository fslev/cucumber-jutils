package ro.cucumber.core.context.props;

import ro.cucumber.core.context.config.CustomInjectorSource;
import ro.cucumber.core.engineering.symbols.EnvironmentSymbolParser;
import ro.cucumber.core.engineering.symbols.ScenarioSymbolParser;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SymbolsParser {

    private String target;
    private ScenarioProps scenarioProps = getScenarioProps();

    public SymbolsParser(String target) {
        this.target = target;
    }

    private static ScenarioProps getScenarioProps() {
        return CustomInjectorSource.getContextInjector().getInstance(ScenarioProps.class);
    }

    public Object parse() {
        String standaloneSymbol = getStandaloneScenarioSymbol();
        if (standaloneSymbol != null) {
            Object val = scenarioProps.get(standaloneSymbol);
            return val != null ? val : target;
        }
        return getParsedStringWithEnvironmentValues(getParsedStringWithScenarioValues(target));
    }

    private static String getParsedStringWithEnvironmentValues(String str) {
        EnvironmentSymbolParser parser = new EnvironmentSymbolParser(str);
        Set<String> symbolNames = parser.searchForSymbols();
        Map<String, String> values = new HashMap();
        symbolNames.forEach((String name) -> {
            String val = EnvProps.get(name);
            if (val != null) {
                values.put(name, val);
            }
        });
        return parser.parse(values);
    }

    private String getParsedStringWithScenarioValues(String str) {
        ScenarioSymbolParser parser = new ScenarioSymbolParser(str);
        Set<String> symbolNames = parser.searchForSymbols();
        Map<String, String> values = new HashMap();
        symbolNames.forEach((String name) -> {
            Object val = scenarioProps.get(name);
            if (val != null) {
                values.put(name, val.toString());
            }
        });
        return parser.parse(values);
    }

    private String getStandaloneScenarioSymbol() {
        ScenarioSymbolParser parser = new ScenarioSymbolParser(target);
        Set<String> symbolNames = parser.searchForSymbols();
        if (!symbolNames.isEmpty()) {
            String element = symbolNames.iterator().next();
            if ((ScenarioSymbolParser.SYMBOL_START + element + ScenarioSymbolParser.SYMBOL_END)
                    .equals(target)) {
                return element;
            }
        }
        return null;
    }
}
