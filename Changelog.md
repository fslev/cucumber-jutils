### 2.5.0
Remove HttpResponseWrapper. Is weak while used for comparing    

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
