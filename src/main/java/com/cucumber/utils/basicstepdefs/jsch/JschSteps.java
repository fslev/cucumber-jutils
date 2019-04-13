package com.cucumber.utils.basicstepdefs.jsch;

import com.cucumber.utils.clients.jsch.JschClient;
import com.cucumber.utils.context.compare.Cucumbers;
import com.cucumber.utils.engineering.utils.ResourceUtils;
import cucumber.api.java.After;
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

    @Then("JSCH execute command \"{cstring}\" and check response=\"{cstring}\"")
    public void executeCmd(String cmd, String expected) {
        String actual = this.client.sendCommand(cmd).trim();
        Cucumbers.compare(expected, actual);
    }

    @After("@jsch_cleanup")
    @And("JSCH disconnect")
    public void disconnect() {
        if (this.client != null) {
            this.client.disconnect();
        }
    }
}
