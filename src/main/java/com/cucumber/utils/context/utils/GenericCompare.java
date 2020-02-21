package com.cucumber.utils.context.utils;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.engineering.compare.Compare;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

final class GenericCompare {
    private Logger log = LogManager.getLogger();
    private ScenarioProps scenarioProps;

    GenericCompare(ScenarioProps scenarioProps) {
        this.scenarioProps = scenarioProps;
    }

    void compare(String message, Object expected, Object actual,
                 boolean jsonNonExtensibleObject, boolean jsonNonExtensibleArray, boolean jsonArrayStrictOrder,
                 boolean xmlChildListLength, boolean xmlChildListSequence, boolean xmlElementNumAttributes) {
        Map<String, String> placeholdersAndValues = new Compare(message, expected, actual,
                jsonNonExtensibleObject, jsonNonExtensibleArray, jsonArrayStrictOrder,
                xmlChildListLength, xmlChildListSequence, xmlElementNumAttributes).compare();
        placeholdersAndValues.forEach(scenarioProps::put);
    }
}
