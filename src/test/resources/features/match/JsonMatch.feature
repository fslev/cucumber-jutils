Feature: Match JSONs

  Scenario: Match JSONs with jsonNonExtensibleObject
    Given var a=
    """
    {"a": 1}
    """
    And var b=
    """
    {"a": 1,"b": 2}
    """
    Then [util] Match #[a] against #[b] using matchConditions=["JSON_NON_EXTENSIBLE_OBJECT", "DO_NOT_MATCH"]
    Given var a=
    """
    {"b": 2,"a": 1}
    """
    Then [util] Match #[a] against #[b] using matchConditions=["JSON_NON_EXTENSIBLE_OBJECT"]

  Scenario: Match JSONs with jsonNonExtensibleArray
    Given var a=
    """
    {"a": [1,2,3]}
    """
    And var b=
    """
    {"a": [1,2,3,4]}
    """
    Then [util] Match #[a] against #[b] using matchConditions=["JSON_NON_EXTENSIBLE_ARRAY", "DO_NOT_MATCH"]
    Given var a=
    """
    {"a": [1,2,4,3]}
    """
    Then [util] Match #[a] against #[b] using matchConditions=["JSON_NON_EXTENSIBLE_OBJECT"]

  Scenario: Match JSONs with jsonArrayStrictOrder
    Given var a=
    """
    {"a": [1,2,3]}
    """
    And var b=
    """
    {"a": [2,1,3]}
    """
    Then [util] Match #[a] against #[b] using matchConditions=["JSON_STRICT_ORDER_ARRAY", "DO_NOT_MATCH"]
    Given var a=
    """
    {"a": [2,1,3]}
    """
    Then [util] Match #[a] against #[b] using matchConditions=["JSON_STRICT_ORDER_ARRAY"]


  Scenario: Match JSONs with jsonNonExtensibleObject, jsonNonExtensibleArray and jsonArrayStrictOrder
    Given var a=
    """
    {"a": [1,2,3]}
    """
    And var b=
    """
    {"b":false, "a": [2,1,3,4]}
    """
    Then [util] Match #[a] against #[b] using matchConditions=["JSON_NON_EXTENSIBLE_OBJECT","JSON_NON_EXTENSIBLE_ARRAY","JSON_STRICT_ORDER_ARRAY", "DO_NOT_MATCH"]
    Given var a=
    """
    {"a": [2,1,3], "b":false}
    """
    Then [util] Match #[a] against #[b] using matchConditions=["JSON_NON_EXTENSIBLE_ARRAY","JSON_STRICT_ORDER_ARRAY", "DO_NOT_MATCH"]
    Given var a=
    """
    {"a": [2,1,3,4], "b":false}
    """
    Then [util] Match #[a] against #[b] using matchConditions=["JSON_NON_EXTENSIBLE_OBJECT","JSON_STRICT_ORDER_ARRAY"]

  Scenario: Check unintentional regex chars at Json match
  This test scenario is valid only if logger is set to debug LEVEL or bellow
    # This should not log any warning related to regular expressions
    And [util] Negative match {"a":"foobar"} with {"a":"[0-9]"}
    And [util] Negative match {"a":"foobar"} with {"[0-9]":"foobar"}
    # This should log regex related warning messages
    And [util] Negative match {"a":"[0-9]"} with {"a":"[0-9]"}
    And [util] Negative match {"[0-9]":"foobar"} with {"[0-9]":"foobar"}

  Scenario: Match JSON with slashes against assign variable
    Given var a="{"path":"~[var]"}"
    Given var b="{"ignore":false, "path":"/tmp/n-config.export._21389211_2020-10-14T09:44:40.110821_4b501ca4-c75d-4c29-8607-c176483c8e6f.xml"}"
    Then [util] Match #[a] with #[b]
    And [util] Match \Q#[var]\E with /tmp/n-config.export._21389211_2020-10-14T09:44:40.110821_4b501ca4-c75d-4c29-8607-c176483c8e6f.xml


  Scenario: Match JSONs by json paths also
    * var limit="10"
    * var expected=
    """json
    {"#($..book[?(@.price <= $['expensive'])])":[{"author": "~[author1]"},{"author": "~[author2]"}]}
    """
    * var actual=
    """json
    {
    "store": {
        "book": [
            {
                "category": "reference",
                "author": "Nigel Rees",
                "title": "Sayings of the Century",
                "price": 8.95
            },
            {
                "category": "fiction",
                "author": "Evelyn Waugh",
                "title": "Sword of Honour",
                "price": 12.99
            },
            {
                "category": "fiction",
                "author": "Herman Melville",
                "title": "Moby Dick",
                "isbn": "0-553-21311-3",
                "price": 8.99
            },
            {
                "category": "fiction",
                "author": "J. R. R. Tolkien",
                "title": "The Lord of the Rings",
                "isbn": "0-395-19395-8",
                "price": 22.99
            }
        ],
        "bicycle": {
            "color": "red",
            "price": 19.95
        }
    },
    "expensive": #[limit]
}
    """
    * [util] Match #[expected] with #[actual]
    * [util] Match Nigel Rees with #[author1]
    * [util] Match Herman Melville with #[author2]

