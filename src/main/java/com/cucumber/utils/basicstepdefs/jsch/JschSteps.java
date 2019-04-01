package com.cucumber.utils.basicstepdefs.jsch;

import com.cucumber.utils.clients.jsch.JschClient;
import com.cucumber.utils.context.compare.Cucumbers;
import com.cucumber.utils.engineering.utils.ResourceUtils;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;

import java.util.Properties;

@ScenarioScoped
public class JschSteps {

    private JschClient client;

    @Given("JSCH connection from properties file \"{cstring}\"")
    public void init(String relFilePath) {
        Properties connProps = ResourceUtils.readProps(relFilePath);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        this.client = new JschClient(connProps.getProperty("host").trim(),
                Integer.valueOf(connProps.getProperty("port", "22").trim()),
                connProps.getProperty("user").trim(), connProps.getProperty("password", "").trim(),
                connProps.getProperty("privateKey").trim(), config);
        this.client.connect();
    }

    @Then("JSCH execute command \"{cstring}\" and check response=\"{cstring}\"")
    public void executeCmd(String cmd, String expected) {
        String actual = this.client.sendCommand(cmd).trim();
        Cucumbers.compare(expected, actual);
    }

    @And("JSCH disconnect")
    public void disconnect() {
        this.client.disconnect();
    }
}
