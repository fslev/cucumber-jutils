package com.cucumber.utils.engineering.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XmlUtilsTest {

    @Test
    public void testXmlIsNotValid() {
        String xml = "<a>test";
        assertFalse(XmlUtils.isValid(xml));
    }

    @Test
    public void testXmlsAreValid() {
        assertTrue(XmlUtils.isValid("<a>test<b>test</b></a>"));
        assertTrue(XmlUtils.isValid("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><a>test<b>test</b></a>"));
        assertTrue(XmlUtils.isValid("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<bookingResponse>\n" +
                "  <bookingId>dlc:~[var1]</bookingId>\n" +
                "</bookingResponse>"));
    }
}
