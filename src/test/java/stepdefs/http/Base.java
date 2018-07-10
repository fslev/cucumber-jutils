package stepdefs.http;

import cucumber.runtime.java.guice.ScenarioScoped;
import ro.cucumber.poc.http.HttpClient;
import com.google.inject.Inject;

public class Base {

    @Inject
    HttpClient client;
}
