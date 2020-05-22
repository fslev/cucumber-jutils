package com.cucumber.utils.engineering.compare;

import com.cucumber.utils.context.props.internal.ScenarioPropsGenerator;
import com.cucumber.utils.engineering.utils.RegexUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.fail;

public class StringRegexCompare implements Placeholdable {
    private final Logger log = LogManager.getLogger();

    private final String expected;
    private final String actual;
    private final String message;

    public StringRegexCompare(Object expected, Object actual) {
        this(null, expected, actual);
    }

    public StringRegexCompare(String message, Object expected, Object actual) {
        this.expected = expected.toString();
        this.actual = actual.toString();
        this.message = message;
    }

    @Override
    public Map<String, Object> compare() {
        ScenarioPropsGenerator generator = new ScenarioPropsGenerator(expected, actual).match();
        Map<String, Object> generatedProperties = generator.getProperties();
        if (generator.getStandalonePlaceholderName() == null) {
            String substitutedExpected = generator.getSubstitutedSource().toString();
            String expectedWithQuotedSubstitutes = generator.getSourceWithQuotedSubstitutes();
            try {
                Pattern pattern = Pattern.compile(expectedWithQuotedSubstitutes, Pattern.DOTALL | Pattern.MULTILINE);
                if (!pattern.matcher(actual).matches()) {
                    checkStringContainsUnintentionalRegexChars(expectedWithQuotedSubstitutes);
                    fail(ParameterizedMessage.format("{}\nEXPECTED:\n{}\nBUT GOT:\n{}\n\nHint: Quote any regex patterns present inside Expected",
                            new Object[]{message != null ? message : "", expectedWithQuotedSubstitutes, actual}));
                }
                return generatedProperties;
            } catch (PatternSyntaxException e) {
                checkStringContainsUnintentionalRegexChars(expected);
                if (!substitutedExpected.equals(actual)) {
                    fail(ParameterizedMessage.format("{}\nEXPECTED:\n{}\nBUT GOT:\n{}", new Object[]{message != null ? message : "", substitutedExpected, actual}));
                }
                return generatedProperties;
            }
        }
        return generatedProperties;
    }

    private void checkStringContainsUnintentionalRegexChars(String expected) {
        if (!log.isDebugEnabled()) {
            return;
        }
        List<String> specialRegexCharList = RegexUtils.getRegexCharsFromString(expected);
        if (!specialRegexCharList.isEmpty()) {
            log.debug(" \n\n Comparison mechanism failed while comparing strings." +
                            " \n Make sure expected String has no unintentional regex special characters that failed the comparison. " +
                            "\n If so, try to quote them by using \\Q and \\E or simply \\" +
                            "\n Found the following list of special regex characters inside expected: {}\nExpected:\n{}\n",
                    specialRegexCharList, expected);
        }
    }
}
