package com.cucumber.utils.context;

import com.cucumber.utils.context.props.ScenarioProps;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class ScenarioPropsUtilsTest {

    private ScenarioProps scenarioProps;

    @Before
    public void init() {
        this.scenarioProps = new ScenarioProps();
    }

    @Test
    public void testParserFromInvalidFile() {
        assertThrows(RuntimeException.class, () -> ScenarioPropsUtils.parse("idontexist.json", scenarioProps));
    }

    @Test
    public void loadPropertyFileFromInvalidExtension() {
        assertThrows(RuntimeException.class, () -> ScenarioPropsUtils.loadFileAsScenarioProperty(
                "props/invalid.extension", scenarioProps, "var1"));
    }
}
