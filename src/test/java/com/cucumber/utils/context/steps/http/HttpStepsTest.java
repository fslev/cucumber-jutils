package com.cucumber.utils.context.steps.http;

import com.cucumber.utils.context.ScenarioUtils;
import com.cucumber.utils.context.vars.ScenarioVars;
import io.jtest.utils.clients.http.HttpClient;
import io.jtest.utils.clients.http.Method;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.IOException;
import java.util.Map;

import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpClient.Builder.class)
@PowerMockIgnore({"javax.net.ssl.*", "javax.management.*", "com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*",
        "org.w3c.dom.*", "com.sun.org.apache.xalan.*"})
public class HttpStepsTest {
    @Mock
    private ScenarioUtils scenarioUtils;
    @Spy
    private final ScenarioVars scenarioVars = new ScenarioVars();

    private final HttpSteps httpSteps = new HttpSteps();

    @Before
    public void mock() throws Exception {
        HttpClient client = PowerMockito.mock(HttpClient.class);
        CloseableHttpResponse mockResponse = PowerMockito.mock(CloseableHttpResponse.class);

        when(mockResponse.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "FINE!"));
        when(mockResponse.getEntity()).thenReturn(new StringEntity("{\"firstName\":\"Lara\",\"lastName\":\"Ol\"}"));
        when(mockResponse.getAllHeaders()).thenReturn(new Header[]{});

        whenNew(HttpClient.class).withAnyArguments().thenReturn(client);

        doReturn(mockResponse).when(client).execute();

        Whitebox.setInternalState(httpSteps, scenarioVars);
        Whitebox.setInternalState(httpSteps, scenarioUtils);
    }

    @Test
    public void testExecuteAndCompare() throws IOException {
        httpSteps.executeAndMatch("http://test.ro", Method.GET, Map.of("1", "1"), Map.of("1", "1"), null,
                "{\"status\":200,\"body\":{\"firstName\":\"Lar.*\"}}");
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteAndCompare_exception() throws IOException {
        httpSteps.executeAndMatch("http://test.ro", Method.GET, Map.of("1", "1"), Map.of("1", "1"), null,
                "{\"status\":wrong}");
    }
}
