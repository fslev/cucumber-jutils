# Cucumber Core

## Summary
Cucumber-core is a Java library meant to help you write organized and clean **Cucumber** tests.  
It takes over the usual features that need to be present inside a test framework, such as:  
* the _expected-actual_ comparing mechanism
* read from from file functionality
* customized up-to-date clients to access various resources i.e databases, HTTP services, etc  
* a light state-sharing mechanism between Cucumber scenario steps       

Most of these features can be imported directly in your test project and are also available as Cucumber step definitions.  

#### Dependency
```javascript
    <dependency>
        <groupId>ro.qa.cucumber</groupId>
        <artifactId>cucumber-core</artifactId>
        <version>${version}</version>
    </dependency>
```  

## Features
### Expected-Actual compare mechanism
The following types of objects are supported for comparison:
* JSONs (String, JsonNode)  
  * dependency: [**json-compare**](https://github.com/fslev/json-compare)
    ```css
    String expected = "{\"!b\":val1\",\"a\":\"val2\"}";
    String actual = "{\"a\":\"val2\",\"c\":\"val1\"}";
    Cucumbers.compare(expected,actual);
    ```

* XMLs  
  * dependency: [**xml-unit**](https://github.com/xmlunit/xmlunit)
    ```css
    String expected = "<struct><int a=1>some .* text</int><boolean>false</boolean></struct>";
    String actual = "<struct><int a=1>some dummy text</int><boolean>false</boolean></struct>";
    Cucumbers.compare(expected,actual);
    ```      

* JSON convertible objects  
     
    ```css
    List<Map<String, Object>> expected = new ArrayList<>();
    List<Map<String, Object>> actual = new ArrayList<>();
    //To Do: Fill expected and actual
    Cucumbers.compare(expected,actual);
    ```   

* Strings, with regex support   
    ```css
    String expected = "some .* text";
    String actual = "some dummy text";
    Cucumbers.compare(expected,actual);
    ```  

* For the rest, the default *equals()* method is used  

### State-sharing mechanism
The state sharing mechanism uses the power of **guice**, **cucumber-guice** and Cucumber **custom** expressions.  
You can share state between different Cucumber steps using *scenario properties*.  

##### Setting scenario properties  
- within the Scenario, using the **param \<name\>="\<value\>"** helper step
    ```css
    Scenario: Test scenario properties
        Given param animal="r\"a$b\\"b[it"
        And param location="forest"
        And The string with scenario placeholders "The #[animal] is running through the #[location]"
        Then Check filled string equals "The r\"a$b\\"b[it is running through the forest"
    ```
- from resource file, via **load scenario props from file "\<relativePath/toFile.properties\>"**
    ```css
    Scenario: Test scenario properties
    * load scenario props from file "placeholders/scenario.properties"
    Then check #[animal]=Rabbit 
    ```
    where, *scenario.properties*:
    ```
    animal=Rabbit
    ```
    __Supported file types__ for setting scenario properties:  ***.properties***, ***.yaml*** and ***.property*** 
 