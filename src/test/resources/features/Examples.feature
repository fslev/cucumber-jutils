Feature: Test examples feature

  Scenario Template: Examples with empty values
  https://github.com/cucumber/cucumber-jvm/pull/1857#issuecomment-585956043

    Given var p="<emptyValue>"
    And var x="<nullValue>"
    * var empty=""
    Then [util] Match #[p] with #[empty]
    And [util] Match #[x] with [_null]
    And [util] Match #[x] against NULL
    Examples:
      | emptyValue | nullValue |
      |            | [_null]   |