# Changelog

### 6.37-SNAPSHOT

### 6.36
Upgrade dependencies  

### 6.35
Upgrade dependencies    

### 6.34
Use Java 8 as base Java release version  

### 6.24
Refactoring  

### 6.23
Refactor polling mechanism  

### 6.22
Refactor match steps  

### 6.21
Upgrade dependencies  
Refactored and enhanced shell client   

### 6.20
Fix short-random predefined scenario property  
Add int-random predefined scenario property  

### 6.19
Upgrade dependencies  
Refactor code  
Move SpELParser to jtest-utils  

### 6.18
Refactoring  
Log loaded scenario properties  
Upgrade dependencies  

### 6.17
Upgrade Json Compare  

### 6.16
Refactor assertion error message  

### 6.15
Upgrade dependencies  

### 6.14
Upgrade jtest-utils -> HTTP response headers are Set<Map.Entry> instead of Map  

### 6.13
Refactoring  
Make object mapper static  
Upgrade dependencies  

### 6.12  
Log scenario parameters setup  
Upgrade dependencies   

### 6.11  
Upgrade jtest-utils (http client - accepts nullables)    

### 6.10
Log warning when SpEL expression is invalid  
Upgrade jtest-utils (refactored http client)  

### 6.9
Upgrade dependencies  

### 6.8
Refactor HTTP steps    
Remove redundant Cucumbers class. Use ObjectMatcher and ScenarioPropsUtils classes instead.       

### 6.7
Upgrade dependencies  

### 6.6
Migrated Cucumber functionalities to separate library jtest-utils    
Note: This version backwards incompatible  
Major versions will be incremented only when Cucumber major version is upgraded  

### 6.5
Bug fix: fix escape values while two Jsons are compared         

### 6.4
Support custom name for scenario property loaded from file      

### 6.3
Code refactoring  

### 6.2
Support for reading files from absolute paths  
Upgrade Cucumber to 6.8.1  
Upgrade other dependencies  

### 6.1
Upgrade to Cucumber 6.8.0  
Upgrade other dependencies     


### 6.0
Upgrade to Cucumber 6.x.x  
Upgrade other dependencies   
Refactoring  
- simplified 'Cucumbers.java' compare and compareHttpResponse methods   
  - use MatchConditions: MatchCondition.JSON_NON_EXTENSIBLE_OBJECT, MatchCondition.DO_NOT_MATCH, etc 
  - remove overloaded methods   
- moved 'Cucumbers.java' class to package 'com.cucumber.utils.context'   
- moved ActionUtils, ResourceUtils, JsonUtils, XmlUtils and StringFormat classes to 'com.cucumber.utils.helper' package  
- added new pre-defined step for comparing objects using Match Conditions  
- other internal refactorings  

### 5.39
Bug fix  
Use Map<String, Object> at datatable conversion type  
SQL query client returns List<Map<String, Object>> instead of List<Map<String, String>>  

### 5.38
Add SpEL support  
Enhance String parser for dynamic scenario properties  

### 5.37
Log scenario URI when test fails

### 5.36
Add date format support for scenario properties

### 5.35
Add steps for comments and collapsible bodies  
Log debug message when comparison fails due to unintentional special regex characters instead of warning  
Check if debug is enabled on logger before checking for regex  

### 5.34
Add warning message when JSON comparison fails due to unintentional special regex characters    
Add warning message when XML comparison fails due to unintentional special regex characters    

### 5.33
Fix negative compare step  

### 5.32
Increase cucumber version to 5.6.0 

### 5.31
Add warning message when String comparison fails due to unintentional special regex characters  

### 5.30
Add cookies support       

### 5.29  
Upgrade Cucumber to 5.5.0  
Upgrade other dependencies     

### 5.28  
Upgrade Cucumber to 5.4.2   

### 5.27  
Upgrade Cucumber    

### 5.26
Show suggestive message while comparing invalid HTTP Responses    

### 5.25
Bug fixes  

### 5.24
Refactor scenario properties loader from files   
Refactor and change compare mechanisms        

### 5.23
Upgrade Cucumber  
Upgrade Json Compare    

### 5.22
Overload Cucumbers.pollAndCompare  

### 5.21
Add debug logging    
Enhance comparators  
- Expose Json array strict order compare parameter  
- Expose Xml child length, order and attributes length   
Update dependencies  

### 5.20
Optimization  

### 5.19      
Add support for null strings: [_null]    

### 5.18      
Upgraded Cucumber  

