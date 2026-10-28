package com.cucumber.utils.features.stepdefs.readme;

import com.cucumber.utils.context.ScenarioUtils;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Readme.md, 8. Writing your own steps
@ScenarioScoped
public class OrderSteps {

    @Inject
    private ScenarioUtils scenarioUtils;

    private Map<String, Object> lastOrder;

    @When("I order {}")
    public void order(Map<String, Object> order) {
        scenarioUtils.log("Order: {}", order);  // written to the scenario report
        lastOrder = order;
    }

    @Then("README check: the last order has a UUID id and quantity 6")
    public void checkLastOrder() {
        assertDoesNotThrow(() -> UUID.fromString(lastOrder.get("id").toString()));
        assertEquals(6, lastOrder.get("quantity"));
    }
}
