Feature: SpEL

  Scenario: Use SpEL inside Gherkin
    * var number="5"
    * var isOddNumber="#{ #[number] % 2 != 0 }"
    * [util] Match true with #[isOddNumber]

  Scenario: Use SpEL inside files
    * var content from file "features/readme/scene/some_text_with_spel.txt"
    * var number="5"
    * [util] Match Is 5 odd: true with #[content]