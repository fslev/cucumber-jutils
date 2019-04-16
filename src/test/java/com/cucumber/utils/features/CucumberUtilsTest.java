package com.cucumber.utils.features;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.junit.runner.RunWith;
import org.testng.annotations.DataProvider;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features",
        glue = {"com.cucumber.utils"}, plugin = {"pretty", "junit:output",
        "json:target/cucumber-report/report.json"}, tags = {"not @Ignore", "not @ignore"})
public class CucumberUtilsTest extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}


