package com.cucumber.utils.engineering.utils;

import com.cucumber.utils.helper.StringFormat;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringFormatTest {

    @Test
    public void testPropertiesReplace() {
        String source = "{\"a\":\"some #[var 1]\",\"b\":#[var2]}";
        Map<String, Object> props = new HashMap<>();
        props.put("var 1", "value here");
        props.put("var2", "\"test\"");
        String expected = "{\"a\":\"some value here\",\"b\":\"test\"}";
        assertEquals(expected, StringFormat.replaceProps(source, props));
    }

    @Test
    public void testPropertiesReplaceWithNulls() {
        String source = "{\"a\":\"some #[var 1]\",\"b\":#[var2]}";
        Map<String, Object> props = new HashMap<>();
        props.put("var 1", null);
        props.put("var2", true);
        String expected = "{\"a\":\"some #[var 1]\",\"b\":true}";
        assertEquals(expected, StringFormat.replaceProps(source, props));
    }

    @Test
    public void testStringToColumnsFormatter() {
        assertEquals("a               b               \n", StringFormat.toColumns(10, "a", "b"));
    }

    @Test
    public void testStringToColumnsFormatterWithEmptyColumns() {
        assertEquals("", StringFormat.toColumns(10, ""));
    }

    @Test
    public void testStringToColumnsFormatterWithNullColumns() {
        assertNull(StringFormat.toColumns(10, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStringToColumnsFormatterWithInvalidColumnWidth() {
        StringFormat.toColumns(0, "a", "b");
    }

    @Test
    public void testStringToColumnsFormatterWithEmptyValues() {
        assertEquals("         b                 \n", StringFormat.toColumns(3, "", "b", ""));
    }

    @Test
    public void testStringToColumnsFormatterWithNulls() {
        assertEquals("a               null            b               \n", StringFormat.toColumns(10, "a", null, "b"));
    }

    @Test
    public void testStringToColumnsFormatterWithLines() {
        assertEquals("abc      null      abc      abc      a        \n" +
                "d                 def      def               \n" +
                "abc               g        tes               \n" +
                "de                ab       t                 \n", StringFormat.toColumns(3, "abcd\nabcde", null, "abcdefg\nab", "abcdef\ntest", "a\n\n\n\n\n\n\n\n"));
    }

    @Test
    public void testStringToColumnsFormatterWithBigStrings() {
        System.out.println(StringFormat.toColumns(80, "{\n" +
                "  \"name\": \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "  \"type\": \"NATIVE\",\n" +
                "  \"records\": [\n" +
                "    {\n" +
                "      \"name\": \"www2.test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "      \"type\": \"CNAME\",\n" +
                "      \"content\": \"adsredir.1and1.info\",\n" +
                "      \"ttl\": 3600\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "      \"type\": \"MX\",\n" +
                "      \"content\": \"mx00.1and1.com\",\n" +
                "      \"ttl\": 600,\n" +
                "      \"prio\": 2\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"www.test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "      \"type\": \"A\",\n" +
                "      \"content\": \"1.2.3.4\",\n" +
                "      \"ttl\": 3600\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"www.test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "      \"type\": \"AAAA\",\n" +
                "      \"content\": \"2001:db8:0:0:0:0:0:1\",\n" +
                "      \"ttl\": 3600\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "      \"type\": \"MX\",\n" +
                "      \"content\": \"mx01.1and1.com\",\n" +
                "      \"ttl\": 3600,\n" +
                "      \"prio\": 11\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "      \"type\": \"NS\",\n" +
                "      \"content\": \"ns1240.ui-dns.com\",\n" +
                "      \"ttl\": 3600\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "      \"type\": \"NS\",\n" +
                "      \"content\": \"ns1240.ui-dns.de\",\n" +
                "      \"ttl\": 3600\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "      \"type\": \"NS\",\n" +
                "      \"content\": \"ns1240.ui-dns.org\",\n" +
                "      \"ttl\": 3600\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "      \"type\": \"NS\",\n" +
                "      \"content\": \"ns1240.ui-dns.biz\",\n" +
                "      \"ttl\": 3600\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "      \"type\": \"SOA\",\n" +
                "      \"content\": \"ns1240.ui-dns.com hostmaster.1and1.com 2017080900 28800 7200 604800 600\",\n" +
                "      \"ttl\": 86400\n" +
                "    }\n" +
                "  ]\n" +
                "}", "{\n" +
                "  \"name\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "  \"type\" : \"NATIVE\",\n" +
                "  \"records\" : [ {\n" +
                "    \"status\" : \"created\",\n" +
                "    \"recordId\" : 49921816,\n" +
                "    \"name\" : \"www2.test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"rootName\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"type\" : \"CNAME\",\n" +
                "    \"content\" : \"adsredir.1and1.info\",\n" +
                "    \"changeDate\" : \"2020-01-07T13:53:32.296Z\",\n" +
                "    \"ttl\" : 3600,\n" +
                "    \"disabled\" : false\n" +
                "  }, {\n" +
                "    \"status\" : \"created\",\n" +
                "    \"recordId\" : 49921817,\n" +
                "    \"name\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"rootName\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"type\" : \"MX\",\n" +
                "    \"content\" : \"mx00.1and1.com\",\n" +
                "    \"changeDate\" : \"2020-01-07T13:53:32.298Z\",\n" +
                "    \"ttl\" : 600,\n" +
                "    \"prio\" : 2,\n" +
                "    \"disabled\" : false\n" +
                "  }, {\n" +
                "    \"status\" : \"created\",\n" +
                "    \"recordId\" : 49921818,\n" +
                "    \"name\" : \"www.test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"rootName\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"type\" : \"A\",\n" +
                "    \"content\" : \"1.2.3.4\",\n" +
                "    \"changeDate\" : \"2020-01-07T13:53:32.300Z\",\n" +
                "    \"ttl\" : 3600,\n" +
                "    \"disabled\" : false\n" +
                "  }, {\n" +
                "    \"status\" : \"created\",\n" +
                "    \"recordId\" : 49921819,\n" +
                "    \"name\" : \"www.test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"rootName\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"type\" : \"AAAA\",\n" +
                "    \"content\" : \"2001:db8:0:0:0:0:0:1\",\n" +
                "    \"changeDate\" : \"2020-01-07T13:53:32.302Z\",\n" +
                "    \"ttl\" : 3600,\n" +
                "    \"disabled\" : false\n" +
                "  }, {\n" +
                "    \"status\" : \"created\",\n" +
                "    \"recordId\" : 49921820,\n" +
                "    \"name\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"rootName\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"type\" : \"MX\",\n" +
                "    \"content\" : \"mx01.1and1.com\",\n" +
                "    \"changeDate\" : \"2020-01-07T13:53:32.304Z\",\n" +
                "    \"ttl\" : 3600,\n" +
                "    \"prio\" : 11,\n" +
                "    \"disabled\" : false\n" +
                "  }, {\n" +
                "    \"status\" : \"created\",\n" +
                "    \"recordId\" : 49921821,\n" +
                "    \"name\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"rootName\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"type\" : \"NS\",\n" +
                "    \"content\" : \"ns1240.ui-dns.com\",\n" +
                "    \"changeDate\" : \"2020-01-07T13:53:32.305Z\",\n" +
                "    \"ttl\" : 3600,\n" +
                "    \"disabled\" : false\n" +
                "  }, {\n" +
                "    \"status\" : \"created\",\n" +
                "    \"recordId\" : 49921822,\n" +
                "    \"name\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"rootName\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"type\" : \"NS\",\n" +
                "    \"content\" : \"ns1240.ui-dns.de\",\n" +
                "    \"changeDate\" : \"2020-01-07T13:53:32.307Z\",\n" +
                "    \"ttl\" : 3600,\n" +
                "    \"disabled\" : false\n" +
                "  }, {\n" +
                "    \"status\" : \"created\",\n" +
                "    \"recordId\" : 49921823,\n" +
                "    \"name\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"rootName\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"type\" : \"NS\",\n" +
                "    \"content\" : \"ns1240.ui-dns.org\",\n" +
                "    \"changeDate\" : \"2020-01-07T13:53:32.308Z\",\n" +
                "    \"ttl\" : 3600,\n" +
                "    \"disabled\" : false\n" +
                "  }, {\n" +
                "    \"status\" : \"created\",\n" +
                "    \"recordId\" : 49921824,\n" +
                "    \"name\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"rootName\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"type\" : \"NS\",\n" +
                "    \"content\" : \"ns1240.ui-dns.biz\",\n" +
                "    \"changeDate\" : \"2020-01-07T13:53:32.310Z\",\n" +
                "    \"ttl\" : 3600,\n" +
                "    \"disabled\" : false\n" +
                "  }, {\n" +
                "    \"status\" : \"created\",\n" +
                "    \"recordId\" : 49921825,\n" +
                "    \"name\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"rootName\" : \"test-apply-fb68280b-26ea-4b3d-8d88-0cb360de8b60.it\",\n" +
                "    \"type\" : \"SOA\",\n" +
                "    \"content\" : \"ns1240.ui-dns.com hostmaster.1and1.com 2017080900 28800 7200 604800 600\",\n" +
                "    \"changeDate\" : \"2020-01-07T13:53:32.312Z\",\n" +
                "    \"ttl\" : 86400,\n" +
                "    \"disabled\" : false\n" +
                "  } ]\n" +
                "}"));
    }
}
