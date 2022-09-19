package com.cucumber.utils.context;

import com.cucumber.utils.context.vars.ScenarioVars;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScenarioVarsUtilsTest {

    private ScenarioVars scenarioVars;

    @Before
    public void init() {
        this.scenarioVars = new ScenarioVars();
    }

    @Test
    public void testParserFromInvalidFile() {
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.parse("idontexist.json", scenarioVars));
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadScenarioVarsFromFile("idontexist.properties", scenarioVars));
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadScenarioVarsFromFile("idontexist.yaml", scenarioVars));
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadScenarioVarsFromDir("idontexist", scenarioVars));
    }

    @Test
    public void testLoadScenarioVarsYamlFile() {
        ScenarioVarsUtils.loadScenarioVarsFromFile("placeholders/scenario.yaml", scenarioVars);
        assertEquals("David #[lastName]", scenarioVars.get("name"));
        assertTrue(assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadScenarioVarsFromFile("placeholders/empty.yaml", scenarioVars))
                .getMessage().contains("Incorrect data inside Yaml file: placeholders/empty.yaml"));
    }

    @Test
    public void testLoadScenarioVarsFromDirWithEmptyYamlFile() {
        assertTrue(assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadScenarioVarsFromDir("props1/dir1", scenarioVars))
                .getMessage().contains("Incorrect data inside Yaml file: props1/dir1/empty.yml"));
    }

    @Test
    public void loadPropertyFileFromInvalidExtension() {
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadFileAsScenarioVariable(
                "props/invalid.extension", scenarioVars, "var1"));
    }
}
