package ro.cucumber.core.utils;


import javax.xml.transform.TransformerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SymbolParser {

    private static String SYMBOL_ASSIGN_START = "\\$\\[";
    private static String SYMBOL_ASSIGN_END = "\\]";
    private static String SYMBOL_ASSIGN_REGEX = SYMBOL_ASSIGN_START + "(.*?)" + SYMBOL_ASSIGN_END;

    public static Map<String, String> extractAssignSymbols(String source, String dest) {
        Map<String, String> symbols = new HashMap<>();
        List<String> symbolNames = getAssignSymbolNames(source);
        if (symbolNames.isEmpty()) {
            return symbols;
        }
        for (String name : symbolNames) {
            source = source.replaceAll(SYMBOL_ASSIGN_START + name + SYMBOL_ASSIGN_END,
                    "\\\\E(.*?)\\\\Q");
        }
        source = "\\Q" + source + "\\E";
        Pattern pattern = Pattern.compile(source,
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(dest);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                symbols.put(symbolNames.get(i - 1), matcher.group(i));
            }
        }
        return symbols;
    }

    private static List<String> getAssignSymbolNames(String str) {
        List<String> names = new ArrayList<>();
        Pattern pattern = Pattern.compile(SYMBOL_ASSIGN_REGEX,
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                names.add(matcher.group(i));
            }
        }
        return names;
    }


    public static void main(String[] args) throws TransformerException {

        String source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "   <Emp id=\"1\"><name> Pankaj</name>   <age>25</age>\n"
                + "              <role>Deve $[here]oper</role><gen>Ma$[blah2]fds</gen></Emp>";
        final String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "   <Emp id=\"1\"><name> Pankaj</name>   <age>25</age>\n"
                + "              <role>Deve zarzvavatloper</role><gen>Male>wafds</gen></Emp>";
        System.out.println(XmlUtils.toSingleLineString(source));
        System.out.println(XmlUtils.toSingleLineString(xmlStr));
        System.out.println(extractAssignSymbols(XmlUtils.toSingleLineString(source),
                XmlUtils.toSingleLineString(xmlStr)));
        // System.out.println(XmlUtils.toSingleLineString(xmlStr));
        // System.out.println(getAssignSymbolNames(XmlUtils.toSingleLineString(xmlStr)));
    }
}
