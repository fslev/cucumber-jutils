package ro.cucumber.core.matchers;

import java.util.Map;

public abstract class AbstractMatcher {
    protected Object expected;
    protected Object actual;

    /**
     * @return A map of assign symbols<br>
     *         Empty, if no assign symbols are defined inside expected
     */
    protected abstract Map<String, String> matches();
}
