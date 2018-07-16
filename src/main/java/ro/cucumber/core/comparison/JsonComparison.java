package ro.cucumber.core.comparison;

import java.util.Map;
import static org.junit.Assert.fail;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonComparison extends Comparison {

    private JsonNode expected;

    public JsonComparison(Object expected, Object actual) {
        super(expected, actual);
    }

    /**
     * 
     * @return A map of assign symbols<br>
     *         Empty map, if not assign symbols are defined inside expected
     */
    Map<String, String> evaluate() {
        if ((actual == null) != (expected == null)) {
            fail(String.format("Expected: [%s] But found: [%s]", expected, actual));
        }
        return null;
    }
}
