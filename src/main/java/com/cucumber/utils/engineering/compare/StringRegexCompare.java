package com.cucumber.utils.engineering.compare;

import com.cucumber.utils.engineering.placeholders.ScenarioPropertiesGenerator;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.fail;

public class StringRegexCompare implements Placeholdable {

    private String expected;
    private String actual;
    private Map<String, String> assignSymbols = new HashMap<>();
    private String message;

    public StringRegexCompare(Object expected, Object actual) {
        this(null, expected, actual);
    }

    public StringRegexCompare(String message, Object expected, Object actual) {
        this.expected = expected.toString();
        this.actual = actual.toString();
        this.message = message;
    }

    @Override
    public Map<String, String> compare() {
        ScenarioPropertiesGenerator generator = new ScenarioPropertiesGenerator(expected, actual);
        if (generator.targetIsStandaloneProperty()) {
            assignSymbols.putAll(generator.getProperties());
            return assignSymbols;
        }
        boolean hasAssignSymbols = !generator.getProperties().isEmpty();
        String parsedString = expected;
        if (hasAssignSymbols) {
            parsedString = generator.getParsedTarget();
        }
        try {
            Pattern pattern = Pattern.compile(parsedString, Pattern.DOTALL | Pattern.MULTILINE);
            if (pattern.matcher(actual).matches()) {
                if (hasAssignSymbols) {
                    this.assignSymbols.putAll(generator.getProperties());
                }
            } else {
                fail(ParameterizedMessage.format("{}\nEXPECTED:\n{}\nBUT GOT:\n{}",
                        new String[]{message != null ? message : "", parsedString, actual}));
            }
        } catch (PatternSyntaxException e) {
            if (parsedString.equals(actual)) {
                if (hasAssignSymbols) {
                    this.assignSymbols.putAll(generator.getProperties());
                }
            } else {
                fail(ParameterizedMessage.format("{}\nEXPECTED:\n{}\nBUT GOT:\n{}", new String[]{message != null ? message : "", parsedString, actual}));
            }
        }
        return assignSymbols;
    }
}
