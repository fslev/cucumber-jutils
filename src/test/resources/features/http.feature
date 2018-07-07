@zare
Feature: First HTTP test
#This is how background can be used to eliminate duplicate steps

#Sample Restful HTTP scenario
  @bleah
  Scenario: Simple test
    #HTTP RESTful Service for test
    Given HTTP REST service at address "wee"
    And HTTP path "lol"
#    And HTTP entity "wafds"
#    And HTTP path "lols"
#    And HTTP headers
#      | header1 | header2 |
#      | cool    | dea     |
    And HTTP method GET
#    Then HTTP execute
#    And HTTP compare response status code with 200
    And HTTP compare response body with
  """
  {
  "id":543
  }
  """
  #Raas
    Given RaaS service
    Then HTTP execute


