Feature: Disabled feature

  @Disabled
  Scenario: Test disabled
    * var x="1"

  @Ignore
  Scenario: Test ignored
    * var x="1"

  Scenario: Test enabled
    * var x="1"