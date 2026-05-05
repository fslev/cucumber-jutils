package com.cucumber.utils.context;

import com.cucumber.utils.context.vars.ScenarioVars;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScenarioVarsUtilsTest {

    private ScenarioVars scenarioVars;

    @BeforeEach
    void init() {
        this.scenarioVars = new ScenarioVars();
    }

    @Test
    void testParserFromInvalidFile() {
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.parse("idontexist.json", scenarioVars));
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadScenarioVarsFromFile("idontexist.properties", scenarioVars));
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadScenarioVarsFromFile("idontexist.yaml", scenarioVars));
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadScenarioVarsFromDir("idontexist", scenarioVars));
    }

    @Test
    void testLoadScenarioVarsYamlFile() {
        ScenarioVarsUtils.loadScenarioVarsFromFile("placeholders/scenario.yaml", scenarioVars);
        assertEquals("David #[lastName]", scenarioVars.get("name"));
        assertTrue(assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadScenarioVarsFromFile("placeholders/empty.yaml", scenarioVars))
                .getMessage().contains("Incorrect data inside Yaml file: placeholders/empty.yaml"));
    }

    @Test
    void testLoadScenarioVarsFromDirWithEmptyYamlFile() {
        assertTrue(assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadScenarioVarsFromDir("props1/dir1", scenarioVars))
                .getMessage().contains("Incorrect data inside Yaml file: props1/dir1/empty.yml"));
    }

    @Test
    void loadPropertyFileFromInvalidExtension() {
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadFileAsScenarioVariable(
                "props/invalid.extension", scenarioVars, "var1"));
    }
}
