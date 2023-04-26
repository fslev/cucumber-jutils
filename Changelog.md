# Changelog

## 9.12-SNAPSHOT

## 9.11 (2023-04-26)
  -  Update dependencies (jtest-utils 5.7, spring-expressions:5.3.27)

## 9.10 (2023-01-10)
- #### Changed
  - Code refactoring
  - Update dependencies (jtest-utils 5.6)

## 9.9 (2022-12-31)
- #### Changed
  - Update dependencies (jtest-utils 5.5)
  - Code refactoring

## 9.8 (2022-12-22)
- #### Changed
  - Refactoring - optimized imports
  - Update dependencies (JTest-Utils 5.4) 
  
## 9.7 (2022-11-27)
- #### Changed
  - Update dependencies (JTest-Utils 5.2)

## 9.6 (2022-11-24)
- #### Changed
  - Update dependencies (JTest-Utils 5.1)

## 9.5 (2022-11-19)
- #### Changed
  - Update [jtest-utils](https://github.com/fslev/jtest-utils) to next major version (SpEL parser was removed)  
    - Moved [SpEL](https://docs.spring.io/spring-framework/docs/5.3.x/reference/html/core.html#expressions) logic here  

## 9.4 (2022-11-18)
- #### Changed
  - Simplify ScenarioVarsParser. SpEL expressions are now parsed using Spring SpelExpressionParser  
  - Update dependencies  

## 9.3 (2022-11-14)
- #### Changed
  - Update jtest-utils to next major version release:
    - Object matching is case-sensitive  
  - Scenario variables parser is case-sensitive  

## 9.2 (2022-11-11)
- #### Changed
  - Updated dependencies
    - _(json-compare & jtest-utils enhancements)_
    - Jackson dependencies update  

## 9.1 (2022-10-31)
- #### Changed
  - Updated dependencies _(json-compare enhancements)_

## 9.0 (2022-10-16)
- #### Removed
  - Unnecessary logs were removed
  - HTTP, SQL, JSCH and Shell client support was removed. Test frameworks should decide which clients should use  
- #### Changed
  - Dependencies update  