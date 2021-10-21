package com.cucumber.utils.context.vars;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class PathVarsTest {

    @Test
    public void testStringPathVar() {
        ScenarioVars scenarioVars = new ScenarioVars();
        scenarioVars.put("var1", "{\"a1\":\"value1\",\"a2\":{\"a21\":[1,2,3]}}");
        assertNull(scenarioVars.get("var1/a1/a2"));
        assertTrue(scenarioVars.get("var1/a2/a21") instanceof String);
        assertEquals("[1,2,3]", scenarioVars.get("var1/a2/a21"));
        assertEquals("3", scenarioVars.get("var1/a2/a21/2"));
    }

    @Test
    public void testObjectPathVar() {
        ScenarioVars scenarioVars = new ScenarioVars();
        scenarioVars.put("var1", Map.of("a1", true, "a2", Map.of("a21", List.of("value1", 2, true))));
        assertNull(scenarioVars.get("var1/a1/a2"));
        assertTrue(scenarioVars.get("var1/a2") instanceof ObjectNode);
        assertEquals(new ObjectMapper().createObjectNode()
                .set("a21", new ObjectMapper().createArrayNode().add("value1").add(2).add(true)), scenarioVars.get("var1/a2"));
        assertTrue(scenarioVars.get("var1/a2/a21") instanceof ArrayNode);
        assertEquals(new ObjectMapper().createArrayNode().add("value1").add(2).add(true), scenarioVars.get("var1/a2/a21"));
        assertTrue(scenarioVars.get("var1/a2/a21/1") instanceof IntNode);
        assertEquals(new IntNode(2), scenarioVars.get("var1/a2/a21/1"));
    }
}
