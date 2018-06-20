package tests;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/simple.feature", glue = {"stepdefs"},
        dryRun = false,plugin = {"pretty","html:target/cucumber-html-report"})
public class SimpleTest {
}
