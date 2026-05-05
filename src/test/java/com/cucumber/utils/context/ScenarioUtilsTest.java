package com.cucumber.utils.context;

import io.cucumber.java.Scenario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ScenarioUtilsTest {

    @Mock
    private Scenario scenario;
    @Captor
    private ArgumentCaptor<String> stringArgCaptor;

    private final ScenarioUtils scenarioUtils = new ScenarioUtils();

    @BeforeEach
    void mockScenario() {
        scenarioUtils.init(scenario);
    }

    @Test
    void testLogging() {
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
