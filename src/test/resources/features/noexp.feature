Feature: Test step definitions without cucumber expressions
  Scenario: Add two numbers
    Given number a=12
    And number b=24
    Then the sum is 36

  Scenario: Product two numbers
    Given number a=4
    And number b=2
    Then the product is 8