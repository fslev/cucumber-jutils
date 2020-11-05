package com.cucumber.utils.features.stepdefs.props;

import com.cucumber.utils.context.ScenarioPropsUtils;
import com.cucumber.utils.context.props.ScenarioProps;
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
}
