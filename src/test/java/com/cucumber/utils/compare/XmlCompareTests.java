package com.cucumber.utils.compare;

import com.cucumber.utils.engineering.compare.XmlCompare;
import com.cucumber.utils.engineering.compare.exceptions.CompareException;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlCompareTests {

    @Test(expected = CompareException.class)
    public void compareMalformedXml() throws CompareException {
        String expected = "<struct><int a=2>3da</int><boolean>false</boolean></struct>";
        String actual = "<struct><int a=2>3da</int><boolean>false</boolean></struct>";
        new XmlCompare(expected, actual);
    }

    @Test
    public void compareXmlWithAssignSymbolsAndInvalidRegex() throws CompareException {
        String expected =
                "<struct><int a=\"~[sym1]\">some ~[sym3] here</int><boolean a=\"bo~[sym2]ue\">false</boolean></struct>";
        String actual = "<struct><boolean a=\"boolAttrValue\">false</boolean>"
                + "<int a=\"(attrValue1\">some text here</int><str a=\"some result\"><a>sub text</a></str></struct>";
        XmlCompare matcher = new XmlCompare(expected, actual);
        Map<String, Object> symbols = matcher.compare();
        assertEquals("(attrValue1", symbols.get("sym1"));
        assertEquals("olAttrVal", symbols.get("sym2"));
        assertEquals("text", symbols.get("sym3"));
        assertEquals(3, symbols.size());
    }

    @Test(expected = AssertionError.class)
    public void compareXmlWithAssignSymbolsAndInvalidRegex_negative() throws CompareException {
        String expected =
                "<struct><int a=\"X~[sym1]\">some ~[sym3] here</int><boolean a=\"bo~[sym2]ue\">false</boolean></struct>";
        String actual = "<struct><boolean a=\"boolAttrValue\">false</boolean>"
                + "<int a=\"(attrValue1\">some text here</int><str a=\"some result\"><a>sub text</a></str></struct>";
        XmlCompare matcher = new XmlCompare(expected, actual);
        matcher.compare();
    }

    @Test
    public void compareEmptyXml() throws CompareException {
        String expected = "<struct></struct>";
        String actual = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?><struct></struct>";
        XmlCompare matcher = new XmlCompare(expected, actual);
        Map<String, Object> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareSimpleXml() throws CompareException {
        String expected = "<struct><int>test</int><boolean>false</boolean></struct>";
        String actual =
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?><struct><boolean>false</boolean><int>test</int></struct>";
        XmlCompare matcher = new XmlCompare(expected, actual);
        Map<String, Object> symbols = matcher.compare();
        assertTrue(symbols.isEmpty());
    }

    @Test
    public void compareComplexXmlWithAssignSymbols() throws CompareException {
        String expected = "<bookstore>\n"
                + "    <book price=\"730.54\" ISBN=\"string\" publicationdate=\"~[pubDate]\">\n"
                + "        <author>\n" + "            <last-name>test~[lastName]</last-name>"
                + "        </author>\n" + "        <genre>string</genre>\n" + "    </book>\n"
                + "    <book price=\"~[price]\" ISBN=\"string\">\n"
                + "        <title>string</title>\n" + "        <author>\n"
                + "            <first-name>string</first-name>\n"
                + "            <last-name>string</last-name>\n" + "        </author>\n"
                + "    </book>\n" + "</bookstore>";
        String actual = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                + "<!-- Created with Liquid Studio -->\n"
                + "<bookstore xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                + "           xsi:noNamespaceSchemaLocation=\"BookStore.xsd\">\n"
                + "    <book price=\"730.54\" ISBN=\"string\" publicationdate=\"2016-02-27\">\n"
                + "        <title>string</title>\n" + "        <author>\n"
                + "            <first-name>string</first-name>\n"
                + "            <last-name>teststring</last-name>\n" + "        </author>\n"
                + "        <genre>string</genre>\n" + "    </book>\n"
                + "    <book price=\"6738.774\" ISBN=\"string\">\n"
                + "        <title>string</title>\n" + "        <author>\n"
                + "            <first-name>string</first-name>\n"
                + "            <last-name>string</last-name>\n" + "        </author>\n"
                + "    </book>\n" + "</bookstore>";
        XmlCompare matcher = new XmlCompare(expected, actual);
        Map<String, Object> symbols = matcher.compare();
        assertEquals("2016-02-27", symbols.get("pubDate"));
        assertEquals("string", symbols.get("lastName"));
        assertEquals("6738.774", symbols.get("price"));
        assertEquals(3, symbols.size());
    }

    @Test(expected = AssertionError.class)
    public void checkMessageFromXmlCompare() throws CompareException {
        String expected = "<struct>test</struct>";
        String actual = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?><struct></struct>";
        try {
            new XmlCompare("Some message", expected, actual).compare();
        } catch (AssertionError e) {
            assertTrue(e.getMessage().contains("Some message") && e.getMessage().contains("Expected:"));
            throw e;
        }
    }

    @Test
    public void compareXmlChildLength() throws CompareException {
        String expected = "<struct><int a=\"2\">3da</int><boolean>.*</boolean></struct>";
        String actual = "<struct><int a=\"2\">3da</int><boolean>false</boolean></struct>";
        new XmlCompare("", expected, actual, true, false, false).compare();
    }

    @Test(expected = AssertionError.class)
    public void compareXmlChildLength_negative() throws CompareException {
        String expected = "<struct><int a=\"2\">3da</int><boolean>.*</boolean></struct>";
        String actual = "<struct><int a=\"2\">3da</int><boolean>false</boolean><x>test</x></struct>";
        new XmlCompare("", expected, actual, true, false, false).compare();
    }

    @Test
    public void compareXmlChildOrder() throws CompareException {
        String expected = "<struct><int a=\"2\">3da</int><boolean>.*</boolean></struct>";
        String actual = "<struct><int a=\"2\">3da</int><boolean>false</boolean></struct>";
        new XmlCompare("", expected, actual, false, true, false).compare();
    }

    @Test(expected = AssertionError.class)
    public void compareXmlChildOrder_negative() throws CompareException {
        String expected = "<struct><list><int>3da</int><int>0da</int></list><boolean>.*</boolean></struct>";
        String actual = "<struct><list><int>0da</int><int>3da</int></list><boolean>false</boolean></struct>";
        new XmlCompare("", expected, actual, false, true, false).compare();
    }

    @Test
    public void compareXmlAttributesInclusion() throws CompareException {
        String expected = "<struct><int a=\"2\">3da</int><boolean>.*</boolean></struct>";
        String actual = "<struct><boolean>false</boolean><int a=\"2\" b=\"3\">3da</int></struct>";
        new XmlCompare("", expected, actual, false, false, false).compare();
    }

    @Test
    public void compareXmlAttributesInclusion_with_regex() throws CompareException {
        String expected = "<struct><int b=\"[0-9]*\">3da</int><boolean>.*</boolean></struct>";
        String actual = "<struct><boolean>false</boolean><int a=\"2\" b=\"3\">3da</int></struct>";
        new XmlCompare("", expected, actual, false, false, false).compare();
    }

    @Test(expected = AssertionError.class)
    public void compareXmlAttributesInclusion_negative() throws CompareException {
        String expected = "<struct><int a=\"[0-9]*\" c=\"2\">3da</int><boolean>.*</boolean></struct>";
        String actual = "<struct><boolean>false</boolean><int a=\"2\" b=\"3\">3da</int></struct>";
        new XmlCompare("", expected, actual, false, false, false).compare();
    }

    @Test
    public void compareXmlAttributesWithLength() throws CompareException {
        String expected = "<struct><int a=\"[0-9]*\" b=\"3\">3da</int><boolean>.*</boolean></struct>";
        String actual = "<struct><boolean>false</boolean><int a=\"2\" b=\"3\">3da</int></struct>";
        new XmlCompare("", expected, actual, false, false, true).compare();
    }

    @Test(expected = AssertionError.class)
    public void compareXmlAttributesWithLength_negative() throws CompareException {
        String expected = "<struct><int a=\"[0-9]*\">3da</int><boolean>.*</boolean></struct>";
        String actual = "<struct><boolean>false</boolean><int a=\"2\" b=\"3\">3da</int></struct>";
        new XmlCompare("", expected, actual, false, false, true).compare();
    }
}
