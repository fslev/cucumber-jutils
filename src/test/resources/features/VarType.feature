Feature: Variable type feature

  Scenario: Test Object conversion to String
  By default, Cucumber-JVM passes argument values from the Gherkin syntax, as Strings.
    When var a="[1,2,3]"
    Then This is a String: #[a]

  Scenario: Test variable conversion to array of objects
    * Array of strings: ["a","b"]
    * Array of enums: ["TEST","OK"]