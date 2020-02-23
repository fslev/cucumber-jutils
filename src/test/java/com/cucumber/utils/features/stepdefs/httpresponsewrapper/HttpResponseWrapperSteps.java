package com.cucumber.utils.features.stepdefs.httpresponsewrapper;

import com.cucumber.utils.context.utils.Cucumbers;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.BasicHttpContext;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@ScenarioScoped
public class HttpResponseWrapperSteps {

    @Inject
    private Cucumbers cucumbers;


    @Then("Compare two random HTTP response wrappers")
    public void compareHttpResponseWrappers() throws IOException {
        HttpResponse mock = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "some reason"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        mock.setEntity(new StringEntity("{\"a\":100}"));
        mock.setHeader(new BasicHeader("Content-Type", "application/json"));
        mock.setHeader(new BasicHeader("Accept", "application/json"));
        cucumbers.compareHttpResponse(null, "{\"status\":200,\"body\":{\"a\":100}}", mock);
    }

    @Then("Compare two random HTTP response wrappers negative")
    public void simulateHttpResponseCompare_negative() throws IOException {
        HttpResponse mock = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "some reason"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        mock.setEntity(new StringEntity("{\"a\":100}"));
        mock.setHeader(new BasicHeader("Content-Type", "application/json"));
        mock.setHeader(new BasicHeader("Accept", "application/json"));
        try {
            cucumbers.compareHttpResponse(null, "{\"a\":100}", mock);
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Response wrapper of invalid format"));
            return;
        }
        fail("Comparison should have failed. Instead it passed.");
    }

    @When("Create HTTP response wrapper with content {} and compare with {}")
    public void createHttpResponseWrapper(String content, String expected) throws IOException {
        HttpResponse mock = new DefaultHttpResponseFactory()
                .newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "some reason"),
                        HttpClientContext.adapt(new BasicHttpContext()));
        mock.setEntity(new StringEntity(content));
        mock.setHeader(new BasicHeader("Content-Type", "application/xml"));
        cucumbers.compareHttpResponse(null, expected, mock);
    }

}
