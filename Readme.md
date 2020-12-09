# Cucumber JUtils

![Java CI with Maven](https://github.com/fslev/cucumber-utils/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/fslev/cucumber-utils/badge.svg?branch=master)](https://coveralls.io/github/fslev/cucumber-utils?branch=master)


Cucumber Utils has been renamed to Cucumber JUtils

# Summary
A Java library meant to help you write organized and clean **Cucumber** tests.  
It takes over the usual dependencies and features needed inside a Cucumber test framework, such as:  
* Matching mechanisms: match XMLs, JSONs, and other Java objects  
* State-sharing mechanism between _Cucumber steps_ running inside same scenario   
* Customized clients for accessing various resources, i.e _databases_, _HTTP services_, etc  
* Predefined Cucumber steps for helping with certain repetitive tasks  
* Utility classes           

#### Maven Central
```
<dependency>
  <groupId>io.github.fslev</groupId>
  <artifactId>cucumber-jutils</artifactId>
  <version>${latest.version}</version>
</dependency>

Gradle: compile("io.github.fslev:cucumber-jutils:${latest.version}")
```  

#### Included dependencies that are worth mentioning:
[**Jtest-Utils**](https://github.com/fslev/jtest-utils)

# Configuration
In order to integrate **cucumber-jutils** within your test project you must configure the following **glue** package inside your IDE Cucumber plugin or / and inside the code:
```
com.cucumber.utils
```  
# Documentation
Feature related documentation can be found [here](https://github.com/fslev/cucumber-utils/wiki)

# Tutorial
Follow the [Cucumber Utils Tutorial](https://github.com/fslev/cucumber-utils-tutorial) for a better picture on how this library should be used.  

