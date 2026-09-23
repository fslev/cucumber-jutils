package com.cucumber.utils.context.vars.internal;

import com.cucumber.utils.context.ScenarioUtils;
import io.json.compare.util.MessageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionException;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.CompositeStringExpression;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.annotation.Nullable;

public class SpELParser {

    private SpELParser() {
    }

    private static final Logger LOG = LogManager.getLogger();
    private static final SpelExpressionParser SPEL_EXPRESSION_PARSER = new SpelExpressionParser();
    private static final String PREFIX = ParserContext.TEMPLATE_EXPRESSION.getExpressionPrefix();
    private static final String SUFFIX = ParserContext.TEMPLATE_EXPRESSION.getExpressionSuffix();
    private static final String RESULT_LOG = "SpEL {}{}{} -> {}";

    public static Object parseQuietly(String source) {
        return parseQuietly(source, null);
    }

    /**
     * Logs each evaluated expression to the scenario log when {@code scenarioUtils} has a running scenario, otherwise to Log4j.
     */
    public static Object parseQuietly(String source, @Nullable ScenarioUtils scenarioUtils) {
        if (source.contains(PREFIX) && source.contains(SUFFIX)) {
            try {
                Expression exp = SPEL_EXPRESSION_PARSER.parseExpression(source, ParserContext.TEMPLATE_EXPRESSION);
                return exp instanceof CompositeStringExpression template
                        ? evaluateParts(template, scenarioUtils) : evaluate(exp, Object.class, scenarioUtils);
            } catch (Exception e) {
                LOG.warn("Invalid SpEL expression, value left unparsed: {}{}{}", reasonOf(e), System.lineSeparator(), source);
            }
        }
        return source;
    }

    // Same concatenation as CompositeStringExpression#getValue, done here so each part's result gets logged
    private static String evaluateParts(CompositeStringExpression template, @Nullable ScenarioUtils scenarioUtils) {
        StringBuilder sb = new StringBuilder();
        for (Expression part : template.getExpressions()) {
            String value = evaluate(part, String.class, scenarioUtils);
            if (value != null) {
                sb.append(value);
            }
        }
        return sb.toString();
    }

    private static <T> T evaluate(Expression exp, Class<T> type, @Nullable ScenarioUtils scenarioUtils) {
        T value = exp.getValue(type);
        if (exp instanceof SpelExpression) {
            logResult(exp.getExpressionString(), value, scenarioUtils);
        }
        return value;
    }

    private static void logResult(String expression, Object value, @Nullable ScenarioUtils scenarioUtils) {
        if (scenarioUtils != null && scenarioUtils.getScenario() != null) {
            scenarioUtils.log(RESULT_LOG, PREFIX, expression, SUFFIX, value != null ? MessageUtil.cropL(value.toString()) : null);
        } else {
            LOG.info(RESULT_LOG, PREFIX, expression, SUFFIX, value);
        }
    }

    private static String reasonOf(Exception e) {
        return e instanceof ExpressionException ee ? ee.getSimpleMessage() : e.toString();
    }
}
