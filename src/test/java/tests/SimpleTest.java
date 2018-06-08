package tests;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = {"stepdefs"}, format = {
        "pretty", "html:target/cucumber", "json:target/cucumber-report/cucumber.json"})
public class SimpleTest {
}
