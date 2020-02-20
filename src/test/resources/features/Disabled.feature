Feature: Disabled feature

  @Disabled
  Scenario: Test disabled
    * param x="1"

  @Ignore
  Scenario: Test ignored
    * param x="1"

  Scenario: Test enabled
    * param x="1"