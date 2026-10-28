package com.cucumber.utils.features.stepdefs.readme;

import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

// Checks the claims in Readme.md, 8. Writing your own steps
@ScenarioScoped
public class TransformerChecksSteps {

    @Then("README check: string parameter {string} stays raw")
    public void stringParameterStaysRaw(String value) {
        assertEquals("#[animal]", value);
    }

    @Then("README check: doc string declared as String stays raw")
    public void docStringStaysRaw(String docString) {
        assertEquals("#[animal]", docString.trim());
    }

    @Then("README check: {} is null")
    public void isNull(Object value) {
        assertNull(value);
    }

    @Then("README check: {} stays the text [_blank]")
    public void staysBlankText(Object value) {
        assertEquals("[_blank]", value);
    }

    @Then("README check: table cells")
    public void tableCells(List<Map<String, Object>> rows) {
        assertEquals("", rows.get(0).get("blank"));
        assertNull(rows.get(0).get("empty"));
    }
}
