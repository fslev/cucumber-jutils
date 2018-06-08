Feature: Zone Transfer OUT with transfer loose type
#This is how background can be used to eliminate duplicate steps

  Background:
  User navigates to Facebook Given
  I am on Facebook login page

#Scenario with AND
  Scenario Outline: Yet, this is the first test scenario.Hope it will work
    When blah <a1> and <a2>
    Examples:
      | a1  | a2 |
      | bah | k  |

  Scenario: Test scenario two, from some time. And this is just a prototype test scenario.Please ignore it
    When blah2
      | b1 | b2 | b3 |
      | 1  | 2  | 3  |

  Scenario: 3
    When save asta

