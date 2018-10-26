package ro.cucumber.core.engineering.compare;

import ro.cucumber.core.engineering.compare.comparators.CustomJsonComparator;
import ro.cucumber.core.engineering.compare.exceptions.CompareException;
import ro.cucumber.core.engineering.utils.JsonUtils;
import ro.skyah.comparator.JSONCompare;
import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class JsonCompare implements SymbolsExtractable {

    private JsonNode expected;
    private JsonNode actual;
    private CustomJsonComparator comparator = new CustomJsonComparator();

    public JsonCompare(Object expected, Object actual) throws CompareException {
        try {
            this.expected = JsonUtils.toJson(expected.toString());
            this.actual = JsonUtils.toJson(actual.toString());
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
