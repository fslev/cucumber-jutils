package com.cucumber.utils.context.vars.internal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class SpELParser {
    private static final Logger LOG = LogManager.getLogger();
    private static final SpelExpressionParser SPEL_EXPRESSION_PARSER = new SpelExpressionParser();

    public static Object parseQuietly(String source) {
        if (source.contains(ParserContext.TEMPLATE_EXPRESSION.getExpressionPrefix()) &&
                source.contains(ParserContext.TEMPLATE_EXPRESSION.getExpressionSuffix())) {
            try {
                Expression exp = SPEL_EXPRESSION_PARSER.parseExpression(source, ParserContext.TEMPLATE_EXPRESSION);
                return exp.getValue(Object.class);
            } catch (Exception e) {
                LOG.warn("Found invalid SpEL expressions:\n{}", e.getMessage());
            }
        }
        return source;
    }
}
