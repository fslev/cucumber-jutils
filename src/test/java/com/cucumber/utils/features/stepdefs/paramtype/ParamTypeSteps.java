package com.cucumber.utils.features.stepdefs.paramtype;

import com.cucumber.utils.context.vars.ScenarioVars;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ScenarioScoped
public class ParamTypeSteps {

    @Inject
    private ScenarioVars scenarioVars;

    @When("This is a String: {}")
    public void checkThisIsString(Object val) {
        assertEquals(String.class, val.getClass());
    }

    @When("Check string {} equals string {string}")
    public void checkStringEquals(String expected, String actual) {
        assertEquals(expected, actual);
    }

    @When("Array of enums: {}")
    public void arrayOfEnums(MyEnum[] myEnums) {
        assertNotNull(myEnums);
    }

    @When("Array of strings: {}")
    public void arrayOfStrings(String[] myStrings) {
        assertNotNull(myStrings);
    }

    public enum MyEnum {
        TEST,
        OK
    }
}


