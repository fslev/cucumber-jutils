Feature: Test examples feature

  Scenario Template: Examples with empty values
  https://github.com/cucumber/cucumber-jvm/pull/1857#issuecomment-585956043

    Given var p="<emptyValue>"
    And var x="<nullValue>"
    Then Match #[p] with ""
    And Match #[x] with "[_null]"
    And Match #[x] with NULL
    Examples:
      | emptyValue | nullValue |
      |            | [_null]   |