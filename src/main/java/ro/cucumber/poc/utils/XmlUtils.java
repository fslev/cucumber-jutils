package ro.cucumber.poc.utils;

import javax.xml.transform.Source;
import org.xmlunit.builder.Input;
import org.xmlunit.builder.Transform;

public class XmlUtils {

    public static final String STYLE_XSL = "<?xml version=\"1.0\"?>\n"
            + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
            + "    <xsl:output indent=\"no\" />\n" + "    <xsl:strip-space elements=\"*\"/>\n"
            + "    <xsl:template match=\"@*|node()\">\n" + "        <xsl:copy>\n"
            + "            <xsl:apply-templates select=\"@*|node()\"/>\n" + "        </xsl:copy>\n"
            + "    </xsl:template>\n" + "</xsl:stylesheet>";

    public static String toSingleLineString(String xml) {
        Source source = Input.fromString(xml).build();
        return Transform.source(source).withStylesheet(Input.fromString(STYLE_XSL).build()).build()
                .toString();
    }
}
