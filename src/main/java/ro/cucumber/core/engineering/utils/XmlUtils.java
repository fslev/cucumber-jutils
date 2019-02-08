package ro.cucumber.core.engineering.utils;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xmlunit.builder.Input;
import org.xmlunit.builder.Transform;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import java.io.IOException;
import java.io.StringReader;

public class XmlUtils {

    public static final String STYLE_XSL = "<?xml version=\"1.0\"?>"
            + "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">"
            + "<xsl:output method=\"xml\" encoding=\"UTF-8\" indent=\"no\" />"
            + "<xsl:strip-space elements=\"*\"/><xsl:template compare=\"@*|node()\"><xsl:copy>"
            + "<xsl:apply-templates select=\"@*|node()\"></xsl:apply-templates></xsl:copy></xsl:template>"
            + "<xsl:template compare=\"*/text()\">"
            + "    <xsl:value-of select=\".\" disable-output-escaping=\"yes\"/>\n"
            + "</xsl:template></xsl:stylesheet>";

    public static String toSingleLineString(String xml) {
        Source source = Input.fromString(xml).build();
        return Transform.source(source).withStylesheet(Input.fromString(STYLE_XSL).build()).build()
                .toString();
    }

    public static boolean isValid(String xml) {
        DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                }
            });
            builder.parse(new InputSource(new StringReader(xml)));
        } catch (SAXException | IOException e) {
            return false;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
