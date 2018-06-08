Feature: Example2
#This is how background can be used to eliminate duplicate steps

  Background:
  User navigates to Facebook Given
  I am on Facebook login page

#Scenario with AND
  Scenario Outline: 1
    Examples:
      | a1  | a2   |
      | bah | test |

    When blah <a1> and <a2>


  Scenario: 2
    When blah2
      | b1 | b2 | b3 |
      | 1  | 2  | 3  |


