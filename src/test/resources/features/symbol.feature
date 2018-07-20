Feature: Test symbol parsers

  Scenario: Test string parser for global properties
    Given The string with global symbols "Username is ${username} and password is ${passWord}"
    Then Check parsed string equals "Username is jtravolta and password is swordfish"

  Scenario: Test string parser for scenario properties
    Given Set scenario props with key "animal" and value "rabbit"
    And Set scenario props with key "location" and value "forest"
    Given The string with scenario symbols "The #[animal] is running through the #[location]"
    Then Check parsed string equals "The rabbit is running through the forest"

  Scenario: Test string parser for global and scenario properties for docstrings
    Given Set scenario props with key "animal" and value "rabbit"
    And Set scenario props with key "location" and value "forest"
    Given The string with scenario symbols "The #[animal] is running through the #[location]"
    Then Check parsed string equals "The rabbit is running through the forest"
