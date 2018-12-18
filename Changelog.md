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
