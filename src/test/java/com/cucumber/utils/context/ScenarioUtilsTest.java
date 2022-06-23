package com.cucumber.utils.context;

import io.cucumber.java.Scenario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.management.*", "com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.dom.*", "com.sun.org.apache.xalan.*"})
public class ScenarioUtilsTest {

    @Mock
    private Scenario scenario;
    @Captor
    private ArgumentCaptor<String> stringArgCaptor;

    private final ScenarioUtils scenarioUtils = new ScenarioUtils();

    @Before
    public void mockScenario() {
        Whitebox.setInternalState(scenarioUtils, scenario);
    }

    @Test
    public void testLogging() {
        assertNotNull(scenarioUtils.getScenario());

        scenarioUtils.log(null);
        verify(scenario, atLeastOnce()).log(stringArgCaptor.capture());
        assertNull(stringArgCaptor.getValue());

        scenarioUtils.log("test");
        verify(scenario, atLeastOnce()).log(stringArgCaptor.capture());
        assertEquals("test", stringArgCaptor.getValue());

        scenarioUtils.log("Nice {} text", "lorem \n/ipsum");
        verify(scenario, atLeastOnce()).log(stringArgCaptor.capture());
        assertEquals("Nice lorem \n/ipsum text", stringArgCaptor.getValue());

        scenarioUtils.log("Nice {} array", Arrays.asList("lorem", "ipsum"));
        verify(scenario, atLeastOnce()).log(stringArgCaptor.capture());
        assertEquals("Nice [lorem, ipsum] array", stringArgCaptor.getValue());

        scenarioUtils.log(Arrays.asList("lorem", "ipsum"));
        verify(scenario, atLeastOnce()).log(stringArgCaptor.capture());
        assertEquals("[lorem, ipsum]", stringArgCaptor.getValue());

        scenarioUtils.log(new RuntimeException("Something went wrong"));
        verify(scenario, atLeastOnce()).log(stringArgCaptor.capture());
        assertTrue(stringArgCaptor.getValue().contains("Something went wrong"));

        scenarioUtils.log("Throw: {}", new RuntimeException("Something went wrong"));
        verify(scenario, atLeastOnce()).log(stringArgCaptor.capture());
        assertTrue(stringArgCaptor.getValue().startsWith("Throw: "));
    }
}
