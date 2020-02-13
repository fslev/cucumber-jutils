Feature: Test examples feature

  Scenario Template: Examples with empty values
  ToDo:
  https://github.com/cucumber/cucumber-jvm/pull/1857#issuecomment-585956043
    Given param p="<emptyValue>"
    And param x="<nullValue>"
#    Then COMPARE #[p] with ""
    And COMPARE #[x] with ""
    Examples:
      | emptyValue | nullValue |
      | [_blank]   |           |