Feature: Compare JSONs

  Scenario: Compare JSONs with jsonNonExtensibleObject
    Given param a=
    """
    {"a": 1}
    """
    And param b=
    """
    {"a": 1,"b": 2}
    """
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=true, jsonNonExtensibleArray=false, jsonArrayStrictOrder=false and message=[_null]
    Given param a=
    """
    {"b": 2,"a": 1}
    """
    Then Compare #[a] against #[b] via jsonNonExtensibleObject=true, jsonNonExtensibleArray=false, jsonArrayStrictOrder=false and message=[_null]

  Scenario: Compare JSONs with jsonNonExtensibleArray
    Given param a=
    """
    {"a": [1,2,3]}
    """
    And param b=
    """
    {"a": [1,2,3,4]}
    """
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=true, jsonArrayStrictOrder=false and message=[_null]
    Given param a=
    """
    {"a": [1,2,4,3]}
    """
    Then Compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=true, jsonArrayStrictOrder=false and message=[_null]

  Scenario: Compare JSONs with jsonArrayStrictOrder
    Given param a=
    """
    {"a": [1,2,3]}
    """
    And param b=
    """
    {"a": [2,1,3]}
    """
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=false, jsonArrayStrictOrder=true and message=[_null]
    Given param a=
    """
    {"a": [2,1,3]}
    """
    Then Compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=false, jsonArrayStrictOrder=true and message=[_null]


  Scenario: Compare JSONs with jsonNonExtensibleObject, jsonNonExtensibleArray and jsonArrayStrictOrder
    Given param a=
    """
    {"a": [1,2,3]}
    """
    And param b=
    """
    {"b":false, "a": [2,1,3,4]}
    """
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=true, jsonNonExtensibleArray=true, jsonArrayStrictOrder=true and message=[_null]
    Given param a=
    """
    {"a": [2,1,3], "b":false}
    """
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=true, jsonNonExtensibleArray=false, jsonArrayStrictOrder=true and message=[_null]
    Given param a=
    """
    {"a": [2,1,3,4], "b":false}
    """
    Then Compare #[a] against #[b] via jsonNonExtensibleObject=true, jsonNonExtensibleArray=false, jsonArrayStrictOrder=true and message=[_null]