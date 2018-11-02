package ro.cucumber.core.features;

import cucumber.api.cli.Main;
import cucumber.api.guice.CucumberModules;
import ro.cucumber.core.context.config.CustomInjectorSource;
import ro.cucumber.core.engineering.utils.ResourceUtils;
import ro.cucumber.core.features.stepdefs.parameterized.FileData;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import com.google.inject.Guice;
import com.google.inject.Stage;

@RunWith(Parameterized.class)
public class ParameterizedTest {
    private String filePath;
    private String content;

    public ParameterizedTest(String filePath, String content) {
        this.filePath = filePath;
        this.content = content;
    }

    @Parameterized.Parameters(name = "{index} File {0}")
    public static Collection getParameters() {
        return ResourceUtils.readDirectory("foobar/dir").stream().map(el -> {
            Map.Entry entry = el.entrySet().iterator().next();
            Object[] objects = new Object[2];
            objects[0] = entry.getKey();
            objects[1] = entry.getValue();
            return objects;
        }).collect(Collectors.toList());
    }


    @Test
    public void runParameterizedScenario() {
        FileData fileData = Guice.createInjector(Stage.PRODUCTION, CucumberModules.SCENARIO).getInstance(FileData.class);
        fileData.setPath(filePath);
        fileData.setContent(content);
        Main.run(new String[]{"-g", "ro.cucumber.core.context.config", "-g", "ro.cucumber.core.basicstepdefs",
                "-g", "ro.cucumber.core.features.stepdefs", "src/test/resources/features/symbols.feature", "-p",
                "pretty", "-p", "html:target/cucumber-html-report", "-p", "json:target/cucumber-report/report.json"}, ParameterizedTest.class.getClassLoader());
    }
}
