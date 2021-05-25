package com.cucumber.utils.context.stepdefs;

import com.cucumber.utils.context.ScenarioPropsUtils;
import com.cucumber.utils.context.ScenarioUtils;
import com.cucumber.utils.context.props.ScenarioProps;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.skyah.util.MessageUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

@ScenarioScoped
public class VarSteps {

    private static final Logger LOG = LogManager.getLogger();
    @Inject
    private ScenarioProps scenarioProps;
    @Inject
    private ScenarioUtils scenarioUtils;

    @Given("var {}=\"{}\"")
    public void setVar(String name, Object value) {
        scenarioProps.put(name, value);
        scenarioUtils.log("var {} = {}", name, value != null ? MessageUtil.cropL(value.toString()) : null);
        LOG.debug("var {} = {}", name, value);
    }

    @Given("var {}=")
    public void setVarFromDocString(String name, StringBuilder value) {
        setVar(name, value.toString());
    }

    @Given("var {} from file path \"{}\"")
    public void setVarFromFile(String name, String filePath) {
        setVar(name, ScenarioPropsUtils.parse(filePath, scenarioProps));
    }

    @Given("load vars from file \"{}\"")
    public void loadScenarioPropertiesFromFile(String filePath) {
        Set<String> propertyNames = ScenarioPropsUtils.loadPropsFromFile(filePath, scenarioProps);
        scenarioUtils.log("Loaded scenario properties having names: {}", propertyNames);
    }

    @Given("load file \"{}\" to var \"{}\"")
    public void loadScenarioPropertiesFromFile(String filePath, String propertyName) {
        ScenarioPropsUtils.loadFileAsScenarioProperty(filePath, scenarioProps, propertyName);
        scenarioUtils.log("Loaded scenario property with name: {}", propertyName);
    }

    @Given("load vars from dir \"{}\"")
    public void setScenarioPropertiesFromDir(String dirPath) {
        Set<String> propertyNames = ScenarioPropsUtils.loadPropsFromDir(dirPath, scenarioProps);
        scenarioUtils.log("Loaded scenario properties having names: {}", propertyNames);
    }

    @Given("table {}=")
    public void setCustomDataTable(String name, List<Map<String, Object>> value) {
        scenarioProps.put(name, value);
        scenarioUtils.log("var {} = {}", name, value);
        LOG.debug("var {} = {}", name, value);
    }
}
