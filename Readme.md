# Cucumber JUtils <sup>[![Stand With Ukraine](https://raw.githubusercontent.com/vshymanskyy/StandWithUkraine/main/badges/StandWithUkraine.svg)](https://vshymanskyy.github.io/StandWithUkraine)</sup>

[![Maven Central](https://img.shields.io/maven-central/v/io.github.fslev/cucumber-jutils.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.fslev%22%20AND%20a:%22cucumber-jutils%22)
![Java CI with Maven](https://github.com/fslev/cucumber-jutils/actions/workflows/maven.yml/badge.svg?branch=main)
[![Coverage Status](https://coveralls.io/repos/github/fslev/cucumber-jutils/badge.svg?branch=main)](https://coveralls.io/github/fslev/cucumber-jutils?branch=main)

A Cucumber-JVM extension built on [Cucumber-Guice](https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-guice). It adds scenario-scoped variables, SpEL templating, fluent assertion steps, and Cucumber parameter-type integration that resolves placeholders inside step args, doc strings, and data tables.

## Features

- **Scenario-scoped variables** — set/read from Gherkin, Java, or files; share between steps within one scenario
- **File-based loading** — `.properties` / `.yaml` / `.json` / `.xml` / `.txt` / `.csv` / `.html` / `.text`
- **JSON Pointer access** — `#[var/path/to/value]` reaches into JSON-typed variables
- **SpEL templating** — `#{ … }` evaluates Spring Expression Language inside Gherkin and resource files
- **Predefined steps** — `[util] Match …`, `[time-util] Check period …`, `[util] Wait …s`
- **Transparent placeholder resolution** — `#[var]` and `#{spel}` are auto-resolved in step args, doc strings, and data tables
- **Built-in object matching** — assertion steps delegate to [JTest-Utils](https://github.com/fslev/jtest-utils) `ObjectMatcher`

## Install

```xml
<dependency>
    <groupId>io.github.fslev</groupId>
    <artifactId>cucumber-jutils</artifactId>
    <version>${latest.version}</version>
</dependency>
```

The library declares Cucumber, Cucumber-Guice, and Guice as `provided`, so your project must pull them in:

```xml
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-java</artifactId>
    <version>${cucumber.version}</version>
</dependency>
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-guice</artifactId>
    <version>${cucumber.version}</version>
</dependency>
<dependency>
    <groupId>com.google.inject</groupId>
    <artifactId>guice</artifactId>
    <version>${guice.version}</version>
</dependency>
```

## Requirements

- Java 17+
- Cucumber-JVM 6.10.0+
- Guice 4.2.1+

## Configure Cucumber glue

Add `com.cucumber.utils` to your Cucumber glue path so that the predefined steps and parameter-type transformer are picked up:

```java
@CucumberOptions(features = "src/test/resources/features",
        glue = {"com.cucumber.utils", "your.own.steps"})
```

## Usage

### Scenario variables in Gherkin

`#[name]` reads a variable. Steps that accept variable substitution must use Cucumber [anonymous parameter types](https://github.com/cucumber/cucumber-expressions#readme) (`{}`).

```gherkin
Scenario: Set and read scenario variables
  * var animal="rabbit"
  * var location="forest"
  * [util] Match some rabbit with some #[animal]
  * [util] Match forest with #[location]
```

### Set variable from a doc string

```gherkin
Scenario: Variable from doc string
  * var animal=
    """
    rabbit
    """
  * [util] Match some rabbit with some #[animal]
```

### Set variable from a file

```gherkin
Scenario: Variable from file
  * var animal from file "features/readme/vars/madagascar.crt"
  * [util] Match macac with #[animal]
```

### Set variable from a data table

```gherkin
Scenario: Variable from data table
  * var animals from table
    | feline  | marsupial       |
    | lioness | kangaroo        |
    | cougar  | tasmanian devil |
  * [util] Match [{"feline":"lioness", "marsupial":"kangaroo"}, {"feline":"cougar", "marsupial":"tasmanian devil"}] with #[animals]
```

### Load variables from a properties or YAML file

`load vars from file` parses `.properties`, `.yaml`, and `.yml` and stores each entry as a scenario variable.

```gherkin
Scenario: Load variables from properties file
  * load vars from file "features/readme/vars/config.properties"
  * [util] Match lioness with #[animal]
  * [util] Match Africa with #[location]
```

### Load variables from a directory

`load vars from dir` walks a directory recursively. Each `.properties`/`.yaml`/`.yml` is flattened into one variable per key; each `.txt`/`.json`/`.xml`/`.csv`/`.html`/`.text` becomes a single variable named after the file (without extension).

Given the directory:

```
placeholders/properties/drinks/
├── drink.yaml          # beer: Bergenbier
│                       # beers:
│                       #   - Ursus
│                       #   - Heineken
└── whisky.txt          # Johnny Walker
```

```gherkin
Scenario: Load variables from directory tree
  * load vars from dir "placeholders/properties/drinks"
  * [util] Match Johnny Walker with #[whisky]
  * [util] Match Bergenbier with #[beer]
  * [util] Match ["Ursus", "Heineken"] with #[beers]
```

`whisky.txt` becomes `#[whisky]` (filename without extension); `drink.yaml` is flattened so that `beer` and `beers` become top-level scenario variables.

### Scenario variables in Java

Inject `ScenarioVars`. Variables set in Java are visible from Gherkin and vice versa:

```java
@ScenarioScoped
public class ScenarioVarsReadmeSteps {

    @Inject
    private ScenarioVars scenarioVars;

    @Given("Some random step which sets some variables")
    public void setVariables() {
        scenarioVars.put("animal", "Cheetah");
        Map<String, Object> vars = new HashMap<>();
        vars.put("figure", "triangle");
        vars.put("number", 10);
        scenarioVars.putAll(vars);
    }
}

@ScenarioScoped
public class ScenarioVarsAnotherReadmeSteps {

    @Inject
    private ScenarioVars scenarioVars;

    @Given("Some random step which reads the variables")
    public void readVariables() {
        assertEquals("Cheetah", scenarioVars.getAsString("animal"));
        assertEquals("triangle", scenarioVars.getAsString("figure"));
        assertEquals(10, scenarioVars.get("number"));
    }
}
```

### Load variables from files with `ScenarioVarsUtils`

```java
@Inject
private ScenarioVars scenarioVars;

@Given("Read scenario variables from file")
public void setVariablesFromFile() {
    ScenarioVarsUtils.loadScenarioVarsFromFile("features/readme/vars/config.properties", scenarioVars);
    ScenarioVarsUtils.loadScenarioVarsFromDir("placeholders/properties/drinks", scenarioVars);
    assertEquals("Africa", scenarioVars.get("location"));
    assertEquals("Johnny Walker", scenarioVars.get("whisky"));
}
```

### Parse a resource file for variables and SpEL

`ScenarioVarsUtils.parse` reads a file and substitutes both `#[var]` and `#{spel}`. The variables it resolves are whatever has been put into `ScenarioVars` earlier in the scenario — set from Gherkin, from another step, or loaded from a file.

> File: `features/readme/scene/some_text.txt`
> ```
> The #[animal] lives in #[location]
> ```

```gherkin
Scenario: Parse files for scenario variables
  * var animal="wolf"
  * var location="forest"
  * Parse file for scenario variables
```

```java
@Inject
private ScenarioVars scenarioVars;

@Given("Parse file for scenario variables")
public void parseFileForScenarioVars() {
    // animal="wolf" and location="forest" were set in the Gherkin steps above,
    // so #[animal] resolves to "wolf" and #[location] resolves to "forest".
    assertEquals("The wolf lives in forest",
            ScenarioVarsUtils.parse("features/readme/scene/some_text.txt", scenarioVars));
}
```

### JSON Pointer access into JSON-typed variables

For variables holding JSON, address inner values with `/` paths (Jackson [JSON Pointer](https://datatracker.ietf.org/doc/html/rfc6901) style):

```gherkin
Scenario: Path variables
  * var x=
    """json
    {"book":{"details":{"title":"Moby Dick"}}}
    """
  * [util] Match #[x/book/details/title] with Moby Dick
```

Works for both `String`-typed JSON values and `Map`/`List` values:

```java
ScenarioVars vars = new ScenarioVars();
vars.put("var1", Map.of("a1", true, "a2", Map.of("a21", List.of("value1", 2, true))));
assertTrue(vars.get("var1/a2") instanceof ObjectNode);
assertEquals(new IntNode(2), vars.get("var1/a2/a21/1"));
```

### Dynamic built-in variables

`ScenarioVars.get(name)` recognises four reserved keys that compute a value at read time:

| Name           | Returns                                                   |
|----------------|-----------------------------------------------------------|
| `uid`          | `String` — random UUID                                    |
| `now`          | `Long` — `System.currentTimeMillis()`                     |
| `short-random` | `Integer` — `0..Short.MAX_VALUE`                          |
| `int-random`   | `Integer` — `0..Integer.MAX_VALUE`                        |

```gherkin
* var requestId="#[uid]"
* var ts="#[now]"
```

### SpEL expressions

`#{ … }` evaluates [Spring Expression Language](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#expressions). Variables are substituted first, then SpEL is evaluated:

```gherkin
Scenario: SpEL inside Gherkin
  * var number="5"
  * var isOddNumber="#{ #[number] % 2 != 0 }"
  * [util] Match true with #[isOddNumber]
```

SpEL works inside files too. If a file contains both placeholders and SpEL, `ScenarioVarsUtils.parse` resolves both:

> File: `features/readme/scene/some_text_with_spel.txt`
> ```
> "Is #[number] odd: #{ #[number] % 2 != 0 }"
> ```

```gherkin
Scenario: SpEL inside files
  * var content from file "features/readme/scene/some_text_with_spel.txt"
  * var number="5"
  * [util] Match "Is 5 odd: true" with #[content]
```

### Predefined assertion steps

```gherkin
# Equality (compares JSON, XML, regex, primitives — all via JTest-Utils ObjectMatcher)
* [util] Match true with true
* [util] Match {"a":1} with {"a":1, "b":2} using matchConditions=JSON_NON_EXTENSIBLE_OBJECT
* [util] Match expected against
  """
  some doc-string content
  """
* [util] Match expected against file "path/to/file.json"
* [util] Match expected against table
  | k1 | k2 |
  | v1 | v2 |
* [util] Match someValue against NULL

# Inverse — assertion fails if the values match
* [util] Negative match a with b
```

### Predefined date / time steps

```gherkin
Scenario: Date arithmetic and period checks
  * var currentMillis="#[now]"
  * [time-util] date var currentDate=from millis #[currentMillis] PLUS 0 YEARS with format pattern=yyyy-MM-dd
  * [time-util] date var futureDate=from millis #[currentMillis] PLUS 15 YEARS with format pattern=yyyy-MM-dd
  * [time-util] Check period from #[currentDate] to #[futureDate] is 15 YEARS using date pattern yyyy-MM-dd
  * [time-util] Check period from 2019-02-03 23:58:12+0200 to 2019-02-04 01:59:10+0300 is 1 HOURS using date time pattern yyyy-MM-dd HH:mm:ssZ
  * [time-util] date millis var ts=from date 2021-03-07 18:44:27.345+0000 PLUS 1 HOURS with format pattern=yyyy-MM-dd HH:mm:ss.SSSZ
  * [util] Match #[ts] with 1615146267345
```

Negative variants (`doesn't match`) and `MINUS` are supported:

```gherkin
* [time-util] Check period from 2020-02-03 to 2021-02-03 doesn't match 36 DAYS using date pattern yyyy-MM-dd
* [time-util] date var pastDate=from millis #[currentMillis] MINUS 31 DAYS with format pattern=yyyy-MM-dd
```

### Sleep

```gherkin
* [util] Wait 10.471s
* [util] Wait 2.5m
```

### Parameter-type integration (transparent)

`com.cucumber.utils.context.config.ParameterTypesConfig` registers a default Cucumber parameter transformer that runs every step argument, doc string, and data table cell through `ScenarioVarsParser` — first `#[var]` substitution, then `#{spel}` evaluation. After substitution, if the parameter type is not `Object`, the value is JSON-deserialized via Jackson, falling back to reflective conversion when it isn't valid JSON.

Two reserved literals are recognised in step args:

| Literal     | Becomes                  |
|-------------|--------------------------|
| `[_null]`   | `null`                   |
| `[_blank]`  | `""` (empty string)      |

## Utility classes

| Class                                                         | What you call                                                                                              |
|---------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| `ScenarioVars`                                                | `get`, `getAsString`, `put`, `putAll`, `nameSet`, `containsVariable`, `size`                               |
| `ScenarioVarsUtils`                                           | `parse(filePath, vars)`, `loadScenarioVarsFromFile`, `loadScenarioVarsFromDir`, `loadFileAsScenarioVariable` |
| `ScenarioVarsParser`                                          | `parse(source, vars)` — runs substitution + SpEL on a string                                              |
| `ScenarioUtils`                                               | `log(msg, args...)`, `getScenario()` — write to the active Cucumber scenario report                       |

```java
@Inject
private ScenarioUtils scenarioUtils;

@Given("write {}=\"{}\"")
public void writeSomething(String name, Object value) {
    scenarioUtils.log("var {} = {}", name, value);
    scenarioUtils.log("Scenario: {}", scenarioUtils.getScenario().getName());
}
```

## Tutorial

The [Cucumber JUtils Tutorial](https://github.com/fslev/cucumber-jutils-tutorial) walks through a full project setup.

## License

[Apache License 2.0](LICENSE)
