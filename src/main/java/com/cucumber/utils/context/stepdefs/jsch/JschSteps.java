package com.cucumber.utils.context.stepdefs.jsch;

import com.cucumber.utils.clients.jsch.JschClient;
import com.cucumber.utils.context.utils.Cucumbers;
import com.cucumber.utils.engineering.utils.ResourceUtils;
import com.google.inject.Inject;
import cucumber.api.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;

import java.util.Properties;

@ScenarioScoped
public class JschSteps {

    private JschClient client;

    @Inject
    private Cucumbers cucumbers;

    @Given("JSCH connection from properties file \"{}\"")
    public void init(String relFilePath) {

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        Properties connProps = ResourceUtils.readProps(relFilePath);
        String host = connProps.getProperty("host").trim();
        int port = Integer.valueOf(connProps.getProperty("port", "22").trim());
        String user = connProps.getProperty("user").trim();
        String pwd = connProps.getProperty("password", "").trim();
        String privateKey = connProps.getProperty("privateKey").trim();

        this.client = new JschClient(host, port, user, pwd, privateKey, config);
        this.client.connect();
    }

    @Then("JSCH execute command \"{}\" and check response=\"{}\"")
    public void executeCmd(String cmd, String expected) {
        String actual = this.client.sendCommand(cmd).trim();
        cucumbers.compare(expected, actual);
    }

    @After("@jsch_cleanup")
    @And("JSCH disconnect")
    public void disconnect() {
        if (this.client != null) {
            this.client.disconnect();
        }
    }
}
