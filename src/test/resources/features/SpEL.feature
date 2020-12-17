Feature: Parse SpEL expressions

  Scenario: Check simple SpEL parsing
    And param c="#{T(java.lang.String).format('%d-%d', 1, 3)}"
    And Match 1-3 with "#[c]"
    Given param a="This is a random number: #{T(Math).random()}"
    Then Match This is a random number: 0.[0-9]* with "#[a]"

  Scenario: Process and Match multiple String embedded SpELs
    * param car="Alfa Romeo Disco Volante"
    And param spel="This is expression: #{T(java.lang.String).format('%d-%d', 1, 3)} and this is another expression: #{'#[car]'.toLowerCase()} car"
    And Match This is expression: 1-3 and this is another expression: alfa romeo disco volante car with "#[spel]"

  Scenario: Process standalone SpEL
    Given param myJson="#{new com.fasterxml.jackson.databind.ObjectMapper().createObjectNode().put("a",1).put("b",2)}"
    Then Match {"b":2, "a":1} with "#[myJson]"

  Scenario: Process standalone SpEL inside docString
    Given param a=
    """
    #{new com.fasterxml.jackson.databind.ObjectMapper().createObjectNode().put("a",1).put("b",2)}
    """
    Then Match #[a] with "{"b":2, "a":1}"

  Scenario: Process SpEL from file
    * load all scenario props from dir "spel_props"
    Given param json=
     """
    {
   "a": "0.[0-9]*",
   "b": false,
   "c": "abcde"
    }
     """
    And Match #[json] with "#[expressionToBeParsed]"

  Scenario: Process SpEL from table
    * load all scenario props from dir "spel_props"
    Given table expectedTable=
      | fruits       |
      | pineapples   |
      | CHERRIES     |
      | strawberries |
    And Match #[expectedTable] with table
      | fruits                        |
      | #{'Pineapples'.toLowerCase()} |
      | #{'cherries'.toUpperCase()}   |
      | strawberries                  |