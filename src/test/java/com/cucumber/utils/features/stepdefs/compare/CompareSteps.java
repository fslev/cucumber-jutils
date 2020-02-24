package com.cucumber.utils.features.stepdefs.compare;

import com.cucumber.utils.clients.http.wrappers.HttpResponseWrapper;
import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.utils.Cucumbers;
import com.cucumber.utils.context.utils.ScenarioUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.BasicHttpContext;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

@ScenarioScoped
public class CompareSteps {


    @Inject
    private ScenarioProps scenarioProps;
    @Inject
    private Cucumbers cucumbers;
    @Inject
    private ScenarioUtils scenarioUtils;

    @Given("Negative compare {} against {} via jsonNonExtensibleObject={}, jsonNonExtensibleArray={}, jsonArrayStrictOrder={}, xmlChildListLength={}, xmlChildListSequence={}, xmlElementNumAttributes={} and message={}")
    public void compareNegative(String expectedJson, String actualJson, boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder, boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes, String message) {
        try {
            compare(expectedJson, actualJson, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder, xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes, message);
            throw new AssertionError("Are equal");
        } catch (AssertionError e) {
            scenarioUtils.log("PASSED: Not equal\n{}", e);
        }
    }

    @Given("Compare {} against {} via jsonNonExtensibleObject={}, jsonNonExtensibleArray={}, jsonArrayStrictOrder={}, xmlChildListLength={}, xmlChildListSequence={}, xmlElementNumAttributes={} and message={}")
    public void compare(String expectedJson, String actualJson, boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder, boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes, String message) {
        scenarioUtils.log("Compare\n{}\nwith\n{}", expectedJson, actualJson);
        cucumbers.compare(message, expectedJson, actualJson, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder, xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes);
    }

    @Given("Http Response Compare {} against {} with {} body, via jsonNonExtensibleObject={}, jsonNonExtensibleArray={}, jsonArrayStrictOrder={}, xmlChildListLength={}, xmlChildListSequence={}, xmlElementNumAttributes={} and message={}")
    public void compareHttpResponse(String expectedJson, HttpResponseWrapper actual, String bodyType, boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder, boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes, String message) throws UnsupportedEncodingException, JsonProcessingException {
        scenarioUtils.log("Compare\n{}\nwith\n{}", expectedJson, actual);
        HttpResponse mock = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, Integer.parseInt(actual.getStatus()), actual.getReasonPhrase()),
                        HttpClientContext.adapt(new BasicHttpContext()));
        StringEntity entity = new StringEntity(bodyType.equals("json") ? new ObjectMapper().convertValue(actual.getEntity(), JsonNode.class).toString() :
                actual.getEntity().toString());
        mock.setEntity(entity);
        actual.getHeaders().forEach((k, v) -> mock.setHeader(new BasicHeader(k, v)));
        cucumbers.compareHttpResponse(message, expectedJson, mock, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder, xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes);
    }

    @Given("[Negative Test] Http Response compare {} against {} with {} body, via jsonNonExtensibleObject={}, jsonNonExtensibleArray={}, jsonArrayStrictOrder={}, xmlChildListLength={}, xmlChildListSequence={}, xmlElementNumAttributes={} and message={}")
    public void compareHttpResponseNegative(String expectedJson, HttpResponseWrapper actual, String bodyType, boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder, boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes, String message) throws UnsupportedEncodingException, JsonProcessingException {
        try {
            compareHttpResponse(expectedJson, actual, bodyType, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder, xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes, message);
            throw new AssertionError("Are equal");
        } catch (AssertionError e) {
            scenarioUtils.log("PASSED: Not equal\n{}", e);
        }
    }

    @Given("[Negative Test] Poll Http Response and Compare {} against {} with {} body, via jsonNonExtensibleObject={}, jsonNonExtensibleArray={}, jsonArrayStrictOrder={}, xmlChildListLength={}, xmlChildListSequence={}, xmlElementNumAttributes={} and message={}")
    public void pollAndCompareHttpResponseNegative(String expectedJson, HttpResponseWrapper actual, String bodyType, boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder, boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes, String message) throws UnsupportedEncodingException, JsonProcessingException {
        try {
            pollAndCompareHttpResponse(expectedJson, actual, bodyType, jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder, xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes, message);
        } catch (AssertionError e) {
            scenarioUtils.log("PASSED: Not equal\n{}", e);
        }
    }

    @Given("Poll Http Response and Compare {} against {} with {} body, via jsonNonExtensibleObject={}, jsonNonExtensibleArray={}, jsonArrayStrictOrder={}, xmlChildListLength={}, xmlChildListSequence={}, xmlElementNumAttributes={} and message={}")
    public void pollAndCompareHttpResponse(String expectedJson, HttpResponseWrapper actual, String bodyType, boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder, boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes, String message) throws UnsupportedEncodingException, JsonProcessingException {
        scenarioUtils.log("Poll and Compare\n{}\nwith\n{}", expectedJson, actual);
        final HttpResponse mock = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, Integer.parseInt(actual.getStatus()), actual.getReasonPhrase()),
                        HttpClientContext.adapt(new BasicHttpContext()));
        StringEntity entity = new StringEntity(bodyType.equals("json") ? new ObjectMapper().convertValue(actual.getEntity(), JsonNode.class).toString() :
                actual.getEntity().toString());
        mock.setEntity(entity);
        actual.getHeaders().forEach((k, v) -> mock.setHeader(new BasicHeader(k, v)));
        final AtomicInteger a = new AtomicInteger();
        cucumbers.pollAndCompareHttpResponse(message, expectedJson, 5, 100L, 1.5,
                () -> {
                    if (a.getAndIncrement() < 5) {
                        return new DefaultHttpResponseFactory().newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "mock"), new BasicHttpContext());
                    } else {
                        return mock;
                    }
                },
                jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder, xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes);
    }

    @Given("Negative Poll Http Response and Compare {} against {} with {} body, by body={}, status={}, headers={}, reason={}, via jsonNonExtensibleObject={}, jsonNonExtensibleArray={}, jsonArrayStrictOrder={}, xmlChildListLength={}, xmlChildListSequence={}, xmlElementNumAttributes={} and message={}")
    public void negativePollAndCompareHttpResponse(String expectedJson, HttpResponseWrapper actual, String bodyType, boolean jsonNonExtensibleObject,
                                                   boolean body, boolean status, boolean headers, boolean reason,
                                                   boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder, boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes, String message) throws UnsupportedEncodingException, JsonProcessingException {
        scenarioUtils.log("Negative Poll and Compare\n{}\nwith\n{}", expectedJson, actual);
        final HttpResponse mock = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, Integer.parseInt(actual.getStatus()), actual.getReasonPhrase()),
                        HttpClientContext.adapt(new BasicHttpContext()));
        StringEntity entity = new StringEntity(bodyType.equals("json") ? new ObjectMapper().convertValue(actual.getEntity(), JsonNode.class).toString() :
                actual.getEntity().toString());
        mock.setEntity(entity);
        actual.getHeaders().forEach((k, v) -> mock.setHeader(new BasicHeader(k, v)));
        final AtomicInteger a = new AtomicInteger();
        cucumbers.negativePollAndCompareHttpResponse(message, expectedJson, 5, 100L, 1.5,
                () -> {
                    if (a.getAndIncrement() < 5) {
                        mock.setReasonPhrase("test");
                        return mock;
                    } else {
                        mock.setReasonPhrase(actual.getReasonPhrase());
                        return mock;
                    }
                }, body, status, headers, reason,
                jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder, xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes);
    }
}
