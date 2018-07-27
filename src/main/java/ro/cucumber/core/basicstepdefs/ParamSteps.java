package ro.cucumber.core.basicstepdefs;

import cucumber.api.java.en.Given;
import cucumber.runtime.java.guice.ScenarioScoped;
import ro.cucumber.core.context.config.CustomDataTable;
import ro.cucumber.core.context.props.ScenarioProps;
import ro.cucumber.core.context.props.SymbolsParser;
import com.google.inject.Inject;

@ScenarioScoped
public class ParamSteps {

    @Inject
    ScenarioProps scenarioProps;

    @Given("param {cstring}={cstring}")
    public void setParamString(String name, String value) {
        scenarioProps.put(name, new SymbolsParser(value).parse());
    }

    @Given("param {cstring}=")
    public void setParamDocString(String name, String value) {
        scenarioProps.put(name, new SymbolsParser(value).parse());
    }

    @Given("table {cstring}=")
    public void setCustomDataTable(String paramName, CustomDataTable value) {
        scenarioProps.put(paramName, value);
    }
}
