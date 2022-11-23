Feature: SpEL

  Scenario: Use SpEL inside Gherkin
    * var number="5"
    * var isOddNumber="#{ #[number] % 2 != 0 }"
    * [util] Match true with #[isOddNumber]