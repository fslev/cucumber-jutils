package ro.cucumber.core.symbols;

import java.util.Map;

public interface SymbolAssignable {

    /**
     * @return A map of symbols with values <br>
     */
    Map<String, String> getAssignSymbols();
}
