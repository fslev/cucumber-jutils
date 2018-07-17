package ro.cucumber.core.matchers;

import ro.skyah.comparator.JSONCompare;
import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMatcher extends AbstractMatcher {

    protected JsonNode expectedNode;
    protected JsonNode actualNode;

    public JsonMatcher(Object expected, Object actual) throws MatcherException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            expectedNode = mapper.readTree(expected.toString());
            actualNode = mapper.readTree(actual.toString());
        } catch (IOException e) {
            throw new MatcherException("No json");
        }
    }

    /**
     * 
     * @return A map of assign symbols<br>
     *         Empty map, if not assign symbols are defined inside expected
     */
    public Map<String, String> matches() {
        JSONCompare.assertEquals(expectedNode, actualNode);
    }
}
