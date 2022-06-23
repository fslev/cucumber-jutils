package com.cucumber.utils.context;

import com.cucumber.utils.context.vars.ScenarioVars;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class ScenarioVarsUtilsTest {

    private ScenarioVars scenarioVars;

    @Before
    public void init() {
        this.scenarioVars = new ScenarioVars();
    }

    @Test
    public void testParserFromInvalidFile() {
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.parse("idontexist.json", scenarioVars));
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadScenarioVarsFromFile("idontexist.json", scenarioVars));
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadScenarioVarsFromDir("idontexist.json", scenarioVars));
    }

    @Test
    public void loadPropertyFileFromInvalidExtension() {
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadFileAsScenarioVariable(
                "props/invalid.extension", scenarioVars, "var1"));
    }
}
