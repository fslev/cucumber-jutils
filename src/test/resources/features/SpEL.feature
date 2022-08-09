Feature: Parse SpEL expressions

  Scenario: Check simple SpEL parsing
    And var c="#{T(java.lang.String).format('%d-%d', 1, 3)}"
    And [util] Match 1-3 with #[c]
    Given var a="This is a random number: #{T(Math).random()}"
    Then [util] Match This is a random number: 0.[0-9]* with #[a]

  Scenario: Check SpEL parsing of values with braces
    * var var1="test}1"
    * var var2=
    """
    {"a":1}
    """
    * [util] Match #{inva}lid} with #{inva}lid}
    * [util] Match \Qva}lid\E with #{'va\}lid'}
    * [util] Match \Qva\}lid\E with #{'va\\}lid'}
    * [util] Match test}1 with #{'#[var1]'}
    * [util] Match 1 with #{T(io.json.compare.util.JsonUtils).toJson('#[var2]').get('a').asInt()}

  Scenario: Process and Match multiple String embedded SpELs
    * var car="Alfa Romeo Disco Volante"
    And var spel="This is expression: #{T(java.lang.String).format('%d-%d', 1, 3)} and this is another expression: #{'#[car]'.toLowerCase()} car"
    And [util] Match This is expression: 1-3 and this is another expression: alfa romeo disco volante car with #[spel]

  Scenario: Process standalone SpEL
    Given var myJson="#{new com.fasterxml.jackson.databind.ObjectMapper().createObjectNode().put("a",1).put("b",2)}"
    Then [util] Match {"b":2, "a":1} with #[myJson]

  Scenario: Process standalone SpEL inside docString
    Given var a=
    """
    #{new com.fasterxml.jackson.databind.ObjectMapper().createObjectNode().put("a",1).put("b",2)}
    """
    Then [util] Match #[a] with {"b":2, "a":1}

  Scenario: Process SpEL from file
    * load vars from dir "spel_props"
    Given var json=
     """
    {
   "a": "[0-9].[0-9]*",
   "b": false,
   "c": "abcde"
    }
     """
    And [util] Match #[json] with #[expressionToBeParsed]

  Scenario: Process SpEL from table
    * load vars from dir "spel_props"
    Given var expectedTable from table
      | fruits       |
      | pineapples   |
      | CHERRIES     |
      | strawberries |
    And [util] Match #[expectedTable] against table
      | fruits                        |
      | #{'Pineapples'.toLowerCase()} |
      | #{'cherries'.toUpperCase()}   |
      | strawberries                  |