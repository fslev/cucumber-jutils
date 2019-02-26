package com.cucumber.utils.engineering.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class ResourceUtils {

    public static String read(String relativeFilePath) throws IOException {
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

    public static Map<String, Object> readYaml(String relativeFilePath) throws IOException {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(relativeFilePath)) {
            if (is == null) {
                throw new IOException("File " + relativeFilePath + " not found");
            }
            return new Yaml().load(is);
        }
    }

    /**
     * @return a Map<String,String> between corresponding file paths and file contents
     */
    public static Map<String, String> readDirectory(String relativeDirPath, String... fileExtensionPatterns) throws IOException, URISyntaxException {
        URL dirURL = Thread.currentThread().getContextClassLoader().getResource(relativeDirPath);
        if (dirURL == null) {
            throw new IOException("Directory " + relativeDirPath + " not found");
        }
        Path rootPath = Paths.get(dirURL.toURI());
        if (!Files.exists(rootPath)) {
            throw new IOException("Directory " + rootPath + " not found");
        }
        Map<String, String> map = Files.walk(rootPath).filter(path
                -> path.toFile().isFile()
                && (fileExtensionPatterns.length == 0
                || Set.of(fileExtensionPatterns).contains(path.getFileName().toString().substring(path.getFileName().toString().lastIndexOf(".")))))
                .collect(Collectors.toMap(path -> relativeDirPath + File.separator + rootPath.relativize(path).toString(),
                        path -> {
                            try {
                                return readFromRelativePath(relativeDirPath + File.separator + rootPath.relativize(path).toString());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }));
        return map;
    }

    private static String readFromRelativePath(String relativeFilePath) throws IOException {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(relativeFilePath);
             ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            if (is == null) {
                throw new IOException("File " + relativeFilePath + " not found");
            }
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8.name());
        }
    }

    public static String getFileName(String relativeFilePath) throws IOException, URISyntaxException {
        Path filePath = Paths.get(Thread.currentThread().getContextClassLoader().getResource(relativeFilePath).toURI());
        if (!Files.exists(filePath)) {
            throw new IOException("File " + relativeFilePath + " not found");
        }
        return filePath.getFileName().toString();
    }
}
