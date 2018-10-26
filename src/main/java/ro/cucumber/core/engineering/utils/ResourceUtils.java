package ro.cucumber.core.engineering.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.JsonNode;

public class ResourceUtils {

    public static String read(String filePath) {
        try (InputStream is = ResourceUtils.class.getClassLoader().getResourceAsStream(filePath);
             ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode readToJson(String filePath) {
        try {
            return JsonUtils.toJson(read(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Malformed JSON", e);
        }
    }
}
