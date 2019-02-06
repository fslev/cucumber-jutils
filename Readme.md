# Cucumber Core

## Summary
Cucumber-core is a Java library meant to help you write organized and clean **Cucumber** tests.  
It takes over the usual features that need to be present inside a test framework, such as:  
* the _expected-actual_ comparing mechanism
* read from from file functionality
* customized up-to-date clients to access various resources i.e databases, HTTP services, etc  
* a light state-sharing mechanism between Cucumber scenario steps       

Most of these features can be imported directly in your test project and are also available as Cucumber step definitions.  

<br>
## Features
#### _Expected-Actual_ comparing mechanism
The following types of objects are supported for comparison:
* JSONs (String, JsonNode)  
  * dependency: [**json-compare**](https://github.com/fslev/json-compare)
   
          