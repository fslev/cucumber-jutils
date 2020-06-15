package com.cucumber.utils.context.props.internal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.regex.Pattern;

public class SpelParser {

    private static final Logger log = LogManager.getLogger();
    public static final String PREFIX = "#{";
    public static final String SUFFIX = "}";

    public static final Pattern captureGroupPattern = Pattern.compile(Pattern.quote(PREFIX) + "(.*?)" + Pattern.quote(SUFFIX),
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);


    public static Object parse(String source) {
        if (source == null || source.isEmpty()) {
            return source;
        }
        return StringParser.replacePlaceholders
                (source, PREFIX, SUFFIX, captureGroupPattern, SpelParser::parseExpression,
                        k -> isExpressionValid(k) && k != parseExpression(k));
    }

    private static Object parseExpression(String expression) {
        try {
            Expression exp = new SpelExpressionParser().parseExpression(expression);
            return exp.getValue(Object.class);
        } catch (Exception e) {
            log.warn("Could not parse SpEL expression: {}", e.getMessage());
            return expression;
        }
    }

    private static Boolean isExpressionValid(String expression) {
        try {
            new SpelExpressionParser().parseExpression(expression);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
