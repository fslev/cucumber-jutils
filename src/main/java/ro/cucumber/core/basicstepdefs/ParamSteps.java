package ro.cucumber.core.basicstepdefs;

import cucumber.api.java.en.Given;
import cucumber.runtime.java.guice.ScenarioScoped;
import ro.cucumber.core.context.props.PlaceholderFiller;
import ro.cucumber.core.context.props.ScenarioProps;
import ro.cucumber.core.engineering.utils.ResourceUtils;
import java.util.List;
import com.google.inject.Inject;

@ScenarioScoped
public class ParamSteps {

    @Inject
    ScenarioProps scenarioProps;

    @Given("param {cstring}={cstring}")
    public void setParamStringQuoted(String name, String value) {
        scenarioProps.put(name, value);
    }

    @Given("param {cstring}=")
    public void setParamDocString(String name, String value) {
        scenarioProps.put(name, value);
    }

    @Given("param {cstring} from file path {cstring}")
    public void setParamFromFile(String name, String filePath) {
        String value = new PlaceholderFiller(ResourceUtils.read(filePath)).fill().toString();
        scenarioProps.put(name, value);
    }

    @Given("table {cstring}=")
    public void setCustomDataTable(String paramName, List value) {
        scenarioProps.put(paramName, value);
    }
}
