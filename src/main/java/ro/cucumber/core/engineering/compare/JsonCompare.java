package ro.cucumber.core.engineering.compare;

import ro.cucumber.core.engineering.compare.comparators.CustomJsonComparator;
import ro.cucumber.core.engineering.compare.exceptions.CompareException;
import ro.cucumber.core.engineering.utils.JsonUtils;
import ro.skyah.comparator.CompareMode;
import ro.skyah.comparator.JSONCompare;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class JsonCompare implements Placeholdable {

    private JsonNode expected;
    private JsonNode actual;
    private CustomJsonComparator comparator = new CustomJsonComparator();
    private boolean nonExtensibleObject;
    private boolean nonExtensibleArray;
    private boolean arrayStrictOrder;

    public JsonCompare(Object extpected, Object actual) throws CompareException {
        this(extpected, actual, false, false, false);
    }

    public JsonCompare(Object expected, Object actual, boolean nonExtensibleObject, boolean nonExtensibleArray) throws CompareException {
        this(expected, actual, nonExtensibleObject, nonExtensibleArray, false);
    }

    public JsonCompare(Object expected, Object actual, boolean arrayStrictOrder) throws CompareException {
        this(expected, actual, false, false, arrayStrictOrder);
    }

    public JsonCompare(Object expected, Object actual, boolean nonExtensibleObject, boolean nonExtensibleArray,
                       boolean arrayStrictOrder) throws CompareException {
        try {
            this.expected = JsonUtils.toJson(expected.toString());
            this.actual = JsonUtils.toJson(actual.toString());
            if (!this.expected.getNodeType().equals(JsonNodeType.OBJECT)
                    && !this.expected.getNodeType().equals(JsonNodeType.ARRAY)
                    && !this.actual.getNodeType().equals(JsonNodeType.OBJECT)
                    && !this.actual.getNodeType().equals(JsonNodeType.ARRAY)) {
                throw new CompareException("Malformed JSON");
            }
            this.nonExtensibleObject = nonExtensibleObject;
            this.nonExtensibleArray = nonExtensibleArray;
            this.arrayStrictOrder = arrayStrictOrder;
        } catch (IOException e) {
            throw new CompareException("Malformed JSON");
        }
    }

    @Override
    public Map<String, String> compare() {
        JSONCompare.assertEquals(expected, actual, comparator, compareModes());
        return comparator.getAssignSymbols();
    }

    private CompareMode[] compareModes() {
        Set<CompareMode> modes = new HashSet<>();
        if (nonExtensibleObject) {
            modes.add(CompareMode.JSON_OBJECT_NON_EXTENSIBLE);
        }
        if (nonExtensibleArray) {
            modes.add(CompareMode.JSON_ARRAY_NON_EXTENSIBLE);
        }
        if (arrayStrictOrder) {
            modes.add(CompareMode.JSON_ARRAY_STRICT_ORDER);
        }
        return modes.toArray(new CompareMode[modes.size()]);
    }
}
