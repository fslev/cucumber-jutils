package ro.cucumber.core.engineering.compare;

import ro.cucumber.core.engineering.compare.comparators.CustomJsonComparator;
import ro.cucumber.core.engineering.compare.exceptions.CompareException;
import ro.skyah.comparator.JSONCompare;
import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class JsonCompare implements SymbolsDefineComparable {

    private JsonNode expected;
    private JsonNode actual;
    private CustomJsonComparator comparator = new CustomJsonComparator();

    public JsonCompare(Object expected, Object actual) throws CompareException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        try {
            this.expected = mapper.readTree(expected.toString());
            this.actual = mapper.readTree(actual.toString());
            if (!this.expected.getNodeType().equals(JsonNodeType.OBJECT)
                    && !this.expected.getNodeType().equals(JsonNodeType.ARRAY)
                    && !this.actual.getNodeType().equals(JsonNodeType.OBJECT)
                    && !this.actual.getNodeType().equals(JsonNodeType.ARRAY)) {
                throw new CompareException("Malformed JSON");
            }
        } catch (IOException e) {
            throw new CompareException("Malformed JSON");
        }
    }

    @Override
    public Map<String, String> compare() {
        JSONCompare.assertEquals(expected, actual, comparator);
        return comparator.getAssignSymbols();
    }
}
