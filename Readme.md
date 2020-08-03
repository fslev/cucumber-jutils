# Cucumber JUtils

Cucumber Utils has been renamed to Cucumber JUtils

# Summary
A Java library meant to help you write organized and clean **Cucumber** tests.  
It takes over the usual dependencies and features needed inside a Cucumber test framework, such as:  
* Compare mechanisms: compare XMLs, JSONs, and other Java objects  
* State-sharing mechanism between _Cucumber Scenario Steps_  
* Customized clients for accessing various resources, i.e _databases_, _HTTP services_, etc  
* Predefined Cucumber steps for easing certain repetitive tasks
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

# Configuration
In order to integrate **cucumber-jutils** within your test project you must configure the following **glue** package inside your IDE Cucumber plugin or / and inside the code:
```
com.cucumber.utils
```  
# Documentation
Feature related documentation can be found [here](https://github.com/fslev/cucumber-utils/wiki)

# Tutorial
Follow the [Cucumber Utils Tutorial](https://github.com/fslev/cucumber-utils-tutorial) for a better picture on how this library is/should be used.  

