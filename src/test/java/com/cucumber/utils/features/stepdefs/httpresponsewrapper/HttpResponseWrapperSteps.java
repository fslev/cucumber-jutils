package com.cucumber.utils.features.stepdefs.httpresponsewrapper;

import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.BasicHttpContext;
import com.cucumber.utils.context.compare.Cucumbers;
import com.cucumber.utils.context.compare.wrappers.HttpResponseWrapper;

import java.io.IOException;

import static org.junit.Assert.fail;

@ScenarioScoped
public class HttpResponseWrapperSteps {


    @Then("Compare two random HTTP response wrappers")
    public void compareHttpResponseWrappers() throws IOException {
        HttpResponse mock = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "some reason"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        mock.setEntity(new StringEntity("{\"a\":100}"));
        mock.setHeader(new BasicHeader("Content-Type", "application/json"));
        mock.setHeader(new BasicHeader("Accept", "application/json"));
        HttpResponseWrapper actualWrapper = new HttpResponseWrapper(mock);
        Cucumbers.compare("{\"status\":200,\"body\":{\"a\":100}}", actualWrapper);
    }

    @Then("Compare two random HTTP response wrappers negative")
    public void simulateHttpResponseCompare_negative() throws IOException {
        HttpResponse mock = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "some reason"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        mock.setEntity(new StringEntity("{\"a\":100}"));
        mock.setHeader(new BasicHeader("Content-Type", "application/json"));
        mock.setHeader(new BasicHeader("Accept", "application/json"));
        HttpResponseWrapper actualWrapper = new HttpResponseWrapper(mock);
        try {
            Cucumbers.compare("{\"a\":100}", actualWrapper);
        } catch (AssertionError e) {
            return;
        }
        fail("Comparison should have failed. Instead it passed.");
    }

}
