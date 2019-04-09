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
