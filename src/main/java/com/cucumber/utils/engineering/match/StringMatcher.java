package com.cucumber.utils.engineering.match;

import com.cucumber.utils.engineering.match.condition.MatchCondition;
import com.cucumber.utils.engineering.utils.RegexUtils;
import com.cucumber.utils.engineering.utils.StringParser;
import com.cucumber.utils.exceptions.InvalidTypeException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.fail;

/**
 * Matches Objects with Strings
 */
public class StringMatcher extends AbstractMatcher<Object> {

    public static final String CAPTURE_PLACEHOLDER_PREFIX = "~[";
    public static final String CAPTURE_PLACEHOLDER_SUFFIX = "]";

    private static final Pattern captureGroupPattern = Pattern.compile(Pattern.quote(CAPTURE_PLACEHOLDER_PREFIX) + "(.*?)" + Pattern.quote(CAPTURE_PLACEHOLDER_SUFFIX),
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

    public StringMatcher(String message, Object expected, Object actual, Set<MatchCondition> matchConditions) throws InvalidTypeException {
        super(message, expected, actual, matchConditions);
        String defaultMessage = "\nEXPECTED:\n" + expected + "\nBUT GOT:\n" + actual + "\n";
        this.message = this.message != null ? this.message + defaultMessage : defaultMessage;
    }

    @Override
    protected Object convert(Object value) {
        return value;
    }

    @Override
    public Map<String, Object> match() {
        if (matchConditions.contains(MatchCondition.DO_NOT_MATCH)) {
            matchConditions.remove(MatchCondition.DO_NOT_MATCH);
            try {
                positiveMatch();
            } catch (AssertionError e) {
                return new HashMap<>();
            }
            fail(negativeMatchMessage);
        }
        return positiveMatch();
    }

    private Map<String, Object> positiveMatch() {
        Map<String, Object> properties = new HashMap<>();

        if (matchesWithNull()) {
            return properties;
        }

        String expectedString = expected.toString();
        List<String> placeholderNames = StringParser.captureValues(expectedString, captureGroupPattern);

        if (placeholderNames.size() == 1 && expectedString.equals(CAPTURE_PLACEHOLDER_PREFIX + placeholderNames.get(0) + CAPTURE_PLACEHOLDER_SUFFIX)) {
            String standalonePlaceholder = placeholderNames.get(0);
            properties.put(standalonePlaceholder, actual);
            return properties;

        } else if (actual == null) {
            fail(message);

        } else if (!placeholderNames.isEmpty()) {
            List<String> capturedValues = StringParser.captureValues(actual.toString(), patternWithPlaceholdersAsCaptureGroups(expectedString, placeholderNames), true);
            if (capturedValues.isEmpty()) {
                fail(message);
            }
            for (int i = 0; i < capturedValues.size(); i++) {
                if (i < placeholderNames.size()) {
                    properties.put(placeholderNames.get(i), capturedValues.get(i));
                }
            }
            return properties;

        } else {
            try {
                Pattern pattern = Pattern.compile(expectedString, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
                if (!pattern.matcher(actual.toString()).matches()) {
                    debugIfStringContainsUnintentionalRegexChars(expectedString);
                    fail(message);
                }
            } catch (PatternSyntaxException e) {
                debugIfStringContainsUnintentionalRegexChars(expectedString);
                if (!expectedString.equals(actual)) {
                    fail(message);
                }
            }
        }
        return properties;
    }

    private boolean matchesWithNull() {
        if (expected == null) {
            if (actual != null) {
                fail(message);
            } else {
                return true;
            }
        }
        return false;
    }

    private static Pattern patternWithPlaceholdersAsCaptureGroups(String source, List<String> placeholderNames) {
        String s = source;
        boolean regex = RegexUtils.isRegex(source);
        for (String key : placeholderNames) {
            s = s.replace(CAPTURE_PLACEHOLDER_PREFIX + key + CAPTURE_PLACEHOLDER_SUFFIX, regex ? "(.*)" : "\\E(.*)\\Q");
        }
        return Pattern.compile(regex ? s : "\\Q" + s + "\\E", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
    }

    private static void debugIfStringContainsUnintentionalRegexChars(String expected) {
        if (!LOG.isDebugEnabled()) {
            return;
        }
        List<String> specialRegexCharList = RegexUtils.getRegexCharsFromString(expected);
        if (!specialRegexCharList.isEmpty()) {
            LOG.debug(" \n\n Comparison mechanism failed while comparing strings." +
                            " \n Make sure expected String has no unintentional regex special characters that failed the comparison. " +
                            "\n If so, try to quote them by using \\Q and \\E or simply \\" +
                            "\n Found the following list of special regex characters inside expected: {}\nExpected:\n{}\n",
                    specialRegexCharList, expected);
        }
    }
}
