package com.cucumber.utils.engineering.match;

import com.cucumber.utils.engineering.match.condition.MatchCondition;
import com.cucumber.utils.exceptions.InvalidTypeException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.BasicHttpContext;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HttpResponseMatcherTests {


    @Test(expected = InvalidTypeException.class)
    public void convertHttpResponse_negative() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"statuses\": 200, \"headers\":{\"auth\":1,\"x-auth\":2}, \"reason\":\"test\",\"body\":{\"a\":\"~[val1] ipsum\"}}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        new HttpResponseMatcher(null, expected, actual, null).match();
    }

    @Test
    public void matchSimpleHttpResponses() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"status\": 200, \"headers\":{\"auth\":1,\"x-auth\":2}, \"reason\":\"test\",\"body\":{\"a\":\"~[val1] ipsum\"}}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("{\"a\":\"lorem ipsum\"}"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        Map<String, Object> props = new HttpResponseMatcher(null, expected, actual, null).match();
        assertEquals(1, props.size());
        assertEquals("lorem", props.get("val1"));
    }

    @Test(expected = AssertionError.class)
    public void matchSimpleHttpResponses_negative() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"status\": 200, \"headers\":{\"auth\":1,\"x-auth\":2}, \"reason\":\"test\",\"body\":{\"a\":\"~[val1] ipsum\"}}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("{\"a\":\"lorem ipsum\"}"));
        actual.addHeader("auth", "2");
        new HttpResponseMatcher(null, expected, actual, null).match();
    }

    @Test
    public void doNotMatchHttpResponsesByStatus() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"status\": 201, \"headers\":{\"auth\":1,\"x-auth\":2}, \"reason\":\"test\",\"body\":{\"a\":\"~[val1] ipsum\"}}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("{\"a\":\"lorem ipsum\"}"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        Map<String, Object> props = new HttpResponseMatcher(null, expected, actual,
                new HashSet<>(Arrays.asList(MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_STATUS))).match();
        assertEquals(1, props.size());
        assertEquals("lorem", props.get("val1"));
    }

    @Test(expected = AssertionError.class)
    public void doNotMatchHttpResponsesByStatus_negative() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"status\": 200, \"headers\":{\"auth\":1,\"x-auth\":2}, \"reason\":\"test\",\"body\":{\"a\":\"~[val1] ipsum\"}}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("{\"a\":\"lorem ipsum\"}"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        new HttpResponseMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_STATUS))).match();
    }

    @Test
    public void doNotMatchHttpResponsesByBody() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"status\": 200, \"headers\":{\"auth\":1,\"x-auth\":2}, \"reason\":\"test\",\"body\":{\"a\":\"~[val1] ipsum\"}}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("{\"a\":\"lorem other\"}"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        Map<String, Object> props = new HttpResponseMatcher(null, expected, actual,
                new HashSet<>(Arrays.asList(MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY))).match();
        assertEquals(0, props.size());
    }

    @Test(expected = AssertionError.class)
    public void doNotMatchHttpResponsesByBody_negative() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"status\": 200, \"headers\":{\"auth\":1,\"x-auth\":2}, \"reason\":\"test\",\"body\":{\"a\":\"~[val1] ipsum\"}}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("{\"a\":\"lorem ipsum\"}"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        new HttpResponseMatcher(null, expected, actual,
                new HashSet<>(Arrays.asList(MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY))).match();
    }

    @Test(expected = AssertionError.class)
    public void doNotMatchHttpResponsesByBody_negative1() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"status\": 400, \"headers\":{\"auth\":1,\"x-auth\":2}, \"reason\":\"test\",\"body\":{\"a\":\"~[val1] ipsum\"}}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("{\"a\":\"lorem other\"}"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        new HttpResponseMatcher(null, expected, actual,
                new HashSet<>(Arrays.asList(MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY))).match();
    }

    @Test
    public void doNotMatchHttpResponsesByStatusAndBody() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"status\": 400, \"headers\":{\"auth\":1,\"x-auth\":2}, \"reason\":\"test\",\"body\":{\"a\":\"~[val1] ipsum\"}}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("{\"a\":\"lorem other\"}"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        Map<String, Object> props = new HttpResponseMatcher(null, expected, actual,
                new HashSet<>(Arrays.asList(MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY,
                        MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_STATUS))).match();
        assertEquals(0, props.size());
    }

    @Test(expected = AssertionError.class)
    public void doNotMatchHttpResponsesByStatusAndBody_negative() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"status\": 200, \"headers\":{\"auth\":1,\"x-auth\":2}, \"reason\":\"test\",\"body\":{\"a\":\"~[val1] ipsum\"}}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("{\"a\":\"lorem other\"}"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        new HttpResponseMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY,
                MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_STATUS))).match();
    }

    @Test
    public void matchHttpResponsesByJsonNonExtensibleObjectBody() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"status\": 200, \"headers\":{\"auth\":1,\"x-auth\":2}, \"reason\":\"test\",\"body\":{\"a\":\"~[val1] ipsum\"}}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("{\"a\":\"lorem ipsum\"}"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        actual.addHeader("Authorization", "Bzasuiofrz====");
        Map<String, Object> props = new HttpResponseMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_NON_EXTENSIBLE_OBJECT))).match();
        assertEquals(1, props.size());
        assertEquals("lorem", props.get("val1"));
    }


    @Test(expected = AssertionError.class)
    public void matchHttpResponsesByJsonNonExtensibleObjectBody_negative() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"status\": 200, \"headers\":{\"auth\":1,\"x-auth\":2}, \"reason\":\"test\",\"body\":{\"a\":\"~[val1] ipsum\"}}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("{\"a\":\"lorem ipsum\", \"b\":0}"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        actual.addHeader("Authorization", "Bzasuiofrz====");
        Map<String, Object> props = new HttpResponseMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_NON_EXTENSIBLE_OBJECT))).match();
        assertEquals(1, props.size());
        assertEquals("lorem", props.get("val1"));
    }

    @Test
    public void doNotMatchHttpResponsesByJsonNonExtensibleObjectBody() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"status\": 200, \"headers\":{\"auth\":1,\"x-auth\":2}, \"reason\":\"test\",\"body\":{\"a\":\"~[val1] ipsum\"}}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("{\"a\":\"lorem ipsum\", \"b\":0}"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        actual.addHeader("Authorization", "Bzasuiofrz====");
        Map<String, Object> props = new HttpResponseMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_NON_EXTENSIBLE_OBJECT,
                MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY))).match();
        assertEquals(0, props.size());
    }

    @Test(expected = AssertionError.class)
    public void doNotMatchHttpResponsesByJsonNonExtensibleObjectBody_negative() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"status\": 200, \"headers\":{\"auth\":1,\"x-auth\":2}, \"reason\":\"test\",\"body\":{\"a\":\"~[val1] ipsum\"}}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("{\"a\":\"lorem ipsum\"}"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        actual.addHeader("Authorization", "Bzasuiofrz====");
        Map<String, Object> props = new HttpResponseMatcher(null, expected, actual, new HashSet<>(Arrays.asList(MatchCondition.JSON_NON_EXTENSIBLE_OBJECT,
                MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY))).match();
        assertEquals(0, props.size());
    }

    @Test
    public void matchHttpResponsesByStringBody() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"status\": 200, \"headers\":{\"auth\":1,\"x-auth\":2}, \"reason\":\"test\",\"body\": \"~[val1] ipsum\"}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("lorem ipsum"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        actual.addHeader("Authorization", "Bzasuiofrz====");
        Map<String, Object> props = new HttpResponseMatcher(null, expected, actual, null).match();
        assertEquals(1, props.size());
        assertEquals("lorem", props.get("val1"));
    }

    @Test(expected = AssertionError.class)
    public void matchHttpResponsesByStringBody_negative() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"status\": 200, \"headers\":{\"auth\":1,\"x-auth\":2}, \"reason\":\"test\",\"body\": \"~[val1] ipsum\"}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("lorem other"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        actual.addHeader("Authorization", "Bzasuiofrz====");
        new HttpResponseMatcher(null, expected, actual, null).match();
    }

    @Test
    public void matchHttpResponsesByXMLBody() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"headers\":{\"auth\":1,\"x-auth\":2},\"body\": \"<a><a1 attr=\\\"~[attrVal]\\\">~[val]</a1></a>\"}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("<a><a1 attr=\"lorem\" type=\"text\">ipsum</a1><b>null</b></a>"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        actual.addHeader("Authorization", "Bzasuiofrz====");
        Map<String, Object> props = new HttpResponseMatcher(null, expected, actual, null).match();
        assertEquals(2, props.size());
        assertEquals("lorem", props.get("attrVal"));
        assertEquals("ipsum", props.get("val"));
    }

    @Test(expected = AssertionError.class)
    public void matchHttpResponsesByXMLBody_negative() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"headers\":{\"auth\":1,\"x-auth\":2},\"body\": \"<a><a1 attr=\\\"~[attrVal]\\\">~[val]</a1></a>\"}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("<a><a2 attr=\"lorem\" type=\"text\">ipsum</a2><b>null</b></a>"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        actual.addHeader("Authorization", "Bzasuiofrz====");
        new HttpResponseMatcher(null, expected, actual, null).match();
    }

    @Test
    public void doNotMatchHttpResponsesByXMLBody() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"headers\":{\"auth\":1,\"x-auth\":2},\"body\": \"<a><a1 attr=\\\"~[attrVal]\\\">~[val]</a1></a>\"}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("<a><a1 attr=\"lorem\" type=\"text\"><a2>0</a2></a1><b>null</b></a>"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        actual.addHeader("Authorization", "Bzasuiofrz====");
        Map<String, Object> props = new HttpResponseMatcher(null, expected, actual,
                new HashSet<>(Arrays.asList(MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY))).match();
        assertEquals(0, props.size());
    }

    @Test(expected = AssertionError.class)
    public void doNotMatchHttpResponsesByXMLBody_negative() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"headers\":{\"auth\":1,\"x-auth\":2},\"body\": \"<a><a1 attr=\\\"~[attrVal]\\\">~[val]</a1></a>\"}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("<a><a1 attr=\"lorem\" type=\"text\">0</a1><b>null</b></a>"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        actual.addHeader("Authorization", "Bzasuiofrz====");
        new HttpResponseMatcher(null, expected, actual,
                new HashSet<>(Arrays.asList(MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY))).match();
    }

    @Test
    public void matchHttpResponsesByXMLBody_with_ChildNodesLength() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"headers\":{\"auth\":1,\"x-auth\":2},\"body\": \"<a><a1 attr=\\\"~[attrVal]\\\">~[val]</a1><b>null</b></a>\"}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("<a><a1 attr=\"lorem\" type=\"text\">ipsum</a1><b>null</b></a>"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        actual.addHeader("Authorization", "Bzasuiofrz====");
        Map<String, Object> props = new HttpResponseMatcher(null, expected, actual,
                new HashSet<>(Arrays.asList(MatchCondition.XML_CHILD_NODELIST_LENGTH))).match();
        assertEquals(2, props.size());
        assertEquals("lorem", props.get("attrVal"));
        assertEquals("ipsum", props.get("val"));
    }

    @Test(expected = AssertionError.class)
    public void matchHttpResponsesByXMLBody_with_ChildNodesLength_negative() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"headers\":{\"auth\":1,\"x-auth\":2},\"body\": \"<a><a1 attr=\\\"~[attrVal]\\\">~[val]</a1></a>\"}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("<a><a1 attr=\"lorem\" type=\"text\">ipsum</a1><b>null</b></a>"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        actual.addHeader("Authorization", "Bzasuiofrz====");
        new HttpResponseMatcher(null, expected, actual,
                new HashSet<>(Arrays.asList(MatchCondition.XML_CHILD_NODELIST_LENGTH))).match();
    }

    @Test
    public void doNotMatchHttpResponsesByXMLBody_with_ChildNodesLength() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"headers\":{\"auth\":1,\"x-auth\":2},\"body\": \"<a><a1 attr=\\\"~[attrVal]\\\">~[val]</a1></a>\"}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("<a><a1 attr=\"lorem\" type=\"text\">ipsum</a1><b>null</b></a>"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        actual.addHeader("Authorization", "Bzasuiofrz====");
        Map<String, Object> props = new HttpResponseMatcher(null, expected, actual,
                new HashSet<>(Arrays.asList(MatchCondition.XML_CHILD_NODELIST_LENGTH, MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY))).match();
        assertEquals(0, props.size());
    }

    @Test(expected = AssertionError.class)
    public void doNotMatchHttpResponsesByXMLBody_with_ChildNodesLength_negative() throws UnsupportedEncodingException, InvalidTypeException {
        String expected = "{\"headers\":{\"auth\":1,\"x-auth\":2},\"body\": \"<a><a1 attr=\\\"~[attrVal]\\\">~[val]</a1><b>null</b></a>\"}";
        HttpResponse actual = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "test"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        actual.setEntity(new StringEntity("<a><a1 attr=\"lorem\" type=\"text\">ipsum</a1><b>null</b></a>"));
        actual.addHeader("auth", "1");
        actual.addHeader("x-auth", "2");
        actual.addHeader("Authorization", "Bzasuiofrz====");
        new HttpResponseMatcher(null, expected, actual,
                new HashSet<>(Arrays.asList(MatchCondition.XML_CHILD_NODELIST_LENGTH, MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY))).match();
    }
}
