package com.cucumber.utils.features.stepdefs.paramtype;

import com.cucumber.utils.context.props.ScenarioProps;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;

@ScenarioScoped
public class ParamTypeSteps {

    @Inject
    private ScenarioProps scenarioProps;

    @When("This is a String: {}")
    public void checkThisIsString(Object val) {
        assertEquals(String.class, val.getClass());
    }
}
