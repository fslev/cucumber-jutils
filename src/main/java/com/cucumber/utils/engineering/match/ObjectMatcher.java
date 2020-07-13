package com.cucumber.utils.engineering.match;

import com.cucumber.utils.engineering.match.condition.MatchCondition;
import com.cucumber.utils.exceptions.InvalidTypeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.fail;

public class ObjectMatcher {
    private final static Logger LOG = LogManager.getLogger();

    /**
     * @return properties captured after the match
     * Expected object can contain placeholders for capturing values from the actual object
     */
    public static Map<String, Object> match(String message, Object expected, Object actual, Set<MatchCondition> matchConditions) {
        AbstractMatcher<?> matcher;
        try {
            LOG.debug("Compare as JSONs");
            matcher = new JsonMatcher(message, expected, actual, matchConditions);
        } catch (InvalidTypeException e1) {
            LOG.debug("Objects are NOT JSONs:\nEXPECTED:\n{}\nACTUAL:\n{}\n--> proceed to XML compare", expected, actual);
            try {
                LOG.debug("Compare as XMLs");
                matcher = new XmlMatcher(message, expected, actual, matchConditions);
            } catch (InvalidTypeException e2) {
                LOG.debug("Objects are NOT XMLs:\nEXPECTED:\n{}\nACTUAL:\n{}\n--> proceed to string REGEX compare", expected, actual);
                try {
                    matcher = new StringMatcher(message, expected, actual, matchConditions);
                } catch (InvalidTypeException e3) {
                    throw new RuntimeException("Cannot compare objects");
                }
            }
        }
        return matcher.match();
    }

    private static boolean nullsMatch(String message, Object expected, Object actual) {
        if (expected == null ^ actual == null) {
            fail(ParameterizedMessage.format("{}\nEXPECTED:\n[{}]\nBUT GOT:\n[{}]",
                    new Object[]{message != null ? message : "", expected, actual}));
        } else return expected == null;
        return false;
    }
}
