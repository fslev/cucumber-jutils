package ro.cucumber.core.engineering.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class ResourceUtils {

    public static String read(String relativeFilePath) {
        return readFromRelativePath(relativeFilePath);
    }

    public static Properties readProps(String relativeFilePath) {
        Properties props = new Properties();
        try {
            props.load(new StringReader(read(relativeFilePath)));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return props;
    }

    public static Map<String, Object> readYaml(String relativeFilePath) {
        return new Yaml().load(Thread.currentThread().getContextClassLoader().getResourceAsStream(relativeFilePath));
    }

    /**
     * @return a Map<String,String> between corresponding file paths and file contents
     */
    public static Map<String, String> readDirectory(String relDirPath) {
        try {
            Path basePath = Paths.get(Thread.currentThread().getContextClassLoader().getResource(relDirPath).toURI());
            Map<String, String> map = Files.walk(basePath).filter(path -> path.toFile().isFile())
                    .collect(Collectors.toMap(path -> basePath.relativize(path).toString(),
                            path -> readFromRelativePath(relDirPath + File.separator + basePath.relativize(path).toString())));
            return map;
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readFromRelativePath(String filePath) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
             ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            if (is == null) {
                throw new IOException("File " + filePath + " not found");
            }
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
