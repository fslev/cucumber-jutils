package ro.cucumber.core.engineering.compare;

import ro.cucumber.core.engineering.compare.comparators.CustomJsonComparator;
import ro.cucumber.core.engineering.compare.exceptions.CompareException;
import ro.skyah.comparator.CompareMode;
import ro.skyah.comparator.JSONCompare;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class JsonConvertibleObjectCompare implements Placeholdable {

    private JsonNode expected;
    private JsonNode actual;
    private CustomJsonComparator comparator = new CustomJsonComparator();
    private boolean extensibleObject;
    private boolean extensibleArray;
    private boolean arrayStrictOrder;

    public JsonConvertibleObjectCompare(Object expected, Object actual) throws CompareException {
        this(expected, actual, true, true, false);
    }

    public JsonConvertibleObjectCompare(Object expected, Object actual, boolean extensibleObject,
                                        boolean extensibleArray, boolean arrayStrictOrder) throws CompareException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

        try {
            this.expected = expected instanceof String ? mapper.readTree((String) expected) : mapper.convertValue(expected, JsonNode.class);
            this.actual = actual instanceof String ? mapper.readTree((String) actual) : mapper.convertValue(actual, JsonNode.class);
            if (!this.expected.getNodeType().equals(JsonNodeType.OBJECT)
                    && !this.expected.getNodeType().equals(JsonNodeType.ARRAY)
                    && !this.actual.getNodeType().equals(JsonNodeType.OBJECT)
                    && !this.actual.getNodeType().equals(JsonNodeType.ARRAY)) {
                throw new CompareException("No JSON string representation");
            }
            this.extensibleObject = extensibleObject;
            this.extensibleArray = extensibleArray;
            this.arrayStrictOrder = arrayStrictOrder;
        } catch (IOException e) {
            throw new CompareException("No JSON string representation");
        }
    }

    @Override
    public Map<String, String> compare() {
        JSONCompare.assertEquals(expected, actual, comparator, compareModes());
        return comparator.getAssignSymbols();
    }

    private CompareMode[] compareModes() {
        Set<CompareMode> modes = new HashSet<>();
        if (!extensibleObject) {
            modes.add(CompareMode.JSON_OBJECT_NON_EXTENSIBLE);
        }
        if (!extensibleArray) {
            modes.add(CompareMode.JSON_ARRAY_NON_EXTENSIBLE);
        }
        if (arrayStrictOrder) {
            modes.add(CompareMode.JSON_ARRAY_STRICT_ORDER);
        }
        return modes.toArray(new CompareMode[modes.size()]);
    }
}
