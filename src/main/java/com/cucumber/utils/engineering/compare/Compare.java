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
    private boolean nonExtensibleObject;
    private boolean nonExtensibleArray;
    private String message;

    public Compare(Object expected, Object actual) {
        this(null, expected, actual, false, false);
    }

    public Compare(String message, Object expected, Object actual) {
        this(message, expected, actual, false, false);
    }

    public Compare(Object expected, Object actual, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        this(null, expected, actual, nonExtensibleObject, nonExtensibleArray);
    }

    public Compare(String message, Object expected, Object actual, boolean nonExtensibleObject, boolean nonExtensibleArray) {
        this.expected = expected;
        this.actual = actual;
        this.nonExtensibleObject = nonExtensibleObject;
        this.nonExtensibleArray = nonExtensibleArray;
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
            matcher = new JsonCompare(message, expected, actual, nonExtensibleObject, nonExtensibleArray);
        } catch (Exception e) {
            log.debug("Compared objects are NOT JSONs:\nEXPECTED:\n{}\nACTUAL:\n{}\n--> proceed to XML compare", expected, actual);
            try {
                log.debug("Compare as XMLs");
                matcher = new XmlCompare(message, expected, actual);
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
        } else if (expected == null) {
            return true;
        }
        return false;
    }
}
