package com.cucumber.utils.context.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import cucumber.api.guice.CucumberModules;
import cucumber.runtime.java.guice.InjectorSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InjectorByThreadSource implements InjectorSource {
    private static Map<Long, Injector> injectors = new ConcurrentHashMap<>();

    public static Injector getInjector(long threadId) {
        if (injectors.get(threadId) == null) {
            Injector injector = Guice.createInjector(Stage.PRODUCTION, CucumberModules.createScenarioModule());
            injectors.put(threadId, injector);
            return injector;
        }
        return injectors.get(threadId);
    }

    @Override
    public Injector getInjector() {
        long threadId = Thread.currentThread().getId();
        return getInjector(threadId);
    }
}
