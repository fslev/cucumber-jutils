Feature: Variables readme

  Scenario: Test scenario variables
    * var animal="rabbit"
    * var location="forest"
    * [util] Match some rabbit with some #[animal]
    * [util] Match forest with #[location]

  Scenario: Test scenario variables set from files
    * load vars from dir "placeholders/properties/drinks"
    * [util] Match Johnny Walker with #[whisky]