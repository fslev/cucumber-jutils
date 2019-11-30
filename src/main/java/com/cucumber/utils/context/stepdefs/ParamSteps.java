package com.cucumber.utils.context.stepdefs;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.utils.Cucumbers;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@ScenarioScoped
public class ParamSteps {

    private Logger log = LogManager.getLogger();
    @Inject
    private Cucumbers cucumbers;
    @Inject
    private ScenarioProps scenarioProps;

    @Given("param {}=\"{}\"")
    public void setParamStringQuoted(String name, String value) {
        scenarioProps.put(name, value);
        log.debug("Param {} = {}", name, value);
    }

    @Given("param {}=")
    public void setParamDocString(String name, StringBuilder value) {
        scenarioProps.put(name, value.toString());
        log.debug("Param {} = {}", name, value);
    }

    @Given("param {} from file path \"{}\"")
    public void setParamFromFile(String name, String filePath) {
        String value = cucumbers.read(filePath);
        scenarioProps.put(name, value);
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
    public void setCustomDataTable(String name, List value) {
        scenarioProps.put(name, value);
        log.debug("Param {} = {}", name, value);
    }
}
