package ro.cucumber.core.basicstepdefs;

import com.google.inject.Inject;
import cucumber.api.java.en.Given;
import cucumber.runtime.java.guice.ScenarioScoped;
import ro.cucumber.core.context.compare.Cucumbers;
import ro.cucumber.core.context.props.ScenarioProps;

import java.util.List;

@ScenarioScoped
public class ParamSteps {

    @Inject
    private ScenarioProps scenarioProps;

    @Given("param {cstring}=\"{cstring}\"")
    public void setParamStringQuoted(String name, String value) {
        scenarioProps.put(name, value);
    }

    @Given("param {cstring}=")
    public void setParamDocString(String name, String value) {
        scenarioProps.put(name, value);
    }

    @Given("param {cstring} from file path \"{cstring}\"")
    public void setParamFromFile(String name, String filePath) {
        String value = Cucumbers.read(filePath);
        scenarioProps.put(name, value);
    }

    @Given("load scenario props from file \"{cstring}\"")
    public void loadScenarioPropertiesFromFile(String filePath) {
        Cucumbers.loadScenarioPropsFromFile(filePath);
    }

    @Given("load all scenario props from dir \"{cstring}\"")
    public void setScenarioPropertiesFromDir(String dirPath) {
        Cucumbers.loadScenarioPropsFromDir(dirPath);
    }

    @Given("table {cstring}=")
    public void setCustomDataTable(String paramName, List value) {
        scenarioProps.put(paramName, value);
    }
}
