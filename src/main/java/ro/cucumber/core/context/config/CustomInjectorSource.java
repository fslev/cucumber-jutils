package ro.cucumber.core.context.config;

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
        injector = (injector == null) ? Guice.createInjector(Stage.PRODUCTION, CucumberModules.SCENARIO) : injector;
        return injector;
    }
}
