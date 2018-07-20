package ro.cucumber.core.features;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "ro.cucumber.core.features",
        glue = {"ro.cucumber.core.context.config", "ro.cucumber.core.basicstepdefs"},
        plugin = {"pretty", "html:target/cucumber-html-report",
                "json:target/cucumber-report/report.json"})
public class SimpleTest {
}


