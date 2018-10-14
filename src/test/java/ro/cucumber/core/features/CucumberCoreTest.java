package ro.cucumber.core.features;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features",
        glue = {"ro.cucumber.core.context.config", "ro.cucumber.core.basicstepdefs",
                "ro.cucumber.core.features.stepdefs"},
        plugin = {"pretty", "html:target/cucumber-html-report",
                "json:target/cucumber-report/report.json"}, tags = "not @Ignore")
public class CucumberCoreTest {
}


