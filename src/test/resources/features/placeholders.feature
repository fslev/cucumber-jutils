Feature: Test placeholder fill

  Scenario: Test placeholder fill with global values
    Given The string with global placeholders "Username is #[username] and password is #[passWord]"
    Then Check filled string equals "Username is jtravolta and password is swordfish"

  Scenario: Test placeholder fill with scenario values
    Given param animal="r\"a$b\\"b[it"
    And param location="forest"
    And The string with scenario placeholders "The #[animal] is running through the #[location]"
    Then Check filled string equals "The r\"a$b\\"b[it is running through the forest"

  Scenario: Test placeholder fill with global and scenario values
    Given param animal="bear"
    Given The string with scenario placeholders "The #[animal] is attacking #[username]"
    Then Check filled string equals "The bear is attacking jtravolta"

  Scenario: Test placeholder fill with scenario values from properties file
    Given Scenario properties from file "placeholders/scenario.properties"
    Given param animal="bear"
    Given The string with scenario placeholders "The #[animal] is attacking #[a] #[b]"
    Then Check filled string equals "The bear is attacking David Jones"
