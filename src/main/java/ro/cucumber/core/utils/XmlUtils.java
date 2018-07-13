package ro.cucumber.core.utils;

import javax.xml.transform.Source;
import org.xmlunit.builder.Input;
import org.xmlunit.builder.Transform;

public class XmlUtils {

    public static final String STYLE_XSL = "<?xml version=\"1.0\"?>"
            + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">"
            + "<xsl:output method=\"xml\" encoding=\"UTF-8\" indent=\"no\" />"
            + "<xsl:strip-space elements=\"*\"/><xsl:template match=\"@*|node()\"><xsl:copy>"
            + "<xsl:apply-templates select=\"@*|node()\"></xsl:apply-templates></xsl:copy></xsl:template>"
            + "<xsl:template match=\"*/text()\">"
            + "    <xsl:value-of select=\".\" disable-output-escaping=\"yes\"/>\n"
            + "</xsl:template></xsl:stylesheet>";

    public static String toSingleLineString(String xml) {
        Source source = Input.fromString(xml).build();
        return Transform.source(source).withStylesheet(Input.fromString(STYLE_XSL).build()).build()
                .toString();
    }
}
