Feature: Test integer

  Scenario: Test scenario params of type integer
    Given param nr=1
    And param nr2="1"
    Then COMPARE #[nr2] with "#[nr]"




