package ro.cucumber.core.matchers;

import java.util.Map;

public interface MatcherWithAssignableSymbols {

    void matches();

    /**
     * @return A map of assign symbols<br>
     * Empty, if no assign symbols are defined inside expected
     */
    Map<String, String> getAssignSymbols();
}
