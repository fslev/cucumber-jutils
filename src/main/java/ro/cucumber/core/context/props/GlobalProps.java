package ro.cucumber.core.context.props;

import java.io.IOException;
import java.util.Properties;

public class GlobalProps {

    private final static String FILE_NAME = "global.properties";
    private static Properties props = loadProps();

    public static String get(String key) {
        if (key == null) {
            return null;
        }
        String val = props.getProperty(key);
        return val != null ? val.trim() : val;
    }

    private static Properties loadProps() {
        Properties props = new Properties();
        try {
            props.load(GlobalProps.class.getClassLoader().getResourceAsStream(FILE_NAME));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return props;
    }
}
