package com.cucumber.utils.features;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features",
        glue = {"com.cucumber.utils"}, plugin = {"pretty", "junit:output",
        "json:target/cucumber-report/report.json"}, tags = {"not @Ignore", "not @ignore"})
public class CucumberUtilsTest {
}


