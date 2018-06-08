Feature: Example1
#This is how background can be used to eliminate duplicate steps

  Background:
  User navigates to Facebook Given
  I am on Facebook login page

#Scenario with AND
  Scenario: 1
    When I enter username as
    """
    {"da":"1",
     "roger":"blah"
     }
    """
    And I enter password as "<status>"


