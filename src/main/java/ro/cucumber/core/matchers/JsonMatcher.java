package ro.cucumber.core.matchers;

import ro.cucumber.core.matchers.comparators.CustomJsonComparator;
import ro.cucumber.core.symbols.SymbolAssignable;
import ro.skyah.comparator.JSONCompare;
import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMatcher implements SymbolAssignable, Matchable {

    private JsonNode expected;
    private JsonNode actual;
    private CustomJsonComparator comparator = new CustomJsonComparator();

    public JsonMatcher(Object expected, Object actual) throws MatcherException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.expected = mapper.readTree(expected.toString());
            this.actual = mapper.readTree(actual.toString());
        } catch (IOException e) {
            throw new MatcherException("Malformed JSON");
        }
    }

    @Override
    public void matches() {
        JSONCompare.assertEquals(expected, actual, comparator);
    }

    @Override
    public Map<String, String> getAssignSymbols() {
        return comparator.getAssignSymbols();
    }
}
