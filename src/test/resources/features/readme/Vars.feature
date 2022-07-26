Feature: Variables readme

  Scenario: Test scenario variables
    * var animal="rabbit"
    * var location="forest"
    * [util] Match some rabbit with some #[animal]
    * [util] Match forest with #[location]

  Scenario: Test scenario variable set from doc string
    * var animal=
    """
    rabbit
    """
    * [util] Match some rabbit with some #[animal]

  Scenario: Test scenario variables set from directory
    * load vars from dir "placeholders/properties/drinks"
    * [util] Match Johnny Walker with #[whisky]
    * [util] Match Bergenbier with #[beer]
    * [util] Match ["Ursus", "Heineken"] with #[beers]

  Scenario: Test scenario variable set from file
    * var animal from file "features/readme/vars/madagascar.crt"
    * [util] Match macac with #[animal]

  Scenario: Test scenario variables set from properties file
    * load vars from file "features/readme/vars/config.properties"
    * [util] Match lioness with #[animal]
    * [util] Match Africa with #[location]

  Scenario: Test scenario variable set from table
    * var animals from table
      | feline  | marsupial       |
      | lioness | kangaroo        |
      | cougar  | tasmanian devil |
    * [util] Match [{"feline":"lioness", "marsupial":"kangaroo"}, {"feline":"cougar", "marsupial":"tasmanian devil"}] with #[animals]

  Scenario: Use scenario variables from Java and Gherkin
    * Some random step which sets some variables
    * [util] Match Cheetah with #[animal]
    * var planet="Mars"
    * Some random step which reads variables set inside Gherkin

  Scenario: Set scenario variables from file with Java
    * Read scenario variables from file

  Scenario: Parse files for scenario variables
    * var animal="wolf"
    * var location="forest"
    * Parse file for scenario variables