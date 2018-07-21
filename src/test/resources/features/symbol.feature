Feature: Test symbol parsers

  Scenario: Test string parser for global properties
    Given The string with global symbols "Username is ${username} and password is ${passWord}"
    Then Check parsed string equals "Username is jtravolta and password is swordfish"

  Scenario: Test string parser for scenario properties
    Given param animal = rabbit
    And param location = forest
    And The string with scenario symbols "The #[animal] is running through the #[location]"
    Then Check parsed string equals "The rabbit is running through the forest"

  Scenario: Test string parser for global and scenario properties
    Given param animal = bear
    Given The string with scenario symbols "The #[animal] is attacking ${username}"
    Then Check parsed string equals "The bear is attacking jtravolta"
