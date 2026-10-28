package com.cucumber.utils.features.stepdefs.readme;

import com.cucumber.utils.context.ScenarioVarsUtils;
import com.cucumber.utils.context.vars.ScenarioVars;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Readme.md, 4.3 From Java
@ScenarioScoped
public class StorySteps {

    @Inject
    private ScenarioVars scenarioVars;

    @Given("the story is told")
    public void tellStory() {
        scenarioVars.put("animal", "wolf");                      // #[animal] in later steps
        String location = scenarioVars.getAsString("location");  // set earlier, in Gherkin or Java
        // some_text.txt: "The #[animal] lives in #[location]"
        String story = ScenarioVarsUtils.parse("features/readme/scene/some_text.txt", scenarioVars);
        assertEquals("The wolf lives in " + location, story);
    }
}
