package com.cucumber.utils.context;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.props.ScenarioPropsParser;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.jtest.utils.common.ResourceUtils;
import io.jtest.utils.matcher.ObjectMatcher;
import io.jtest.utils.matcher.condition.MatchCondition;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Set;
import java.util.function.Supplier;

@ScenarioScoped
public class Cucumbers {

    private final ScenarioProps scenarioProps;

    @Inject
    private Cucumbers(ScenarioProps scenarioProps) {
        this.scenarioProps = scenarioProps;
    }

    public String read(String relativeFilePath) {
        try {
            return ScenarioPropsParser.parse(ResourceUtils.read(relativeFilePath), scenarioProps).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadFileToScenarioProperty(String relativeFilePath, String propertyName) {
        ScenarioPropsLoader.loadScenarioPropertyFile(relativeFilePath, scenarioProps, propertyName);
    }

    /**
     * @param relativeFilePath
     * @return names of loaded properties
     */
    public Set<String> loadScenarioPropsFromFile(String relativeFilePath) {
        return ScenarioPropsLoader.loadScenarioPropsFromFile(relativeFilePath, scenarioProps);
    }

    /**
     * Loads scenario properties from all supported file patterns: .properties, .yaml, .property, ...
     *
     * @return names of loaded properties
     */
    public Set<String> loadScenarioPropsFromDir(String relativeDirPath) {
        return ScenarioPropsLoader.loadScenarioPropsFromDir(relativeDirPath, scenarioProps);
    }

    public void compare(Object expected, Object actual, MatchCondition... matchConditions) {
        compare(null, expected, actual, matchConditions);
    }

    public void compare(String message, Object expected, Object actual, MatchCondition... matchConditions) {
        scenarioProps.putAll(ObjectMatcher.match(message, expected, actual, matchConditions));
    }

    public void pollAndCompare(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                               Supplier<Object> supplier, MatchCondition... matchConditions) {
        scenarioProps.putAll(ObjectMatcher.pollAndMatch(message, expected, supplier, pollDurationInSeconds, pollIntervalInMillis, exponentialBackOff, matchConditions));
    }

    public <T extends HttpResponse> void compareHttpResponse(String message, Object expected, T actual, MatchCondition... matchConditions) {
        scenarioProps.putAll(ObjectMatcher.matchHttpResponse(message, expected, actual, matchConditions));
    }

    public <T extends HttpResponse> void pollAndCompareHttpResponse(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                                                                    Supplier<T> supplier, MatchCondition... matchConditions) {
        scenarioProps.putAll(ObjectMatcher.pollAndMatchHttpResponse(message, expected, supplier, pollDurationInSeconds, pollIntervalInMillis, exponentialBackOff, matchConditions));
    }
}
