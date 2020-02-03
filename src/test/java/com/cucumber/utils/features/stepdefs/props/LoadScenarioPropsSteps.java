package com.cucumber.utils.features.stepdefs.props;

import com.cucumber.utils.context.utils.Cucumbers;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;

import static org.junit.Assert.assertThrows;

@ScenarioScoped
public class LoadScenarioPropsSteps {

    @Inject
    private Cucumbers cucumbers;

    @Given("Load duplicated scenario props from dir {} and expect exception")
    public void loadDuplicatedScenarioProps(String dir) {
        assertThrows(RuntimeException.class, () -> cucumbers.loadScenarioPropsFromDir(dir));
    }
}
