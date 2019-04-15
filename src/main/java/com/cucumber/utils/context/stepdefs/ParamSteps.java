package com.cucumber.utils.context.stepdefs;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.utils.Cucumbers;
import com.google.inject.Inject;
import cucumber.api.java.en.Given;
import cucumber.runtime.java.guice.ScenarioScoped;
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

    @Given("param {cstring}=\"{cstring}\"")
    public void setParamStringQuoted(String name, String value) {
        scenarioProps.put(name, value);
        log.debug("Param {} = {}", name, value);
    }

    @Given("param {cstring}=")
    public void setParamDocString(String name, String value) {
        scenarioProps.put(name, value);
        log.debug("Param {} = {}", name, value);
    }

    @Given("param {cstring}={int}")
    public void setParamInteger(String name, Integer value) {
        scenarioProps.put(name, value);
        log.debug("Param {} = {}", name, value);
    }

    @Given("param {cstring} from file path \"{cstring}\"")
    public void setParamFromFile(String name, String filePath) {
        String value = cucumbers.read(filePath);
        scenarioProps.put(name, value);
    }

    @Given("load scenario props from file \"{cstring}\"")
    public void loadScenarioPropertiesFromFile(String filePath) {
        cucumbers.loadScenarioPropsFromFile(filePath);
    }

    @Given("load all scenario props from dir \"{cstring}\"")
    public void setScenarioPropertiesFromDir(String dirPath) {
        cucumbers.loadScenarioPropsFromDir(dirPath);
    }

    @Given("table {cstring}=")
    public void setCustomDataTable(String paramName, List value) {
        scenarioProps.put(paramName, value);
    }
}
