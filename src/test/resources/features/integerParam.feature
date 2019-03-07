Feature: Test integer

  Scenario: Test scenario params of type integer
    Given param nr=1
    Then COMPARE 1 with "#[nr]"




