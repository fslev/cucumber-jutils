package ro.cucumber.core.engineering.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

public class ResourceUtils {

    public static String read(String filePath) {
        return readFromAbsolutePath(Thread.currentThread().getContextClassLoader().getResource(filePath).getPath());
    }

    /**
     * @return a Map<String,String> between corresponding file paths and file contents
     */
    public static Map<String, String> readDirectory(String dirPath) {
        try {
            Path basePath = Paths.get(Thread.currentThread().getContextClassLoader().getResource(dirPath).toURI());
            Map<String, String> map = Files.walk(basePath).filter(path -> path.toFile().isFile())
                    .collect(Collectors.toMap(path -> basePath.relativize(path).toString(),
                            path -> readFromAbsolutePath(path.toString())));
            return map;
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readFromAbsolutePath(String filePath) {
        try (InputStream is = new FileInputStream(filePath);
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
}
