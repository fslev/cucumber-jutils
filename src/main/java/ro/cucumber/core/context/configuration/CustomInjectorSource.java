package ro.cucumber.core.context.configuration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;

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
