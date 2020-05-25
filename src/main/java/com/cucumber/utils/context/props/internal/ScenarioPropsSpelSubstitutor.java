package com.cucumber.utils.context.props.internal;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;
import java.util.regex.Pattern;

public class ScenarioPropsSpelSubstitutor {

    public static final String PREFIX = "#{";
    public static final String SUFFIX = "}";
    public static List <String> list;

    public static final Pattern captureGroupPattern = Pattern.compile(Pattern.quote(PREFIX) + "(.*?)" + Pattern.quote(SUFFIX),
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

    public static Object replace(String source) {
        if (source == null || source.isEmpty()) {
            return source;
        }
       list = StringParser.captureValues(source, captureGroupPattern);
        if (list.size() != 1) {
            return source;
        }
        return setSpelParam(list.get(0));
    }

    public static Object setSpelParam(String value) {
        try {
            ExpressionParser expressionParser = new SpelExpressionParser();
            Expression exp = expressionParser.parseExpression(value);
            return exp.getValue(Object.class);
        } catch (ParseException e) {
            e.printStackTrace();
           return value;
        }

    }
}