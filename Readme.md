# Cucumber JUtils <sup>[![Stand With Ukraine](https://raw.githubusercontent.com/vshymanskyy/StandWithUkraine/main/badges/StandWithUkraine.svg)](https://vshymanskyy.github.io/StandWithUkraine)</sup>

[![Maven Central](https://img.shields.io/maven-central/v/io.github.fslev/cucumber-jutils.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.fslev%22%20AND%20a:%22cucumber-jutils%22)
![Java CI with Maven](https://github.com/fslev/cucumber-jutils/workflows/Java%20CI%20with%20Maven/badge.svg?branch=main)
[![Coverage Status](https://coveralls.io/repos/github/fslev/cucumber-jutils/badge.svg?branch=main)](https://coveralls.io/github/fslev/cucumber-jutils?branch=main)


# Summary
An extension to [Cucumber for Java](https://github.com/cucumber/cucumber-jvm) based on [Cucumber Guice](https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-guice), with scenario variables support, assertion support and some pre-defined utility steps.  


It augments your Cucumber based test framework with some powerful features, such as:
* [Scenario scoped variables](#scenario-vars)
* [Assertion support](#assertion-support)  
* [SpEL support](#spel-support)
* [Predefined common steps](#predefined-steps)   
* [Utility classes](#utility-classes)           

#### Maven Central
```
<dependency>
  <groupId>io.github.fslev</groupId>
  <artifactId>cucumber-jutils</artifactId>
  <version>${latest.version}</version>
</dependency>

Gradle: compile("io.github.fslev:cucumber-jutils:${latest.version}")
```  

### Required dependencies
[Cucumber-JUtils](https://github.com/fslev/cucumber-jutils) requires the following dependencies inside your project:  
```
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

# Configuration
In order to integrate **cucumber-jutils** within your test project you must configure the following **glue** package inside your IDE Cucumber plugin or / and inside the code:
```
com.cucumber.utils
```  
# Tutorial
Follow the [Cucumber JUtils Tutorial](https://github.com/fslev/cucumber-jutils-tutorial) for a better picture on how this library should be used.

# <a name="scenario-vars"></a> 1. Scenario scoped variables
Scenario variables can be set and read inside __Gherkin__ syntax, __Java__ code and __resource__ files.  
These variables are scenario scoped. Meaning, they live as long as the scenario is running and they cannot be accessed from another scenario.  


## 1.1 Gherkin
Scenario variables are read using `#[` and `]` delimiters.  
__Important note:__ If you want to use scenario variables inside your step arguments, your step definition has to use [anonymous parameter types](https://github.com/cucumber/cucumber-expressions#readme).  

Variables can be set using different pre-defined steps:  

#### `* var <name>="<value>"`
Example:
```Gherkin
Scenario: Test scenario variables
  * var animal="rabbit"
  * var location="forest"
  * [util] Match some rabbit with some #[animal]
  * [util] Match forest with #[location]
```
As you can see bellow, the matching step is defined with anonymous parameter types:  
```javascript
@Then("[util] Match {} with {}")
public void match(Object expected, Object actual) {
```

#### `* var <varName>=<docString>`
Defines a variable with value from [doc string](https://cucumber.io/docs/gherkin/reference/):  
```gherkin
Scenario: Test scenario variable set from doc string
  * var animal=
    """
    rabbit
    """
  * [util] Match some rabbit with some #[animal]
```

#### `* var <varName> from file "<path/to/file>"`
Defines a variable with value from file content:    
> File: features/readme/vars/madagascar.crt
> > macac

```gherkin
Scenario: Test scenario variable set from file
  * var animal from file "features/readme/vars/madagascar.crt"
  * [util] Match macac with #[animal]
```

#### `* load vars from file "path/to/file"`
Loads the properties from a file, as scenario variables: `.properties`, `.yaml`, `.yml`.  
Example:
> File: features/readme/vars/config.properties
> > animal = lioness  
> > location = Africa

```gherkin
Scenario: Test scenario variables set from properties file
  * load vars from file "features/readme/vars/config.properties"
  * [util] Match lioness with #[animal]
  * [util] Match Africa with #[location]
```

#### `* load vars from dir "<path/to/directory>"`
It reads recursively the entire directory tree structure and each file becomes a scenario variable:  
_file name, without extension -> `variable name`_  
_file content -> `variable value`_  

Supported file types: `.txt`, `.text`, `.json`, `.xml`, `.html`, `.csv`  
Properties inside files: `.properties`, `.yaml`, `.yml` are also parsed as scenario variables.  

Example:  
>Directory: placeholders/properties/drinks
>>File: whisky.txt
>>>Johnny Walker

>>File: drink.yaml
>>> beer: Bergenbier  
>>> beers:  
>>> ``-`` Ursus  
>>> ``-`` Heineken   

```gherkin
Scenario: Test scenario variables set from directory
  * load vars from dir "placeholders/properties/drinks"
  * [util] Match Johnny Walker with #[whisky]
  * [util] Match Bergenbier with #[beer]
  * [util] Match ["Ursus", "Heineken"] with #[beers]
```


#### `* var <varName> from table`
Defines a variable with value from [data table](https://github.com/cucumber/cucumber-jvm/tree/main/datatable):
```gherkin
Scenario: Test scenario variable set from table
  * var animals from table
    | feline  | marsupial       |
    | lioness | kangaroo        |
    | cougar  | tasmanian devil |
  * [util] Match [{"feline":"lioness", "marsupial":"kangaroo"}, {"feline":"cougar", "marsupial":"tasmanian devil"}] with #[animals]
```

## 1.2 Java
Scenario variables can also be set and used directly inside Java code, by injecting the `ScenarioVars.class`.  
Variables defined inside Gherkin files can be used from Java code and vice versa.  

#### 1.2.1 ScenarioVars `.put()`, `.putAll()`, `.get()` and `.getAsString()`
__Set variables inside a step and use them from another step__
```javascript
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
```
```javascript
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
__Set and use variables from both Gherkin and Java:__
```Gherkin
Scenario: Use scenario variables from Java and Gherkin
  * Some random step which sets some variables
  * [util] Match Cheetah with #[animal]
  * var planet="Mars"
  * Some random step which reads variables set inside Gherkin
```
```javascript
@Inject
private ScenarioVars scenarioVars;

@Given("Some random step which reads variables set inside Gherkin")
public void readVariablesSetViaGherkin() {
    assertEquals("Mars", scenarioVars.getAsString("planet"));
}
```

#### 1.2.2 ScenarioVarsUtils `.loadScenarioVarsFromFile()` and `.loadScenarioVarsFromDir()`
Similar to the Gherkin steps, scenario variables can also be set from files:  

```javascript
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

## 1.3 Resources 
You may parse resource files for scenario variables, delimited by `#[` and `]`.
### `ScenarioVarsUtils.parse()`
> File path: features/readme/scene/some_text.txt
>> The #[animal] lives in #[location]
```gherkin
Scenario: Parse files for scenario variables
  * var animal="wolf"
  * var location="forest"
  * Parse file for scenario variables
```
```javascript
@Inject
private ScenarioVars scenarioVars;
    
@Given("Parse file for scenario variables")
public void parseFileForScenarioVars() {
    assertEquals("The wolf lives in forest", ScenarioVarsUtils.parse("features/readme/scene/some_text.txt", scenarioVars));
}
```

Play with the [Readme examples](https://github.com/fslev/cucumber-jutils/tree/main/src/test/resources/features/readme) for getting a better insight on how scenario variables work.  

# <a name="assertion-support"></a> 2. Assertion support
[Cucumber-JUtils](https://github.com/fslev/cucumber-jutils) already ships with [**JTest-Utils**](https://github.com/fslev/jtest-utils) that has some powerful assertions in terms of Objects matching.   


# <a name="spel-support"></a> 3. SpEL support
You may use [SpEL](https://docs.spring.io/spring-framework/docs/5.3.x/reference/html/core.html#expressions) expressions inside Gherkin or resource files, delimited by `#{` and `}`:
### 3.1 Gherkin
```Gherkin
Feature: SpEL

  Scenario: Use SpEL inside Gherkin
    * var number="5"
    * var isOddNumber="#{ #[number] % 2 != 0 }"
    * [util] Match true with #[isOddNumber]
```

### 3.2 Resource
[SpEL](https://docs.spring.io/spring-framework/docs/5.3.x/reference/html/core.html#expressions) expressions used inside files:  
> File path: features/readme/scene/some_text_with_spel.txt
>> "Is #[number] odd: #{ #[number] % 2 !=0 }" 


```gherkin
Scenario: Use SpEL inside files
  * var content from file "features/readme/scene/some_text_with_spel.txt"
  * var number="5"
  * [util] Match "Is 5 odd: true" with #[content]
```

__Note:__ `ScenarioVarsUtils.parse()` not only parses for scenario variables, but also for SpEL expressions:  
```javascript
@Inject
private ScenarioVars scenarioVars;
    
@Given("Parse file for SpEL")
public void parseFileForSpEL() {
    assertEquals("\"Is 5 odd: true\"", ScenarioVarsUtils.parse("features/readme/scene/some_text_with_spel.txt", scenarioVars));
}
```
```gherkin
Scenario: Parse files for SpEL
  * var number="5"
  * Parse file for SpEL
```
Play with the [Readme examples](https://github.com/fslev/cucumber-jutils/tree/main/src/test/resources/features/readme) for getting a better insight on how SpEL expressions are used.  


# <a name="predefined-steps"></a> 4. Predefined common steps

### 4.1 Matching
```Gherkin
  # throws and AssertionError 
  * [util] Match true with false
```
### 4.2 Date time
```Gherkin
  * [time-util] Check period from 2019-02-03 23:58:12+0200 to 2019-02-04 01:59:10+0300 is 1 HOURS using date time pattern yyyy-MM-dd HH:mm:ssZ

  * var currentMillis="#[now]"
  * [time-util] date var currentDate=from millis #[currentMillis] PLUS 0 YEARS with format pattern=yyyy-MM-dd
  * [time-util] date var futureDateYears=from millis #[currentMillis] PLUS 15 YEARS with format pattern=yyyy-MM-dd
  * [time-util] Check period from #[currentDate] to #[futureDateYears] is 15 YEARS using date pattern yyyy-MM-dd
```

### 4.3 Sleep
```gherkin
  * [util] Wait 10.471s
  * [util] Wait 2.5m
```

# <a name="utility-classes"></a> 5. Utility classes
- `ScenarioVars` - it stores all scenario variables. Access it via injection: `com.google.inject.Inject`
- `ScenarioVarsUtils` - it sets scenario variables and parses files for both scenario variables and SpEL expressions
- `ScenarioVarsParser` - it parses a String for scenario variables and SpEL expressions
- `ScenarioUtils` - fast access to the underlying Cucumber `Scenario.class` and used for writing:
```javascript
@Inject
private ScenarioUtils scenarioUtils;

@Given("write {}=\"{}\"")
public void writeSomething(String name, Object value) {
    scenarioUtils.log("var {} = {}", name, value);
    scenarioUtils.log("Scenario: {}", scenarioUtils.getScenario().getName());
}
```

# Tutorial
Follow the [Cucumber JUtils Tutorial](https://github.com/fslev/cucumber-jutils-tutorial) for a better picture on how this library should be used.

# Website
https://fslev.github.io/cucumber-jutils/
