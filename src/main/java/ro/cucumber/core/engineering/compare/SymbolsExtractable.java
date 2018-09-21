package ro.cucumber.core.engineering.compare;

import java.util.Map;

public interface SymbolsExtractable {

    /**
     * @return A Map of symbol names with their corresponding values
     */
    Map<String, String> compare();
}
