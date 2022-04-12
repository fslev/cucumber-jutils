package com.cucumber.utils.context.steps;

import com.cucumber.utils.context.ScenarioUtils;
import com.cucumber.utils.context.ScenarioVarsUtils;
import com.cucumber.utils.context.vars.ScenarioVars;
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
    private ScenarioVars scenarioVars;
    @Inject
    private ScenarioUtils scenarioUtils;

    @Given("var {}=\"{}\"")
    public void setVar(String name, Object value) {
        scenarioVars.put(name, value);
        scenarioUtils.log("var {} = {}", name, value != null ? MessageUtil.cropL(value.toString()) : null);
        LOG.debug("var {} = {}", name, value);
    }

    @Given("var {}=")
    public void setVarFromDocString(String name, StringBuilder value) {
        setVar(name, value.toString());
    }

    @Given("var {} from file \"{}\"")
    public void setVarFromFile(String name, String filePath) {
        setVar(name, ScenarioVarsUtils.parse(filePath, scenarioVars));
    }

    @Given("load vars from file \"{}\"")
    public void loadScenarioVarsFromFile(String filePath) {
        Set<String> propertyNames = ScenarioVarsUtils.loadScenarioVarsFromFile(filePath, scenarioVars);
        scenarioUtils.log("Loaded scenario variables having names: {}", propertyNames);
    }

    @Given("load vars from dir \"{}\"")
    public void loadScenarioVarsFromDir(String dirPath) {
        Set<String> propertyNames = ScenarioVarsUtils.loadScenarioVarsFromDir(dirPath, scenarioVars);
        scenarioUtils.log("Loaded scenario variables having names: {}", propertyNames);
    }

    @Given("var {} from table")
    public void loadVarFromTable(String name, List<Map<String, Object>> value) {
        scenarioVars.put(name, value);
        scenarioUtils.log("var {} = {}", name, value);
        LOG.debug("var {} = {}", name, value);
    }
}
