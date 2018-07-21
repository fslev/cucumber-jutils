package ro.cucumber.core.basicstepdefs;

import com.google.inject.Inject;
import cucumber.api.java.en.Given;
import cucumber.runtime.java.guice.ScenarioScoped;
import ro.cucumber.core.context.props.ScenarioProps;

@ScenarioScoped
public class ParamSteps {

    @Inject
    ScenarioProps props;

    @Given("param {cstring} =")
    public void setParamDocString(String name, String value) {
        props.put(name, value);
    }

    @Given("param {cstring} = {cstring}")
    public void setParamString(String name, String value) {
        props.put(name, value);
    }
}
