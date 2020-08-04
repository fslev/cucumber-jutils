package com.cucumber.utils.engineering.match.comparators.xml;

import com.cucumber.utils.engineering.match.StringMatcher;
import com.cucumber.utils.engineering.match.condition.MatchCondition;
import com.cucumber.utils.exceptions.InvalidTypeException;
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
import java.util.Set;

public class CustomXmlComparator implements DifferenceEvaluator {

    private final Map<String, Object> generatedProperties = new HashMap<>();
    private final Set<MatchCondition> matchConditions;

    public CustomXmlComparator(Set<MatchCondition> matchConditions) {
        this.matchConditions = matchConditions;
    }

    @Override
    public ComparisonResult evaluate(Comparison comparison, ComparisonResult comparisonResult) {
        ComparisonType comparisonType = comparison.getType();

        if (comparison.getType() == ComparisonType.CHILD_NODELIST_LENGTH) {
            return matchConditions.contains(MatchCondition.XML_CHILD_NODELIST_LENGTH) ? comparisonResult : ComparisonResult.SIMILAR;
        }

        if (comparison.getType() == ComparisonType.CHILD_NODELIST_SEQUENCE) {
            return matchConditions.contains(MatchCondition.XML_CHILD_NODELIST_SEQUENCE) ?
                    comparisonResult.equals(ComparisonResult.SIMILAR) ? ComparisonResult.DIFFERENT : comparisonResult
                    : ComparisonResult.SIMILAR;
        }

        if (comparison.getType() == ComparisonType.ELEMENT_NUM_ATTRIBUTES) {
            return matchConditions.contains(MatchCondition.XML_ELEMENT_NUM_ATTRIBUTES) ? comparisonResult : ComparisonResult.SIMILAR;
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
        try {
            generatedProperties.putAll(match(expectedAttributes, actualAttributes));
        } catch (XmlMatchException e) {
            return ComparisonResult.DIFFERENT;
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

    public Map<String, Object> match(Map<QName, String> expectedAttributes, Map<QName, String> actualAttributes) throws XmlMatchException {
        Map<String, Object> generatedProps = new HashMap<>();
        for (Map.Entry<QName, String> expAttr : expectedAttributes.entrySet()) {
            String actualAttrVal = actualAttributes.get(expAttr.getKey());
            if (actualAttrVal == null) {
                throw new XmlMatchException();
            }
            try {
                generatedProps.putAll(match(expAttr.getValue(), actualAttrVal));
            } catch (XmlMatchException e) {
                throw new XmlMatchException();
            }
        }
        return generatedProps;
    }

    public Map<String, Object> match(String expected, String actual) throws XmlMatchException {
        try {
            return new StringMatcher(null, expected, actual, matchConditions).match();
        } catch (AssertionError | InvalidTypeException e) {
            throw new XmlMatchException();
        }
    }

    public Map<String, Object> getGeneratedProperties() {
        return generatedProperties;
    }

}
