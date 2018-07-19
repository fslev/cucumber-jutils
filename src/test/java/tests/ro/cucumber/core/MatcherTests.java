package tests.ro.cucumber.core;

import ro.cucumber.core.matchers.Matcher;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MatcherTests {

    @Test
    public void compareJsonWithAssignSymbols() {
        String expected = "{\"b\":\"(~[sym1]\"}";
        String actual = "{\"a\":\"val2\",\"b\":\"(val1\"}";
        Matcher matcher = new Matcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertEquals("val1", symbols.get("sym1"));
        assertEquals(1, symbols.size());
    }

    @Test
    public void compareXmlWithAssignSymbols() {
        String expected =
                "<struct><int a=\"~[sym1]\">some ~[sym3] here</int><boolean a=\"bo~[sym2]ue\">false</boolean></struct>";
        String actual = "<struct><boolean a=\"boolAttrValue\">false</boolean>"
                + "<int a=\"(attrValue1\">some text here</int><str a=\"some value\"><a>sub text</a></str></struct>";
        Matcher matcher = new Matcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertEquals("(attrValue1", symbols.get("sym1"));
        assertEquals("olAttrVal", symbols.get("sym2"));
        assertEquals("text", symbols.get("sym3"));
        assertEquals(3, symbols.size());
    }

    @Test
    public void compareString() {
        String expected =
                "<struct<int a=\"~[sym1]\">some ~[sym3] here</int><boolean a=\"bo~[sym2]ue\">false</boolean></struct>";
        String actual =
                "<struct<int a=\"val1\">some val3 here</int><boolean a=\"boval2ue\">false</boolean></struct>";
        Matcher matcher = new Matcher(expected, actual);
        Map<String, String> symbols = matcher.match();
        assertEquals("val1", symbols.get("sym1"));
        assertEquals("val2", symbols.get("sym2"));
        assertEquals("val3", symbols.get("sym3"));
        assertEquals(3, symbols.size());
    }
}
