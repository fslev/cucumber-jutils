package com.cucumber.utils.context;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.props.ScenarioPropsParser;
import com.cucumber.utils.engineering.match.condition.MatchCondition;
import com.cucumber.utils.helper.ResourceUtils;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Set;
import java.util.function.Supplier;

@ScenarioScoped
public class Cucumbers {

    private final ScenarioProps scenarioProps;
    private final Compare genericCompare;

    @Inject
    private Cucumbers(ScenarioProps scenarioProps) {
        this.scenarioProps = scenarioProps;
        this.genericCompare = new Compare(scenarioProps);
    }

    public String read(String relativeFilePath) {
        try {
            return ScenarioPropsParser.parse(ResourceUtils.read(relativeFilePath), scenarioProps).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        genericCompare.compare(message, expected, actual, matchConditions);
    }

    public void pollAndCompare(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                               Supplier<Object> supplier, MatchCondition... matchConditions) {
        genericCompare.pollAndCompare(message, expected, pollDurationInSeconds, pollIntervalInMillis, exponentialBackOff, supplier, matchConditions);
    }

    public <T extends HttpResponse> void compareHttpResponse(String message, Object expected, T actual, MatchCondition... matchConditions) {
        try {
            genericCompare.compareHttpResponse(message, expected, actual, matchConditions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends HttpResponse> void pollAndCompareHttpResponse(String message, Object expected, Integer pollDurationInSeconds, Long pollIntervalInMillis, Double exponentialBackOff,
                                                                    Supplier<T> supplier, MatchCondition... matchConditions) {
        genericCompare.pollAndCompareHttpResponse(message, expected, pollDurationInSeconds, pollIntervalInMillis, exponentialBackOff, supplier, matchConditions);
    }
}
