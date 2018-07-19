package ro.cucumber.core.matchers;

import java.util.Map;

public interface SymbolsAssignMatchable {

    /**
     * @return A Map of assign symbols
     */
    Map<String, String> match();
}
