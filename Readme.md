# Cucumber Utils

# Summary
A Java library meant to help you write organized and clean **Cucumber** tests.  
It takes over the usual dependencies and features needed inside a test framework, such as:  
* [**Comparing** mechanism (compare XMLs, JSONs, and Java objects)](#1-compare-mechanism)  
* [Customized **clients**](#3-customized-clients) to access various resources, i.e _databases_, _HTTP services_, etc  
* a light and powerful **state-sharing mechanism** between _Cucumber Scenario Steps_  
* other utility methods           

Most of these features can be used directly from code within your test project and are also available as _Cucumber step definitions_.    

#### Maven Central
```
<dependency>
  <groupId>io.github.fslev</groupId>
  <artifactId>cucumber-utils</artifactId>
  <version>${latest.version}</version>
</dependency>

Gradle: compile("io.github.fslev:cucumber-utils:${latest.version}")
```  
#### Included dependencies that are worth to mention:

* [**cucumber-java8**](https://github.com/cucumber/cucumber-jvm/tree/master/java8)
* [**cucumber-guice**](https://github.com/cucumber/cucumber-jvm/tree/master/guice)
* [**xml-unit**](https://github.com/xmlunit/xmlunit)
* [**json-compare**](https://github.com/fslev/json-compare)
* [**apache-http-client**](https://github.com/apache/httpcomponents-client)

# Configuration
In order to integrate **cucumber-utils** within your test project you must configure the following **glue** package inside your IDE Cucumber plugin or / and inside the code:
```
com.cucumber.utils
```  

# Features
## 1. Compare mechanism
The following types of objects are supported for complex comparison, via the _Cucumbers_ class:
* JSONs (String, JsonNode)  
  * dependency: [**json-compare**](https://github.com/fslev/json-compare)
    ```
    String expected = "{\"!b\":val1\",\"a\":\"val2\"}";
    String actual = "{\"a\":\"val2\",\"c\":\"val1\"}";
    cucumbers.compare(expected, actual); //comparison passes
    ```
* JSON convertible Java objects  
     
    ```
    List<String> expected = Arrays.asList(new String[]{"a", "b", "c", ".*"});
    List<String> actual = Arrays.asList(new String[]{"c", "a", "c", "b"});
    cucumbers.compare(expected, actual); //comparison passes
    ```   
* XMLs  
  * dependency: [**xml-unit**](https://github.com/xmlunit/xmlunit)
    ```
    String expected = "<struct><int a=1>some .* text</int><boolean>false</boolean></struct>";
    String actual = "<struct><int a=1>some dummy text</int><boolean>false</boolean></struct>";
    cucumbers.compare(expected, actual); //comparison passes
    ```      
* Strings, with regex support   
    ```
    String expected = "some .* text";
    String actual = "some dummy text";
    cucumbers.compare(expected, actual); //comparison passes
    ```  
* If the objects compared are not of any type from above, then the comparison is done via the equals() method.  

### 1.1 Poll and compare
Compare until condition is met or until timeout:
```
// Compare every 1000 millis until generated random number is 3 or until total time duration is 10s   
int expected = 3;
cucumbers.pollAndCompare(expected, 1000, 10, () -> generateRandomNumber());
```

## 2. State-sharing mechanism
The state sharing mechanism is based on **guice**, **cucumber-guice** and Cucumber [**anonymous parameter types**](https://cucumber.io/docs/cucumber/cucumber-expressions/#parameter-types).  
State is shared between different Cucumber steps inside same scenario by using *scenario properties*.  

### 2.1 How to set and use scenario properties  

- within the Cucumber Scenario, by using the **param \<name\>="\<value\>"** _Cucumber_ step
```
Scenario: Test scenario properties
  Given param animal="rabbit"
  And param location="forest"
  And the string with scenario properties "The #[animal] is running through the #[location]"
  Then check string equals "The rabbit is running through the forest"
```

As you can see, in order to use the value of a scenario property within your _Cucumber_ scenario, you must call it by its name, using the special symbols **#[ ]**.  
_Under the hood_: If your Cucumber step definition uses anonymous parameter types ***{}***, then any **#[property.name]** sequence is parsed and the corresponding value is mapped to the corresponding argument:     
```java
    @Given("the string with scenario properties \"{}\"")
    public void setString(String str) {
        this.str = str;
        //taking the example above, the str parsed value is "The rabbit is running through the forest"
    }
```

- from resource file, via **load scenario props from file "relative/path/to/file.properties"** _Cucumber_ step
           
```
Scenario: Test scenario properties
  * load scenario props from file "placeholders/scenario.properties"
  Then COMPARE #[animal] with "Rabbit" 
```
where, *scenario.properties* file contains:
```
animal=Rabbit
```  
    
***Note:***  _Supported file types_ for setting scenario properties:  
* ***.properties***
* ***.yaml***
* ***.property***
* ***.json***
* ***.xml***
* ***.txt***  
    
If a scenario property is read from a **.property**, **.json**, **.xml** or **.txt** file, then the name of the scenario property will actually be the name of the file, without extension:  
```
Scenario: Test placeholder fill with scenario property file
  * load scenario props from file "placeholders/animal.property"
  Then COMPARE #[animal] with "Rabbit"
```  
where, *animal.property* file contains:
```
Rabbit
```  

- from resource directory, via **load all scenario props from dir "relative/path/to/dir"** Cucumber step           
```
* load all scenario props from dir "placeholders/properties"
Given The string with scenario placeholders "Soda=#[soda], food=#[food], whisky=#[whisky], burger=#[burger] and cheese=#[cheese]"
Then Check filled string equals "Soda=Coca-Cola, food=burger, whisky=Johnny Walker, burger=Cheeseburger and cheese=Mozzarela" 
```
where, inside the *properties* directory are defined several files containing the corresponding properties.    
    
***Note:***  
    The function for reading scenario properties from a directory walks through the whole directory tree structure. It filters only the supported file types.  
    
- programmatically, by injecting the a *ScenarioProps* class instance via **Guice**:    
```java
    import com.google.inject.Inject;

    @ScenarioScoped
    public class ParamSteps {
        @Inject
        private ScenarioProps scenarioProps;
        public void setProp(String name, String value) {
            scenarioProps.put(name, value);
        }
    }
```
     
- Another way to set scenario properties is from the compare mechanism:  
Suppose you want to extract a value from the JSON response received after calling the API of an application.  
In order to do that, you must compare the actual response with an expected one, but inside the expected value you must introduce a special placeholder **~[property.name]**.  

Example:  

```
Scenario: Test scenario properties
  When invoke HTTP API Create user    
  Then check response body="{"id":"~[userId]"}" 
```  
If comparison passes, then a new scenario property will be set, having the name ***userId*** and the value matched from the HTTP response.  
This new scenario property can be used further inside your test scenario:  

```
When invoke HTTP API Get user with id = #[userId]
Then check HTTP response status = 200
```
     
## 3. Customized clients

The following clients are available via Cucumber-Utils:
- HTTP client
- SQL clients (MySQL, PostgreSQL, Sybase, etc -> depending on the sql driver you configure)
- Shell and Jsch clients

These clients are initialized and configured via the builder pattern, in order to allow construction of new settings between Cucumber steps.      
    
### HTTP Client
Example:  
```java
HttpClient client = new HttpClient.Builder()
                .address("http://example.com")
                .path("/user/")
                .method(Method.POST)
                .addHeader("auth", "authCode")
                .addQueryParam("host","google.ro")
                .entity("{\"a\":\"some json value\"}")
                .build();
HttpResponse response = client.execute();
String responseAsString = EntityUtils.toString(response.getEntity());
```

## 4. Pre-defined Cucumber utility steps 

- Compare date times
```
Given DateTime pattern="yyyy-MM-dd HH:mm:ss"
Then DateTime check period from "2018-02-03 01:00:00" to "2019-02-03 01:00:00" is 1year
And DateTime check period from "2018-02-03 01:00:00" to "2019-02-02 12:01:10" is 364days
And DateTime check period from "2019-02-03 01:02:12" to "2019-02-03 23:59:10" is 22hours
And DateTime check period from "2019-02-03 22:02:12" to "2019-02-03 23:59:10" is 116minutes
And DateTime check period from "2019-02-03 23:58:12" to "2019-02-03 23:59:10" is 58seconds
``` 
    
- Connect to SQL databases, execute queries, compare results and also execute updates
```
Scenario: Test MYSQL client select
  Given SQL data source from file path "config/database/mysql.properties"
  Then SQL execute query "select * from gift order by person_id asc limit 3" and compare result with
          | person_id | gift                                                 |
          | .*        | .*                                                   |
          | 21189037  | fun & joy for everybody!                             |
          | 21193939  | Leica M9-P Hermes Edition: http://vimeo.com/42108675 |
``` 
```
Scenario: Test POSTGRESQL client simple insert with tabular data
  Given SQL data source from file path "config/database/psql.properties"
  Then SQL INSERT into table "mag" the following data
          | person_id | description       |
          | 14        | http://heheheh.ro |
          | 16        | null              |
          | 17        | wow               |
```
- Execute shell / bash  commands:
```
* SHELL execute command "ls -alh" and check response=".*"
``` 
- Connect via SSH to a remote server and execute bash commands:
```
Given JSCH connection from properties file "config/jsch/jsch.properties"
Then JSCH execute command "hostname -f" and check response="vm-test\d+.sandbox.lan"
```
where _jsch.properties_ contains:
```
host=vm-test1.sandbox.lan 
port=22 
user=tanja 
password=*****
privateKey=/home/tanja/.ssh/id_rsa
```