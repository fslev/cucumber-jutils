Feature: Common steps feature

  Scenario: Test comment steps
    * # This is a comment
    * #>> Start of section
    * #<<

  Scenario: Test wait steps
    * [util] Wait 0.147s
    * [util] Wait 0.01m