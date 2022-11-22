# Cucumber JUtils <sup>[![Stand With Ukraine](https://raw.githubusercontent.com/vshymanskyy/StandWithUkraine/main/badges/StandWithUkraine.svg)](https://vshymanskyy.github.io/StandWithUkraine)</sup>

[![Maven Central](https://img.shields.io/maven-central/v/io.github.fslev/cucumber-jutils.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.fslev%22%20AND%20a:%22cucumber-jutils%22)
![Java CI with Maven](https://github.com/fslev/cucumber-jutils/workflows/Java%20CI%20with%20Maven/badge.svg?branch=main)
[![Coverage Status](https://coveralls.io/repos/github/fslev/cucumber-jutils/badge.svg?branch=main)](https://coveralls.io/github/fslev/cucumber-jutils?branch=main)


# Summary
An extension to [Cucumber for Java](https://github.com/cucumber/cucumber-jvm) based on [Cucumber Guice](https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-guice), with scenario variables support, assertion support and some pre-defined utility steps.  


_It augments your Cucumber based test framework with some powerful features, such as:_  
* [Scenario scoped variables](#scenario-vars) (available from both Gherkin and Java)
* [Assertion support](#assertion-support)  
* [SpEL support](#spel-support)
* [Predefined Cucumber steps](#predefined-steps)   
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
[Cucumber-JUtils](https://github.com/fslev/cucumber-jutils) requires the following dependencies which you must specify inside your project:  
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

#### JTest-Utils
Cucumber-JUtils is also based on [**jtest-utils**](https://github.com/fslev/jtest-utils) dependency which contains many helpful features.  

# Configuration
In order to integrate **cucumber-jutils** within your test project you must configure the following **glue** package inside your IDE Cucumber plugin or / and inside the code:
```
com.cucumber.utils
```  

# <a name="scenario-vars"></a> Scenario scoped variables
Scenario variables can be set and read inside Gherkin syntax, Java code and resource files.  
These variables are scenario scoped. Meaning, they live as long as the scenario is running and they cannot be accessed from other scenario.  


## Gherkin
Scenario variables are read using `#[` and `]` delimiters.  
_Important note:_ If you want to use scenario variables inside your step arguments, your step definition has to use [anonymous parameter types](https://github.com/cucumber/cucumber-expressions#readme).  

These variables can be set using different pre-defined steps:  

### `* var {}="{}"`
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

### `* load vars from dir "{}"`


### 

# Documentation
Feature related documentation can be found [here](https://github.com/fslev/cucumber-jutils/wiki)

# Tutorial
Follow the [Cucumber JUtils Tutorial](https://github.com/fslev/cucumber-jutils-tutorial) for a better picture on how this library should be used.  

