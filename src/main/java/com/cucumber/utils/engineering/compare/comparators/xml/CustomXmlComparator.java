package com.cucumber.utils.engineering.compare.comparators.xml;

import com.cucumber.utils.engineering.placeholders.ScenarioPropertiesGenerator;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.ComparisonType;
import org.xmlunit.diff.DifferenceEvaluator;
import org.xmlunit.util.Nodes;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CustomXmlComparator implements DifferenceEvaluator {

    private Map<String, String> generatedProperties = new HashMap<>();
    private boolean childNodeListLength;
    private boolean childNodeListSequence;
    private boolean elementNumAttributes;

    public CustomXmlComparator(boolean childNodeListLength, boolean childNodeListSequence, boolean elementNumAttributes) {
        this.childNodeListLength = childNodeListLength;
        this.childNodeListSequence = childNodeListSequence;
        this.elementNumAttributes = elementNumAttributes;
    }

    @Override
    public ComparisonResult evaluate(Comparison comparison, ComparisonResult comparisonResult) {
        ComparisonType comparisonType = comparison.getType();

        if (comparison.getType() == ComparisonType.CHILD_NODELIST_LENGTH) {
            return childNodeListLength ? comparisonResult : ComparisonResult.SIMILAR;
        }

        if (comparison.getType() == ComparisonType.CHILD_NODELIST_SEQUENCE) {
            return childNodeListSequence ?
                    comparisonResult.equals(ComparisonResult.SIMILAR) ? ComparisonResult.DIFFERENT : comparisonResult
                    : ComparisonResult.SIMILAR;
        }

        if (comparison.getType() == ComparisonType.ELEMENT_NUM_ATTRIBUTES) {
            return elementNumAttributes ? comparisonResult : ComparisonResult.SIMILAR;
        }

        if (comparisonType == ComparisonType.XML_ENCODING
                || comparisonType == ComparisonType.XML_VERSION
                || comparisonType == ComparisonType.XML_STANDALONE
                || comparisonType == ComparisonType.NO_NAMESPACE_SCHEMA_LOCATION
                || comparison.getControlDetails().getTarget() == null) {
            return ComparisonResult.SIMILAR;
        }

        Node expectedNode = comparison.getControlDetails().getTarget();
        Node actualNode = comparison.getTestDetails().getTarget();

        if (expectedNode instanceof Attr && actualNode instanceof Attr) {
            String expected = ((Attr) expectedNode).getValue();
            String actual = ((Attr) actualNode).getValue();
            return compare(expected, actual);
        }

        if (expectedNode instanceof Text && actualNode instanceof Text) {
            String expected = ((Text) expectedNode).getData();
            String actual = ((Text) actualNode).getData();
            return compare(expected, actual);
        }

        if (comparisonType.equals(ComparisonType.ATTR_NAME_LOOKUP)) {
            return compare(Nodes.getAttributes(expectedNode), Nodes.getAttributes(actualNode));
        }

        return comparisonResult;
    }

    private ComparisonResult compare(Map<QName, String> expectedAttributes, Map<QName, String> actualAttributes) {
        for (Map.Entry<QName, String> expAttr : expectedAttributes.entrySet()) {
            String actualAttrVal = actualAttributes.get(expAttr.getKey());
            if (actualAttrVal == null) {
                return ComparisonResult.DIFFERENT;
            }
            try {
                match(expAttr.getValue(), actualAttrVal);
            } catch (XmlMatchException e) {
                return ComparisonResult.DIFFERENT;
            }
        }
        return ComparisonResult.SIMILAR;
    }

    private ComparisonResult compare(String expected, String actual) {
        try {
            generatedProperties.putAll(match(expected, actual));
        } catch (XmlMatchException e) {
            return ComparisonResult.DIFFERENT;
        }
        return ComparisonResult.SIMILAR;
    }

    public Map<String, String> match(String expected, String actual) throws XmlMatchException {
        ScenarioPropertiesGenerator generator = new ScenarioPropertiesGenerator(expected, actual);
        boolean hasPropertiesToGenerate = !generator.getProperties().isEmpty();
        String parsedExpected = hasPropertiesToGenerate ? generator.getParsedTarget() : expected;
        String parsedExpectedQuoted = hasPropertiesToGenerate ? generator.getParsedTarget(true) : expected;
        try {
            Pattern pattern = Pattern.compile(parsedExpectedQuoted);
            if (pattern.matcher(actual).matches()) {
                return generator.getProperties();
            } else {
                throw new XmlMatchException();
            }
        } catch (PatternSyntaxException e) {
            if (parsedExpected.equals(actual)) {
                return generator.getProperties();
            } else {
                throw new XmlMatchException();
            }
        }
    }

    public Map<String, String> getGeneratedProperties() {
        return generatedProperties;
    }

}
