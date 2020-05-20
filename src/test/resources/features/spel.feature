Feature: Test examples feature

  Scenario: Examples with empty values
    And param a=T(Math).random()
    And param b=T(Math).random()
    And Negative COMPARE #[a] with "#[b]"
    And param c=T(java.lang.String).format('%d-%d', 1, 2)
    And COMPARE 1-2 with "#[c]"
