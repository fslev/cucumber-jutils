package com.cucumber.utils.context.stepdefs;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.utils.Cucumbers;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

@ScenarioScoped
public class ParamSteps {

    private Logger log = LogManager.getLogger();
    @Inject
    private Cucumbers cucumbers;
    @Inject
    private ScenarioProps scenarioProps;

    @Given("param {}=\"{}\"")
    public void setParamString(String name, String value) {
        scenarioProps.put(name, value);
        log.debug("Param {} = {}", name, value);
    }

    @Given("param {}=")
    public void setParamDocString(String name, StringBuilder value) {
        setParamString(name, value.toString());
    }

    @Given("param {} from file path \"{}\"")
    public void setParamFromFile(String name, String filePath) {
        setParamString(name, cucumbers.read(filePath));
    }

    @Given("load scenario props from file \"{}\"")
    public void loadScenarioPropertiesFromFile(String filePath) {
        cucumbers.loadScenarioPropsFromFile(filePath);
    }

    @Given("load all scenario props from dir \"{}\"")
    public void setScenarioPropertiesFromDir(String dirPath) {
        cucumbers.loadScenarioPropsFromDir(dirPath);
    }

    @Given("table {}=")
    public void setCustomDataTable(String name, List<Map<String, String>> value) {
        scenarioProps.put(name, value);
        log.debug("Param {} = {}", name, value);
    }
}
