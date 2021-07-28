# Cucumber JUtils

[![Maven Central](https://img.shields.io/maven-central/v/io.github.fslev/cucumber-jutils.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.fslev%22%20AND%20a:%22cucumber-jutils%22)
![Java CI with Maven](https://github.com/fslev/cucumber-utils/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/fslev/cucumber-utils/badge.svg?branch=master)](https://coveralls.io/github/fslev/cucumber-utils?branch=master)


# Summary
A Java library meant to help you write organized and clean **Cucumber** tests.  
_It takes over the usual dependencies and features needed inside a Cucumber test framework, such as:_  
* Matching mechanisms: match XMLs, JSONs, and other types of objects  
* Scenario scoped variables (defined and used both inside the Gherkin document and Step definitions)    
* Customized clients for accessing various resources, i.e _databases_, _HTTP services_, etc  
* Predefined Cucumber steps for helping common tasks  
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
Cucumber-JUtils includes [**jtest-utils**](https://github.com/fslev/jtest-utils) dependency which contains many helpful features.  

# Configuration
In order to integrate **cucumber-jutils** within your test project you must configure the following **glue** package inside your IDE Cucumber plugin or / and inside the code:
```
com.cucumber.utils
```  
# Documentation
Feature related documentation can be found [here](https://github.com/fslev/cucumber-utils/wiki)

# Tutorial
Follow the [Cucumber Utils Tutorial](https://github.com/fslev/cucumber-utils-tutorial) for a better picture on how this library should be used.  

