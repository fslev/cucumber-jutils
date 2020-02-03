@Ignore
Feature: Polling for results

  Scenario: Guess the number
    Given interval limits 3 and 9
    Then poll until random generated number 8 is found

  Scenario: Polling with timeout 0 seconds
    Then poll 0s until 0=0