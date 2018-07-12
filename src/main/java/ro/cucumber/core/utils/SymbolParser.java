package ro.cucumber.core.utils;


import javax.xml.transform.TransformerException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SymbolParser {

    private static String SYMBOL_NAME_REGEX = "&\\[(.*?)\\]";

    private static List<String> extractSymbolNames(String source) {
        List<String> symbolList = new ArrayList<>();
        Pattern pattern = Pattern.compile(SYMBOL_NAME_REGEX,
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                symbolList.add(matcher.group(i));
            }
        }
        return symbolList;
    }


    public static void main(String[] args) throws TransformerException {
        final String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "   <Emp id=\"1\"><name> Pankaj</name>   <age>25</age>\n"
                + "              <role>Deve loper</role><gen>Male</gen></Emp>";
        System.out.println(XmlUtils.toSingleLineString(xmlStr));
        System.out.println(extractSymbolNames("ds&[dds]sgfdgfd${k}a"));
    }
}
