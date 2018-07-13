package tests.ro.cucumber.core;

import ro.cucumber.core.utils.SymbolsAssignParser;
import ro.cucumber.core.utils.XmlUtils;
import org.junit.Test;

public class TestSymbolsAssignParser {

    @Test
    public void testSimpleText() {
        String a = "1234~[var1]6~[var2]0";
        String b = "1234567890";
        System.out.println(new SymbolsAssignParser(a, b).getAssignSymbols());
        System.out.println(new SymbolsAssignParser(a, b).getStringWithAssignValues());
        String source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "   <Emp id=\"1\"><name> Pankaj</name>   <age>25</age>\n"
                + "              <role>Deve ~[here]oper</role><gen>Ma~[blah2]fds</gen></Emp>";
        final String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "   <Emp id=\"1\"><name> Pankaj</name>   <age>25</age>\n"
                + "              <role>Deve ~[here]oper</role><gen>Ma > karce fds</gen></Emp>";
        // String source1 = "$[var1]r$[var2]";
        // final String xmlStr1 = "scra2";
        // System.out.println(XmlUtils.toSingleLineString(source));
        // System.out.println(XmlUtils.toSingleLineString(xmlStr));
        // Map<String, String> map = getAssignSymbols(source1, xmlStr1);
        // System.out.println(map);
        // System.out.println(getStringWithAssignValues(source1, map));
        // System.out.println(XmlUtils.toSingleLineString(xmlStr));
        System.out.println(new SymbolsAssignParser(XmlUtils.toSingleLineString(source),
                XmlUtils.toSingleLineString(xmlStr)).getAssignSymbols());
        System.out.println(new SymbolsAssignParser(XmlUtils.toSingleLineString(source),
                XmlUtils.toSingleLineString(xmlStr)).getStringWithAssignValues());
    }
}