### 5.17      
Add replaceProps for StringFormat    

### 5.16      
Optimize code    

### 5.15      
Refactor logging  

### 5.14    
Add helpful logging when reading scenario properties from files   

### 5.13    
Add 'add headers' support for http client    

### 5.12  
Refactor scenario status output logs   
Update to Cucumber 5.2.0  

### 5.11  
Bug fix    

### 5.10  
Refactor scenario logging  

### 5.9    
Remove .md as a scenario property file      
Bug fix  
Add Error support in case of duplicated scenario properties are recursively loaded from directory      

### 5.8    
Bug fix  
Refactoring    

### 5.7  
Refactoring       

### 5.6
Reformat scenario logging     

### 5.5
Update Cucumber version     

### 5.4
Update Cucumber version   

### 5.3
Refactored code  

### 5.2       
Update Cucumber    
Add more supported file formats for reading properties   

### 5.1       
Bug fix  

### 5.0       
---- Upgrade to Cucumber 5 ----  
  
### 4.62              
Update dependencies     

### 4.61              
Add more logs      

### 4.60              
Fix priority    

### 4.59              
Log scenario name after  

### 4.58              
Log scenario name before       

### 4.57              
Update dependencies   
Internal: Add cucumber report plugin    

### 4.56            
Update dependencies    
Add strings format to columns feature    

### 4.55            
Refactor ResourceUtils    
Update dependencies     

### 4.54          
Fix Scenario init bug     

### 4.53          
Decrease logging level   

### 4.52        
Log failed comparison mechanisms    

### 4.51        
Refactor failed comparison message  

### 4.50        
Refactor failed comparison message      

### 4.49      
Change method poller behaviour
Add Action Utilities    

### 4.48      
Increase Cucumber and HttpClient dependency versions  

### 4.47      
Increase Json Compare version  
Extract escaped JSON values into scenario properties         

### 4.46    
Add exponentialBackOff support for Cucumbers class    

### 4.45  
Add exponentialBackOff support  

### 4.44
Bug fix  

### 4.43
Bug fix    

### 4.42
Refactor scenario  utils    

### 4.41
Add scenario logger       

### 4.40
Bug fix      
Increase Cucumber version    
Update dependencies  

### 4.30    
Remove deprecated cucumber.api.* packages    

### 4.29    
Bug fix & Refactoring    

### 4.28    
Bug fix & Refactoring  

### 4.27  
Bug fix & Refactoring       

### 4.26  
Add support for assign symbols on JSON fields. Bug fix      

### 4.25  
Add support for assign symbols on JSON fields      

### 4.24
Fix anonymous parameter type conversion  
 
### 4.23
Update Cucumber version  

### 4.22
Update Cucumber version      

### 4.21
Add JMX support    

### 4.20
Update to cucumber 4.5.3     

### 4.19
Fix Method poll duration bug       

### 4.18
Update to cucumber 4.5.1     

### 4.17
Update to cucumber 4.5.0    

### 4.16
Use cucumber 4.4.0      

### 4.15
Add more logs       

### 4.14
Add support for negative compare     

### 4.13
Bug fix  
Refactoring     

### 4.12
Refactor logs        

### 4.11
Bug fix  
Refactor logs    
     
### 4.10
More logging for SQL client       

### 4.9
Re-format again assertion error msg        

### 4.8
Re-format assertion error msg      

### 4.7
Update Jackson vulnerable dependency  

### 4.6
Update dependencies     

### 4.5
Reformat comparison error messages   

### 4.4
Increment Cucumber dependencies      

### 4.3
Add logger to compare predefined steps       

### 4.2
Relax HttpClient builder to support Apache HttpClientBuilder     
Add #\[short-random\] scenario property  
  
### 4.1
Support for TestNG  

### 4.0    
Upgrade to Cucumber 4.x    

### 3.29    
Fix Java 8 backwards compatibility issue    

### 3.28    
Use Json Compare For Java 8     

### 3.27    
Compile library compatible with Java 8     

### 3.26  
Refactor Shell Client    

### 3.25  
Update Json Compare     

### 3.24  
Bug fix    

### 3.23  
Refactor SqlClient    

### 3.22  
Refactor HttpClient  

### 3.21  
Accept more file extension types   
Refactoring  

### 3.20  
Refactor http client      

### 3.19  
Fix possible bug. Consume Http Entities after each time are used    

### 3.18  
Update dependencies    

### 3.17  
Fix Http Response Listener bug       

### 3.16  
Add SHELL support     

