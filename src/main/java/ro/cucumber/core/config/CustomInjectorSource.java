package ro.cucumber.core.config;

import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

public class CustomInjectorSource implements InjectorSource {

    private static Injector injector;

    public static Injector getContextInjector() {
        return injector;
    }

    @Override
    public Injector getInjector() {
        injector = Guice.createInjector(Stage.PRODUCTION, CucumberModules.SCENARIO);
        return injector;
    }
}
