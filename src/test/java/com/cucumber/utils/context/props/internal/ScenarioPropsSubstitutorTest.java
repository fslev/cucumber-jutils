package com.cucumber.utils.context.props.internal;

import com.cucumber.utils.context.props.ScenarioProps;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ScenarioPropsSubstitutorTest {

    private ScenarioProps scenarioProps;

    @Before
    public void init() {
        scenarioProps = new ScenarioProps();
    }

    @Test
    public void testScenarioPropsSubstitution() {
        String s = "abc#[prop1]#[prop2]def";
        Map<String, Object> props = new HashMap<>();
        props.put("prop1", 1);
        props.put("prop2", "some value with #[dummy]");
        scenarioProps.putAll(props);
        assertEquals("abc1some value with #[dummy]def", ScenarioPropsSubstitutor.replace(s, scenarioProps));

        String s1 = "Val: #[obj]";
        props.put("obj", new BigInteger("1000"));
        scenarioProps.putAll(props);
        assertEquals("Val: 1000", ScenarioPropsSubstitutor.replace(s1, scenarioProps));

        String s1a = " #[obj] ";
        props.put("obj", new BigInteger("1000"));
        scenarioProps.putAll(props);
        assertEquals(" 1000 ", ScenarioPropsSubstitutor.replace(s1a, scenarioProps));

        String s2 = "";
        props.put("obj", new BigInteger("1000"));
        scenarioProps.putAll(props);
        assertTrue(ScenarioPropsSubstitutor.replace(s2, scenarioProps).toString().isEmpty());

        assertNull(ScenarioPropsSubstitutor.replace(null, scenarioProps));

        String s3 = "#[var_2020]";
        assertEquals(s3, ScenarioPropsSubstitutor.replace(s3, scenarioProps));

        String s4 = "#[null_var]";
        props.put("null_var", null);
        scenarioProps.putAll(props);
        assertNull(ScenarioPropsSubstitutor.replace(s4, scenarioProps));

        String s5 = "This is null: #[null_var] !";
        props.put("null_var", null);
        scenarioProps.putAll(props);
        assertEquals("This is null: null !", ScenarioPropsSubstitutor.replace(s5, scenarioProps));
    }

    @Test
    public void testStandaloneScenarioPropsSubstitution() {
        String s = "#[prop1#[prop2]";
        Map<String, Object> props = new HashMap<>();
        props.put("prop1#[prop2", "replacement");
        scenarioProps.putAll(props);
        assertEquals("replacement", ScenarioPropsSubstitutor.replace(s, scenarioProps));

        String s1 = "#[a]";
        props.put("a", "replacement1");
        scenarioProps.putAll(props);
        assertEquals("replacement1", ScenarioPropsSubstitutor.replace(s1, scenarioProps));

        String s2 = "#[obj]";
        props.put("obj", new BigInteger("1000"));
        scenarioProps.putAll(props);
        assertEquals(new BigInteger("1000"), ScenarioPropsSubstitutor.replace(s2, scenarioProps));

        String s3 = "#[boolean]";
        props.put("boolean", true);
        scenarioProps.putAll(props);
        assertEquals(true, ScenarioPropsSubstitutor.replace(s3, scenarioProps));
    }

    @Test
    public void testStandaloneScenarioPropsSubstitutionWithDynamicValues() {
        String s = "#[uid]";
        assertTrue(ScenarioPropsSubstitutor.replace(s, scenarioProps).toString().matches("[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}"));

        s = "this is #[uid]";
        assertTrue(ScenarioPropsSubstitutor.replace(s, scenarioProps).toString().matches("this is [a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}"));

        s = "#[short-random]";
        assertTrue(ScenarioPropsSubstitutor.replace(s, scenarioProps) instanceof Integer);
        assertTrue(-1 < (int) ScenarioPropsSubstitutor.replace(s, scenarioProps));
        assertTrue(Short.MAX_VALUE * 2 > (int) ScenarioPropsSubstitutor.replace(s, scenarioProps));

        s = "random val: #[short-random]";
        assertTrue(ScenarioPropsSubstitutor.replace(s, scenarioProps).toString().matches("random val: ([0-9]{1}|[0-9]{2}|[0-9]{3}|[0-9]{4}|[0-9]{5})"));

        s = "#[now]";
        assertTrue(ScenarioPropsSubstitutor.replace(s, scenarioProps) instanceof Long);
        assertTrue(System.currentTimeMillis() <= (long) ScenarioPropsSubstitutor.replace(s, scenarioProps));
        assertTrue(System.currentTimeMillis() + 10000 > (long) ScenarioPropsSubstitutor.replace(s, scenarioProps));

        s = "Millis from from 1970: #[now]";
        assertTrue(ScenarioPropsSubstitutor.replace(s, scenarioProps).toString().matches("Millis from from 1970: [0-9]*"));
    }
}