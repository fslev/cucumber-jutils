package com.cucumber.utils.context.vars.internal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionException;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.CompositeStringExpression;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class SpELParser {

    private SpELParser() {
    }

    private static final Logger LOG = LogManager.getLogger();
    private static final SpelExpressionParser SPEL_EXPRESSION_PARSER = new SpelExpressionParser();
    private static final String PREFIX = ParserContext.TEMPLATE_EXPRESSION.getExpressionPrefix();
    private static final String SUFFIX = ParserContext.TEMPLATE_EXPRESSION.getExpressionSuffix();

    public static Object parseQuietly(String source) {
        if (source.contains(PREFIX) && source.contains(SUFFIX)) {
            try {
                Expression exp = SPEL_EXPRESSION_PARSER.parseExpression(source, ParserContext.TEMPLATE_EXPRESSION);
                return exp instanceof CompositeStringExpression template ? evaluateParts(template) : evaluate(exp, Object.class);
            } catch (Exception e) {
                LOG.warn("Invalid SpEL expression, value left unparsed: {}{}{}", reasonOf(e), System.lineSeparator(), source);
            }
        }
        return source;
    }

    // Same concatenation as CompositeStringExpression#getValue, done here so each part's result gets logged
    private static String evaluateParts(CompositeStringExpression template) {
        StringBuilder sb = new StringBuilder();
        for (Expression part : template.getExpressions()) {
            String value = evaluate(part, String.class);
            if (value != null) {
                sb.append(value);
            }
        }
        return sb.toString();
    }

    private static <T> T evaluate(Expression exp, Class<T> type) {
        T value = exp.getValue(type);
        if (exp instanceof SpelExpression) {
            LOG.info("SpEL {}{}{} -> {}", PREFIX, exp.getExpressionString(), SUFFIX, value);
        }
        return value;
    }

    private static String reasonOf(Exception e) {
        return e instanceof ExpressionException ee ? ee.getSimpleMessage() : e.toString();
    }
}
