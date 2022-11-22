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