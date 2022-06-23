package com.cucumber.utils.context.vars.internal;

import com.cucumber.utils.context.vars.ScenarioVars;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ScenarioVarsSubstitutorTest {

    private ScenarioVars scenarioVars;

    @Before
    public void init() {
        scenarioVars = new ScenarioVars();
    }

    @Test
    public void testScenarioVarsSubstitution() {
        String s = "abc#[prop1]#[prop2]def";
        Map<String, Object> props = new HashMap<>();
        props.put("prop1", 1);
        props.put("prop2", "some value with #[dummy]");
        scenarioVars.putAll(props);
        assertEquals("abc1some value with #[dummy]def", ScenarioVarsSubstitutor.replace(s, scenarioVars));

        String s1 = "Val: #[obj]";
        props.put("obj", new BigInteger("1000"));
        scenarioVars.putAll(props);
        assertEquals("Val: 1000", ScenarioVarsSubstitutor.replace(s1, scenarioVars));

        String s1a = " #[obj] ";
        props.put("obj", new BigInteger("1000"));
        scenarioVars.putAll(props);
        assertEquals(" 1000 ", ScenarioVarsSubstitutor.replace(s1a, scenarioVars));

        String s2 = "";
        props.put("obj", new BigInteger("1000"));
        scenarioVars.putAll(props);
        assertTrue(ScenarioVarsSubstitutor.replace(s2, scenarioVars).toString().isEmpty());

        assertNull(ScenarioVarsSubstitutor.replace(null, scenarioVars));

        String s3 = "#[var_2020]";
        assertEquals(s3, ScenarioVarsSubstitutor.replace(s3, scenarioVars));

        String s4 = "#[null_var]";
        props.put("null_var", null);
        scenarioVars.putAll(props);
        assertNull(ScenarioVarsSubstitutor.replace(s4, scenarioVars));

        String s5 = "This is null: #[null_var] !";
        props.put("null_var", null);
        scenarioVars.putAll(props);
        assertEquals("This is null: null !", ScenarioVarsSubstitutor.replace(s5, scenarioVars));
    }

    @Test
    public void testStandaloneScenarioVarsSubstitution() {
        String s = "#[0p$ro-p1.pr_op2@]";
        Map<String, Object> props = new HashMap<>();
        props.put("0p$ro-p1.pr_op2@", "replacement");
        scenarioVars.putAll(props);
        assertEquals("replacement", ScenarioVarsSubstitutor.replace(s, scenarioVars));

        String s1 = "#[a]";
        props.put("a", "replacement1");
        scenarioVars.putAll(props);
        assertEquals("replacement1", ScenarioVarsSubstitutor.replace(s1, scenarioVars));

        String s2 = "#[obj]";
        props.put("obj", new BigInteger("1000"));
        scenarioVars.putAll(props);
        assertEquals(new BigInteger("1000"), ScenarioVarsSubstitutor.replace(s2, scenarioVars));

        String s3 = "#[boolean]";
        props.put("boolean", true);
        scenarioVars.putAll(props);
        assertEquals(true, ScenarioVarsSubstitutor.replace(s3, scenarioVars));
    }

    @Test
    public void testStandaloneScenarioVarsSubstitutionWithDynamicValues() {
        String s = "#[uid]";
        assertTrue(ScenarioVarsSubstitutor.replace(s, scenarioVars).toString().matches("[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}"));

        s = "this is #[uid]";
        assertTrue(ScenarioVarsSubstitutor.replace(s, scenarioVars).toString().matches("this is [a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}"));

        s = "#[short-random]";
        assertTrue(ScenarioVarsSubstitutor.replace(s, scenarioVars) instanceof Integer);
        assertTrue(-1 < (int) ScenarioVarsSubstitutor.replace(s, scenarioVars));
        assertTrue(Short.MAX_VALUE > (int) ScenarioVarsSubstitutor.replace(s, scenarioVars));

        s = "random val: #[short-random]";
        assertTrue(ScenarioVarsSubstitutor.replace(s, scenarioVars).toString().matches("random val: ([0-9]{1}|[0-9]{2}|[0-9]{3}|[0-9]{4}|[0-9]{5})"));

        s = "#[int-random]";
        assertTrue(ScenarioVarsSubstitutor.replace(s, scenarioVars) instanceof Integer);
        assertTrue(-1 < (int) ScenarioVarsSubstitutor.replace(s, scenarioVars));
        assertTrue(Integer.MAX_VALUE > (int) ScenarioVarsSubstitutor.replace(s, scenarioVars));


        s = "#[now]";
        assertTrue(ScenarioVarsSubstitutor.replace(s, scenarioVars) instanceof Long);
        assertTrue(System.currentTimeMillis() <= (long) ScenarioVarsSubstitutor.replace(s, scenarioVars));
        assertTrue(System.currentTimeMillis() + 10000 > (long) ScenarioVarsSubstitutor.replace(s, scenarioVars));

        s = "Millis from from 1970: #[now]";
        assertTrue(ScenarioVarsSubstitutor.replace(s, scenarioVars).toString().matches("Millis from from 1970: [0-9]*"));
    }

    @Test
    public void testSubstitutionWithEmptyScenarioVars() {
        String s = "#[p]";
        scenarioVars.put("p", "");
        assertEquals("", ScenarioVarsSubstitutor.replace(s, scenarioVars));
    }
}