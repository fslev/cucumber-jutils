package com.cucumber.utils.features;

import io.cucumber.junit.Cucumber;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.junit.runner.RunWith;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

@RunWith(Cucumber.class)
@io.cucumber.junit.CucumberOptions(features = "src/test/resources/features",
        glue = {"com.cucumber.utils"}, plugin = {"pretty", "junit:target/junit/cucumber-reports.xml",
        "json:target/cucumber-report/report.json"}, tags = {"not @Ignore", "not @ignore"})
@CucumberOptions(features = "src/test/resources/features",
        glue = {"com.cucumber.utils"}, plugin = {"pretty", "junit:target/junit/cucumber-reports.xml",
        "json:target/cucumber-report/report.json"}, tags = {"not @Ignore", "not @ignore"})
public class CucumberUtilsTest extends AbstractTestNGCucumberTests implements ITest {

    private ThreadLocal<String> testName = new ThreadLocal<>();

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }


    @BeforeMethod
    public void BeforeMethod(Method method, Object[] testData, ITestContext ctx) {
        if (testData.length > 0) {
            testName.set(testData[0].toString());
            ctx.setAttribute("testName", testName.get());
        } else
            ctx.setAttribute("testName", method.getName());
    }

    @Override
    public String getTestName() {
        return testName.get();
    }
}


