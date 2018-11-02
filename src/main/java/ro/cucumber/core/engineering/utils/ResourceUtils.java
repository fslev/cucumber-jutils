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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResourceUtils {

    public static String read(String filePath) {
        return readFromAbsolutePath(Thread.currentThread().getContextClassLoader().getResource(filePath).getPath());
    }

    /**
     * @return a list of Map<String,String> data for each file within the directory and its sub-directories
     * A key-value pair from the map contains the file relative path respectively the file content
     */
    public static List<Map<String, String>> readDirectory(String dirPath) {
        try {
            Path base = Paths.get(Thread.currentThread().getContextClassLoader().getResource(dirPath).toURI());
            List<Map<String, String>> dataList = Files.walk(base).filter(path -> path.toFile().isFile())
                    .map(path -> Map.of(base.relativize(path).toString(), readFromAbsolutePath(path.toString())))
                    .collect(Collectors.toList());
            return dataList;
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
