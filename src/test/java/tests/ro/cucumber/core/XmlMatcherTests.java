package tests.ro.cucumber.core;

import ro.cucumber.core.matchers.JsonMatcher;
import ro.cucumber.core.matchers.MatcherException;
import ro.cucumber.core.matchers.XmlMatcher;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class XmlMatcherTests {

    @Test(expected = MatcherException.class)
    public void compareMalformedXml() throws MatcherException {
        String expected = "<struct><int a=2>3da</int><boolean>false</boolean></struct>";
        String actual = "<struct><int a=2>3da</int><boolean>false</boolean></struct>";
        new XmlMatcher(expected, actual);
    }

    @Test
    public void compareXmlWithAssignSymbolsAndInvalidRegex() throws MatcherException {
        String expected =
                "<struct><int a=\"~[sym1]\">some text here</int><boolean a=\"bo~[sym2]ue\">false</boolean></struct>";
        String actual = "<struct><boolean a=\"boolAttrValue\">false</boolean>"
                + "<int a=\"(attrValue1\">some text here</int><str a=\"some value\"><a>sub text</a></str></struct>";
        XmlMatcher matcher = new XmlMatcher(expected, actual);
        assertTrue(matcher.getAssignSymbols().isEmpty());
        matcher.matches();
        Map<String, String> symbols = matcher.getAssignSymbols();
        assertEquals("(attrValue1", symbols.get("sym1"));
        assertEquals("olAttrVal", symbols.get("sym2"));
        assertEquals(2, matcher.getAssignSymbols().size());
    }

    @Test
    public void compareSimpleJson() throws MatcherException {
        String expected = "{\"!b\":\"val1\",\"a\":\"val2\"}";
        String actual = "{\"a\":\"val2\",\"c\":\"val1\"}";
        JsonMatcher matcher = new JsonMatcher(expected, actual);
        assertTrue(matcher.getAssignSymbols().isEmpty());
        matcher.matches();
        assertTrue(matcher.getAssignSymbols().isEmpty());
    }
}
