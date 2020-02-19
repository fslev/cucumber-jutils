package com.cucumber.utils.engineering.compare;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

public class Compare implements Placeholdable {
    private Logger log = LogManager.getLogger();
    protected Object expected;
    protected Object actual;
    private boolean jsonNonExtensibleObject;
    private boolean jsonNonExtensibleArray;
    private boolean jsonArrayStrictOrder;
    private boolean xmlChildListLength;
    private boolean xmlChildListSequence;
    private boolean xmlElementNumAttributes;
    private String message;

    public Compare(Object expected, Object actual) {
        this(null, expected, actual, false, false, false, false, false, false);
    }

    public Compare(String message, Object expected, Object actual) {
        this(message, expected, actual, false, false, false, false, false, false);
    }

    public Compare(String message, Object expected, Object actual, boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray,
                   boolean jsonArrayStrictOrder, boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes) {
        this.expected = expected;
        this.actual = actual;
        this.jsonNonExtensibleObject = jsonNonExtensibleObject;
        this.jsonNonExtensibleArray = jsonNonExtensibleArray;
        this.jsonArrayStrictOrder = jsonArrayStrictOrder;
        this.xmlChildListLength = xmlChildListLength;
        this.xmlChildListSequence = xmlChildListSequence;
        this.xmlElementNumAttributes = xmlElementNumAttributes;
        this.message = message;
    }

    @Override
    /**
     * Returns a map of placeholders if the expected object contains special placeholders for assigning placeholders to variables ('~[...]')
     */
    public Map<String, String> compare() {
        if (nullsMatch()) {
            return new HashMap<>();
        }
        Placeholdable matcher;
        try {
            log.debug("Compare as JSONs");
            matcher = new JsonCompare(message, expected, actual, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder);
        } catch (Exception e) {
            log.debug("Compared objects are NOT JSONs:\nEXPECTED:\n{}\nACTUAL:\n{}\n--> proceed to XML compare", expected, actual);
            try {
                log.debug("Compare as XMLs");
                matcher = new XmlCompare(message, expected, actual, xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes);
            } catch (Exception e2) {
                log.debug("Compared objects are NOT XMLS:\nEXPECTED:\n{}\nACTUAL:\n{}\n--> proceed to string REGEX compare", expected, actual);
                matcher = new StringRegexCompare(message, expected, actual);
            }
        }
        return matcher.compare();
    }

    private boolean nullsMatch() {
        if (expected == null ^ actual == null) {
            fail(ParameterizedMessage.format("{}\nEXPECTED:\n[{}]\nBUT GOT:\n[{}]",
                    new Object[]{message != null ? message : "", expected, actual}));
        } else return expected == null;
        return false;
    }
}