### 3.15  
Add JSCH support   

### 3.14  
Add support to compare only dates   

### 3.13  
Add support to compare only dates          

### 3.12
Bug fix        

### 3.11
Enhance Http Responses comparison      

### 3.10
Bug fix inside ResourceUtils    

### 3.9
Refactor date time step definitions        

### 3.8
Refactor date time step definitions      

### 3.7
Add scenario params of type Integer  

### 3.6
Accept request entity for DELETE type requests  

### 3.5
Enhance message        

### 3.4
Bug fix        

### 3.3
Refactoring    

### 3.2
Refactoring  

### 3.1
Bug fix    

### 3.0
Bug fix          

### 2.30
Bug fix      

### 2.29
Bug fix      

### 2.28
Revert      

### 2.27
Bug fix       

### 2.26
Bug fix       

### 2.24
Fix possible bug         

### 2.23
Refactoring and fix bugs       

### 2.22
Add warning for failed comparison between two HTTP Responses     

### 2.20
Refactoring    

### 2.19
Update dependencies versions  

### 2.18
Change step description for loading properties from directory    

### 2.17
Remove setHeader builder method from http client    

### 2.16
Refactoring  

### 2.15
Remove useless logging  

### 2.14
Add functionality to recursively load all supported scenario properties from a given directory    
Refactor Date step definitions  
Bug fix and refactorings      

### 2.13
Refactored Date step definitions    

### 2.12
Add date helper step defs support  
Refatoring  
Bug fix   

### 2.11
Rename Cucumbers methods   

### 2.10
Add yaml support for reading scenario properties    

### 2.9.3
Increase log details  

### 2.9.2
Bug fix  

### 2.9.1
Refactoring  
Add scenario properties injection support while reading from external file    

### 2.9.0
Change default scenario properties file name to scenario.properties  

### 2.8.1
Add step definition for loading scenario props from file

### 2.8.0
Add support to ScenarioProps for loading scenario props from file   

### 2.7.0
Add support for default global variables to ScenarioProps (env.properties file)  

### 2.6.7
Add a warning when a scenario property is overridden   

### 2.6.5
JSON compare bug fixes

### 2.6.4
JSON compare bug fixes      

### 2.6.3
JSON compare bug fixes    

### 2.6.2
Add getters to HTTP client   

### 2.6.1
Poll support for SQL    
Bug fixes  

### 2.6.0
SQL client accepts null values for update    

### 2.5.9
BUG fixes  
Enhance SQL client and SQL basic step definitions  
Internal refactoring    

### 2.5.8
Add JSON pretty print  

### 2.5.7
Add simple execute SQL execute query as a basic step definition      

### 2.5.6
Refactoring      

### 2.5.5
Refactor message after failed compare      

### 2.5.4
Enhanced message after failed compare     

### 2.5.3
Fix bug in message from failed compare   

### 2.5.2
Fix bug in sql client  

### 2.5.1
Fix bug with message from failed compares  

### 2.5.0
Add support for comparing HttpResponses  
Refactor basic step definitions  
Relax {cstring} to allow empty compares  
Remove HttpResponseWrapper. Is weak while used for comparing    
Internal refactoring  

### 2.4.0
Bug fixes  
Add placeholder for curr time  
Update cucumber expressions version  
Refactoring  
Make HTTP and SQL clients extendable

### 2.3.0
Support for Postgresql and Sybase     
  
### 2.2.0
Revert to cucumber-jvm **3.0.2** due to some issues on both cucumber-jvm 4.2.0 and Intellij Idea:  
https://github.com/cucumber/cucumber-jvm/issues/1515  
https://github.com/cucumber/cucumber-jvm/issues/1514  
https://youtrack.jetbrains.com/issue/IDEA-203098  
https://youtrack.jetbrains.com/issue/IDEA-203099  
https://youtrack.jetbrains.com/issue/IDEA-203395  

Add more helper methods to Cucumbers class    
Internal refactoring  
Add logging to Mysql client    

### 2.1.0
Add more step definitions for reading and comparing contents from files  
Improve Cucumbers.compare  
Refactoring  
Bug fixes

 
### 2.0.2
Refactoring  
Add default data table converter: JacksonTableTransformer  
Add sslContext, requestRetryHandler and serviceRetryStrategy build methods  
HttpClient now supports PATCH

### 2.0.1
Add interceptor loggers to http client  
Colorize logging

### 2.0.0
Use Cucumber 4+  
Add more helpers for reading resources  
