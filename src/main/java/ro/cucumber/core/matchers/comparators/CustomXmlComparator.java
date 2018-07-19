package ro.cucumber.core.matchers.comparators;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.ComparisonType;
import org.xmlunit.diff.DifferenceEvaluator;
import ro.cucumber.core.symbols.SymbolsAssignParser;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CustomXmlComparator implements DifferenceEvaluator {

    private Map<String, String> assignSymbols = new HashMap<>();

    @Override
    public ComparisonResult evaluate(Comparison comparison, ComparisonResult comparisonResult) {
        ComparisonType comparisonType = comparison.getType();
        if (comparisonType == ComparisonType.CHILD_NODELIST_LENGTH
                || comparisonType == ComparisonType.CHILD_NODELIST_SEQUENCE
                || comparisonType == ComparisonType.XML_ENCODING
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
        if (comparisonResult == ComparisonResult.EQUAL
                || comparisonResult == ComparisonResult.SIMILAR) {
            return comparisonResult;
        }

        return ComparisonResult.DIFFERENT;
    }

    private ComparisonResult compare(String expected, String actual) {
        SymbolsAssignParser parser = new SymbolsAssignParser(expected, actual);
        boolean hasAssignSymbols = !parser.getAssignSymbols().isEmpty();
        String parsedExpected = hasAssignSymbols ? parser.getStringWithAssignValues() : expected;
        try {
            Pattern pattern = Pattern.compile(parsedExpected);
            if (pattern.matcher(actual).matches()) {
                if (hasAssignSymbols) {
                    this.assignSymbols.putAll(parser.getAssignSymbols());
                }
                return ComparisonResult.SIMILAR;
            } else {
                return ComparisonResult.DIFFERENT;
            }
        } catch (PatternSyntaxException e) {
            if (parsedExpected.equals(actual)) {
                if (hasAssignSymbols) {
                    this.assignSymbols.putAll(parser.getAssignSymbols());
                }
                return ComparisonResult.EQUAL;
            } else {
                return ComparisonResult.DIFFERENT;
            }
        }
    }

    public Map<String, String> getAssignSymbols() {
        return assignSymbols;
    }
}
