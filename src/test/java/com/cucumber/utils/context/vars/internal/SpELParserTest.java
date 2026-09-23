package com.cucumber.utils.context.vars.internal;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Property;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpELParserTest {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    private final CapturingAppender appender = new CapturingAppender();
    private LoggerConfig loggerConfig;

    @BeforeEach
    public void captureLogs() {
        appender.start();
        loggerConfig = LoggerContext.getContext(false).getConfiguration().getLoggerConfig(SpELParser.class.getName());
        loggerConfig.addAppender(appender, null, null);
    }

    @AfterEach
    public void stopCapturingLogs() {
        loggerConfig.removeAppender(appender.getName());
        appender.stop();
    }

    @Test
    public void emptySourceTest() {
        assertEquals(SpELParser.parseQuietly(""), "");
    }

    @Test
    public void testSimpleParse() {
        String s = "#{T(java.net.IDN).toASCII('testá.com')}";
        assertEquals("xn--test-8na.com", SpELParser.parseQuietly(s));
    }

    @Test
    public void testEscapedExpressions() {
        String s = "two plus three is #{2+3} and one two plus two is #{2+2}";
        assertEquals("two plus three is 5 and one two plus two is 4", SpELParser.parseQuietly(s));

        // one escaped expression prefix
        s = "two plus three is \\#{2+3} and one two plus two is #{2+2}";
        assertEquals("two plus three is \\5 and one two plus two is 4", SpELParser.parseQuietly(s));

        // two escaped expression prefixes
        s = "two plus three is \\#{2+3} and one two plus two is \\#{2+2}";
        assertEquals("two plus three is \\5 and one two plus two is \\4", SpELParser.parseQuietly(s));
    }

    @Test
    public void invalidSpelExpression() {
        String s = "#{(java.net.IDN).toASCII('testá.com')}";
        assertEquals("#{(java.net.IDN).toASCII('testá.com')}", SpELParser.parseQuietly(s));
    }

    @Test
    public void multipleInvalidSpelExpressions() {
        String s = "test #{1+2} and #{invalid";
        assertEquals("test #{1+2} and #{invalid", SpELParser.parseQuietly(s));

        s = "test #{1+2} and #{'#{'}invalid";
        assertEquals("test 3 and #{invalid", SpELParser.parseQuietly(s));
    }

    @Test
    public void invalidSpelContent() {
        String s = "T(invalid.net.IDN).toASCII('testá.com')";
        assertEquals("T(invalid.net.IDN).toASCII('testá.com')", SpELParser.parseQuietly(s));
    }

    @Test
    public void spELGeneratesNull() {
        assertNull(SpELParser.parseQuietly("#{T(com.cucumber.utils.context.vars.internal.SpELParserTest).returnsNull()}"));
    }

    @Test
    public void templatePartEvaluatingToNullIsSkipped() {
        assertEquals("a  b", SpELParser.parseQuietly("a #{null} b"));
    }

    @Test
    public void testSpelParsingOfMultipleExpressions() {
        String s = "#{T(java.net.IDN).toASCII('testá.com')}#{T(java.net.IDN).toASCII('testá.com')}";
        assertEquals("xn--test-8na.comxn--test-8na.com", SpELParser.parseQuietly(s));
    }

    @Test
    public void testSpelParsingOfMultipleExpressionsWithOneInvalidDelimited() {
        String s = "#{T(java.net.IDN).toASCII('testá.com')}#{'#{'}T(java.net.IDN).toASCII('testá.com')";
        assertEquals("xn--test-8na.com#{T(java.net.IDN).toASCII('testá.com')", SpELParser.parseQuietly(s));
    }

    @Test
    public void testSpelParsingOfMultipleExpressionsWithOneInvalid() {
        String s = "#{T(java.net.IDN).toASCII('testá.com')}#{'#{'}T(jav.net.IDN).toASCII('testá.com')}";
        assertEquals("xn--test-8na.com#{T(jav.net.IDN).toASCII('testá.com')}", SpELParser.parseQuietly(s));
    }

    @Test
    public void testSpelParsingOfMultipleExpressionsWithOneLiteral() {
        String s = "#{T(java.net.IDN).toASCII('testá.com')}#{'#{T(java.net.IDN).toASCII(''testá.com'')}'}";
        assertEquals("xn--test-8na.com#{T(java.net.IDN).toASCII('testá.com')}", SpELParser.parseQuietly(s));
    }

    @Test
    public void testSpelParsingOfExpressionContainingBackslash() {
        String s = "#{('a\\Bc'+'d\\Ef').toLowerCase()}#{('g\\hi').toLowerCase()}";
        assertEquals("a\\bcd\\efg\\hi", SpELParser.parseQuietly(s));
    }

    @Test
    public void testSpelParsingOfExpressionContainingEscapedBraces() {
        String s = "#{'abcD\\}EF'.toLowerCase()} and #{'abcD\\}EF'.toLowerCase()}";
        assertEquals("abcd\\}ef and abcd\\}ef", SpELParser.parseQuietly(s));
    }

    @Test
    public void logsEachEvaluatedExpressionOfTemplate() {
        SpELParser.parseQuietly("two plus three is #{2+3} and two plus two is #{2+2}");
        assertEquals(List.of("SpEL #{2+3} -> 5", "SpEL #{2+2} -> 4"), logged(Level.INFO));
        assertEquals(List.of(), logged(Level.WARN));
    }

    @Test
    public void logsStandaloneExpression() {
        SpELParser.parseQuietly("#{T(java.net.IDN).toASCII('testá.com')}");
        assertEquals(List.of("SpEL #{T(java.net.IDN).toASCII('testá.com')} -> xn--test-8na.com"), logged(Level.INFO));
    }

    @Test
    public void logsTheResultThatIsReturned() {
        COUNTER.set(0);
        String counter = "#{T(com.cucumber.utils.context.vars.internal.SpELParserTest).nextCount()}";
        assertEquals(1, SpELParser.parseQuietly(counter));
        assertEquals("count: 2", SpELParser.parseQuietly("count: " + counter));
        assertEquals(List.of("SpEL " + counter + " -> 1", "SpEL " + counter + " -> 2"), logged(Level.INFO));
    }

    @Test
    public void warnsWithReasonAndSourceWhenEvaluationFails() {
        String s = "This is: #{T(java.net.I).toASCII('testá.com')}";
        assertEquals(s, SpELParser.parseQuietly(s));
        String warning = singleWarning();
        assertTrue(warning.contains("EL1005E: Type cannot be found 'java.net.I'"), warning);
        assertTrue(warning.contains(s), warning);
    }

    @Test
    public void warnsWithExceptionTypeWhenInvokedMethodThrows() {
        String s = "#{T(java.lang.Integer).parseInt('x')}";
        assertEquals(s, SpELParser.parseQuietly(s));
        String warning = singleWarning();
        assertTrue(warning.contains("java.lang.NumberFormatException: For input string: \"x\""), warning);
        assertTrue(warning.contains(s), warning);
    }

    @Test
    public void warnsWithoutEvaluatingWhenTemplateCannotBeParsed() {
        String s = "test #{1+2} and #{invalid";
        assertEquals(s, SpELParser.parseQuietly(s));
        String warning = singleWarning();
        assertTrue(warning.contains("No ending suffix '}' for expression starting at character 16: #{invalid"), warning);
        assertTrue(warning.contains(s), warning);
        assertEquals(List.of(), logged(Level.INFO));
    }

    @Test
    public void logsNothingForTextWithoutExpressions() {
        SpELParser.parseQuietly("T(invalid.net.IDN).toASCII('testá.com')");
        assertEquals(List.of(), logged(Level.INFO));
        assertEquals(List.of(), logged(Level.WARN));
    }

    public static Object returnsNull() {
        return null;
    }

    public static int nextCount() {
        return COUNTER.incrementAndGet();
    }

    private List<String> logged(Level level) {
        return appender.events.stream()
                .filter(e -> e.getLoggerName().equals(SpELParser.class.getName()) && e.getLevel() == level)
                .map(e -> e.getMessage().getFormattedMessage())
                .toList();
    }

    private String singleWarning() {
        List<String> warnings = logged(Level.WARN);
        assertEquals(1, warnings.size(), warnings::toString);
        return warnings.get(0);
    }

    private static final class CapturingAppender extends AbstractAppender {

        private final List<LogEvent> events = new ArrayList<>();

        CapturingAppender() {
            super("SpELParserTest", null, null, true, Property.EMPTY_ARRAY);
        }

        @Override
        public void append(LogEvent event) {
            events.add(event.toImmutable());
        }
    }
}
