# Cucumber JUtils <sup>[![Stand With Ukraine](https://raw.githubusercontent.com/vshymanskyy/StandWithUkraine/main/badges/StandWithUkraine.svg)](https://vshymanskyy.github.io/StandWithUkraine)</sup>

[![Maven Central](https://img.shields.io/maven-central/v/io.github.fslev/cucumber-jutils.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.fslev%22%20AND%20a:%22cucumber-jutils%22)
![Java CI with Maven](https://github.com/fslev/cucumber-jutils/workflows/Java%20CI%20with%20Maven/badge.svg?branch=main)
[![Coverage Status](https://coveralls.io/repos/github/fslev/cucumber-jutils/badge.svg?branch=main)](https://coveralls.io/github/fslev/cucumber-jutils?branch=main)


# Summary
An extension to [Cucumber for Java](https://github.com/cucumber/cucumber-jvm) based on [Cucumber Guice](https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-guice), with scenario variables support, assertion support and some pre-defined utility steps.  


_It takes over the usual features needed inside a Cucumber based test framework, such as:_  
* Scenario scoped variables (defined and used both inside the Gherkin document and Step definitions)
* Matching mechanisms: match XMLs, JSONs, texts and HTTP responses  
* Predefined Cucumber steps for helping with common tasks  
* Some useful utility classes           

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
Cucumber-JUtils requires the following dependencies which you must specify inside your pom.xml:  
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
# Documentation
Feature related documentation can be found [here](https://github.com/fslev/cucumber-jutils/wiki)

# Tutorial
Follow the [Cucumber JUtils Tutorial](https://github.com/fslev/cucumber-jutils-tutorial) for a better picture on how this library should be used.  

