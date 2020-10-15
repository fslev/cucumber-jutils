package com.cucumber.utils.features.stepdefs.compare;

import com.cucumber.utils.context.Cucumbers;
import com.cucumber.utils.context.ScenarioUtils;
import com.cucumber.utils.context.props.ScenarioProps;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.jtest.utils.clients.http.wrappers.HttpResponseWrapper;
import io.jtest.utils.common.XmlUtils;
import io.jtest.utils.matcher.condition.MatchCondition;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.BasicHttpContext;

import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@ScenarioScoped
public class HttpCompareSteps {

    @Inject
    private ScenarioProps scenarioProps;
    @Inject
    private Cucumbers cucumbers;
    @Inject
    private ScenarioUtils scenarioUtils;

    @Given("Http Response Compare {} against {} with matchConditions={} and message={}")
    public void compareHttpResponse(String expectedJson, HttpResponseWrapper actual, Set<MatchCondition> matchConditions, String message) throws UnsupportedEncodingException {
        scenarioUtils.log("Compare\n{}\nwith\n{}", expectedJson, actual);
        HttpResponse mock = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, Integer.parseInt(actual.getStatus()), actual.getReasonPhrase()),
                        HttpClientContext.adapt(new BasicHttpContext()));
        mock.setReasonPhrase(actual.getReasonPhrase());
        StringEntity entity;
        if (XmlUtils.isValid(actual.getEntity().toString())) {
            entity = new StringEntity(actual.getEntity().toString());
        } else {
            try {
                entity = new StringEntity(new ObjectMapper().convertValue(actual.getEntity(), JsonNode.class).toString());
            } catch (Exception e) {
                entity = new StringEntity(actual.getEntity().toString());
            }
        }
        mock.setEntity(entity);
        actual.getHeaders().forEach((k, v) -> mock.setHeader(new BasicHeader(k, v)));
        cucumbers.compareHttpResponse(message, expectedJson, mock, matchConditions.toArray(new MatchCondition[0]));
    }

    @Given("Poll Http Response and Compare {} against {} with matchConditions={} and message={}")
    public void pollAndCompareHttpResponse(String expectedJson, HttpResponseWrapper actual, Set<MatchCondition> matchConditions, String message) throws UnsupportedEncodingException {
        scenarioUtils.log("Poll and Compare\n{}\nwith\n{}", expectedJson, actual);
        final HttpResponse mock = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, Integer.parseInt(actual.getStatus()), actual.getReasonPhrase()),
                        HttpClientContext.adapt(new BasicHttpContext()));
        StringEntity entity;
        if (XmlUtils.isValid(actual.getEntity().toString())) {
            entity = new StringEntity(actual.getEntity().toString());
        } else {
            try {
                entity = new StringEntity(new ObjectMapper().convertValue(actual.getEntity(), JsonNode.class).toString());
            } catch (Exception e) {
                entity = new StringEntity(actual.getEntity().toString());
            }
        }
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
                matchConditions.toArray(new MatchCondition[0]));
    }
}
