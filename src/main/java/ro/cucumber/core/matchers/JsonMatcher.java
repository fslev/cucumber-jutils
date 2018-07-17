package ro.cucumber.core.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ro.skyah.comparator.JSONCompare;

import java.io.IOException;
import java.util.Map;

public class JsonMatcher implements MatcherWithAssignableSymbols {

    private JsonNode expectedNode;
    private JsonNode actualNode;
    private CustomJsonComparator comparator = new CustomJsonComparator();

    public JsonMatcher(Object expected, Object actual) throws MatcherException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            expectedNode = mapper.readTree(expected.toString());
            actualNode = mapper.readTree(actual.toString());
        } catch (IOException e) {
            throw new MatcherException("Malformed JSON");
        }
    }

    public void matches() {
        JSONCompare.assertEquals(expectedNode, actualNode, comparator);
    }

    @Override
    public Map<String, String> getAssignSymbols() {
        return comparator.getAssignSymbols();
    }
}
