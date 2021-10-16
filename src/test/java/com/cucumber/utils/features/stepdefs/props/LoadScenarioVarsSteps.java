package com.cucumber.utils.features.stepdefs.props;

import com.cucumber.utils.context.ScenarioVarsUtils;
import com.cucumber.utils.context.vars.ScenarioVars;
import com.cucumber.utils.exceptions.InvalidScenarioVarFileType;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;

import static org.junit.Assert.assertThrows;

@ScenarioScoped
public class LoadScenarioVarsSteps {

    @Inject
    private ScenarioVars scenarioVars;

    @Given("Load duplicated scenario vars from dir {} and expect exception")
    public void loadDuplicatedScenarioVars(String dir) {
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadScenarioVarsFromDir(dir, scenarioVars));
    }

    @Given("Load scenario variable from invalid file type={}")
    public void loadScenarioVarertyFromInvalidFile(String filePath) {
        assertThrows(InvalidScenarioVarFileType.class, () -> ScenarioVarsUtils.loadScenarioVarsFromFile(filePath, scenarioVars));
    }

    @Given("Load scenario variable from unknown location={}")
    public void loadScenarioVarertyFromUnknownLocation(String filePath) {
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadScenarioVarsFromFile(filePath, scenarioVars));
    }

    @Given("Load scenario variables from unknown location={}")
    public void loadScenarioVarertiesFromUnknownLocation(String dirPath) {
        assertThrows(RuntimeException.class, () -> ScenarioVarsUtils.loadScenarioVarsFromDir(dirPath, scenarioVars));
    }
}
