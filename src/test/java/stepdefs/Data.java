package stepdefs;

import cucumber.api.java.en.When;

public class Data {

    private String data;

    public String getData() {
        return this.data;
    }

    @When("^save (.*)$")
    public void save(String data) {
        this.data = data;
    }
}
