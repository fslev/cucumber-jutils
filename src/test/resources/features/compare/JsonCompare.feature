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
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=true, jsonNonExtensibleArray=false, jsonArrayStrictOrder=false, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]
    Given param a=
    """
    {"b": 2,"a": 1}
    """
    Then Compare #[a] against #[b] via jsonNonExtensibleObject=true, jsonNonExtensibleArray=false, jsonArrayStrictOrder=false, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]

  Scenario: Compare JSONs with jsonNonExtensibleArray
    Given param a=
    """
    {"a": [1,2,3]}
    """
    And param b=
    """
    {"a": [1,2,3,4]}
    """
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=true, jsonArrayStrictOrder=false, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]
    Given param a=
    """
    {"a": [1,2,4,3]}
    """
    Then Compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=true, jsonArrayStrictOrder=false, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]

  Scenario: Compare JSONs with jsonArrayStrictOrder
    Given param a=
    """
    {"a": [1,2,3]}
    """
    And param b=
    """
    {"a": [2,1,3]}
    """
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=false, jsonArrayStrictOrder=true, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]
    Given param a=
    """
    {"a": [2,1,3]}
    """
    Then Compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=false, jsonArrayStrictOrder=true, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]


  Scenario: Compare JSONs with jsonNonExtensibleObject, jsonNonExtensibleArray and jsonArrayStrictOrder
    Given param a=
    """
    {"a": [1,2,3]}
    """
    And param b=
    """
    {"b":false, "a": [2,1,3,4]}
    """
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=true, jsonNonExtensibleArray=true, jsonArrayStrictOrder=true, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]
    Given param a=
    """
    {"a": [2,1,3], "b":false}
    """
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=true, jsonArrayStrictOrder=true, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]
    Given param a=
    """
    {"a": [2,1,3,4], "b":false}
    """
    Then Compare #[a] against #[b] via jsonNonExtensibleObject=true, jsonNonExtensibleArray=false, jsonArrayStrictOrder=true, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]

  Scenario: Check unintentional regex chars at Json compare
    # This should not log any warning related to regular expressions
    And Negative COMPARE {"a":"foobar"} with "{"a":"[0-9]"}"
    And Negative COMPARE {"a":"foobar"} with "{"[0-9]":"foobar"}"
    # This should log regex related warning messages
    And Negative COMPARE {"a":"[0-9]"} with "{"a":"[0-9]"}"
    And Negative COMPARE {"[0-9]":"foobar"} with "{"[0-9]":"foobar"}"