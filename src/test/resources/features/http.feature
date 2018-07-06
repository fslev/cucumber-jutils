Feature: First HTTP test
#This is how background can be used to eliminate duplicate steps

#Sample Restful HTTP scenario
  Scenario: Simple test
    #HTTP RESTful Service for test
    Given HTTP REST service at address "http://google.ro"
    And HTTP path ""
    And HTTP headers
      | header1 | header2 |
      | cool    | dea     |
    And HTTP method GET
    Then HTTP execute
    And HTTP compare response status code with 200
    And HTTP compare response body with
  """
  {
  "id":543
  }
  """
  #Raas
    Given Raas service
    Then make call


