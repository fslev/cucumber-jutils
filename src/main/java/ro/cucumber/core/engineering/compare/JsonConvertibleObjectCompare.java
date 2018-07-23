package ro.cucumber.core.engineering.compare;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import ro.cucumber.core.engineering.compare.comparators.CustomJsonComparator;
import ro.cucumber.core.engineering.compare.exceptions.CompareException;
import ro.skyah.comparator.CompareMode;
import ro.skyah.comparator.JSONCompare;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class JsonConvertibleObjectCompare implements SymbolsAssignComparable {

    private JsonNode expected;
    private JsonNode actual;
    private CustomJsonComparator comparator = new CustomJsonComparator();

    public JsonConvertibleObjectCompare(Object expected, Object actual) throws CompareException {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter expectedSW = new StringWriter();
        StringWriter actualSW = new StringWriter();
        mapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
        try {
            mapper.writeValue(expectedSW, expected);
            mapper.writeValue(actualSW, actual);
            this.expected = mapper.readTree(expectedSW.toString());
            this.actual = mapper.readTree(actualSW.toString());
            if (!this.expected.getNodeType().equals(JsonNodeType.OBJECT)
                    && !this.expected.getNodeType().equals(JsonNodeType.ARRAY)
                    && !this.actual.getNodeType().equals(JsonNodeType.OBJECT)
                    && !this.actual.getNodeType().equals(JsonNodeType.ARRAY)) {
                throw new CompareException("No JSON string representation");
            }
        } catch (IOException e) {
            throw new CompareException("No JSON string representation");
        }
    }

    @Override
    public Map<String, String> compare() {
        JSONCompare.assertEquals(expected, actual, comparator,
                CompareMode.JSON_ARRAY_NON_EXTENSIBLE, CompareMode.JSON_OBJECT_NON_EXTENSIBLE);
        return comparator.getAssignSymbols();
    }
}
