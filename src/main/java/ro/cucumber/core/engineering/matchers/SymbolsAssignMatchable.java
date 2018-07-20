package ro.cucumber.core.engineering.matchers;

import java.util.Map;

public interface SymbolsAssignMatchable {

    /**
     * @return A Map of assign symbols
     */
    Map<String, String> match();
}
