package com.cucumber.utils.features.stepdefs.props;

import com.cucumber.utils.context.ScenarioPropsUtils;
import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.exceptions.InvalidScenarioPropertyFileType;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;

import static org.junit.Assert.assertThrows;

@ScenarioScoped
public class LoadScenarioPropsSteps {

    @Inject
    private ScenarioProps scenarioProps;

    @Given("Load duplicated scenario props from dir {} and expect exception")
    public void loadDuplicatedScenarioProps(String dir) {
        assertThrows(RuntimeException.class, () -> ScenarioPropsUtils.loadPropsFromDir(dir, scenarioProps));
    }

    @Given("Load scenario property from invalid file type={}")
    public void loadScenarioPropertyFromInvalidFile(String filePath) {
        assertThrows(InvalidScenarioPropertyFileType.class, () -> ScenarioPropsUtils.loadPropsFromFile(filePath, scenarioProps));
    }

    @Given("Load scenario property from unknown location={}")
    public void loadScenarioPropertyFromUnknownLocation(String filePath) {
        assertThrows(RuntimeException.class, () -> ScenarioPropsUtils.loadPropsFromFile(filePath, scenarioProps));
    }

    @Given("Load scenario properties from unknown location={}")
    public void loadScenarioPropertiesFromUnknownLocation(String dirPath) {
        assertThrows(RuntimeException.class, () -> ScenarioPropsUtils.loadPropsFromDir(dirPath, scenarioProps));
    }
}
