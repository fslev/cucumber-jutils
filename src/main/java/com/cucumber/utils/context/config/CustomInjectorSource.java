package com.cucumber.utils.context.config;

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
        injector = (injector == null) ? Guice.createInjector(Stage.PRODUCTION, CucumberModules.SCENARIO) : injector;
        return injector;
    }
}
