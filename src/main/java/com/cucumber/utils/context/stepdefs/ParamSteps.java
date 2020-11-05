package com.cucumber.utils.context.stepdefs;

import com.cucumber.utils.context.ScenarioPropsUtils;
import com.cucumber.utils.context.props.ScenarioProps;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

@ScenarioScoped
public class ParamSteps {

    private static final Logger LOG = LogManager.getLogger();
    @Inject
    private ScenarioProps scenarioProps;

    @Given("param {}=\"{}\"")
    public void setParamString(String name, Object value) {
        scenarioProps.put(name, value);
        LOG.debug("Param {} = {}", name, value);
    }

    @Given("param {}=")
    public void setParamDocString(String name, StringBuilder value) {
        setParamString(name, value.toString());
    }

    @Given("param {} from file path \"{}\"")
    public void setParamFromFile(String name, String filePath) {
        setParamString(name, ScenarioPropsUtils.parse(filePath, scenarioProps));
    }

    @Given("load scenario props from file \"{}\"")
    public void loadScenarioPropertiesFromFile(String filePath) {
        ScenarioPropsUtils.loadPropsFromFile(filePath, scenarioProps);
    }

    @Given("load file \"{}\" to scenario property \"{}\"")
    public void loadScenarioPropertiesFromFile(String filePath, String propertyName) {
        ScenarioPropsUtils.loadFileAsScenarioProperty(filePath, scenarioProps, propertyName);
    }

    @Given("load all scenario props from dir \"{}\"")
    public void setScenarioPropertiesFromDir(String dirPath) {
        ScenarioPropsUtils.loadPropsFromDir(dirPath, scenarioProps);
    }

    @Given("table {}=")
    public void setCustomDataTable(String name, List<Map<String, Object>> value) {
        scenarioProps.put(name, value);
        LOG.debug("Param {} = {}", name, value);
    }
}
