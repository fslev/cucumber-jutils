Feature: Match JSONs

  Scenario: Match JSONs with jsonNonExtensibleObject
    Given param a=
    """
    {"a": 1}
    """
    And param b=
    """
    {"a": 1,"b": 2}
    """
    Then Match #[a] with "#[b]" using matchConditions=["JSON_NON_EXTENSIBLE_OBJECT", "DO_NOT_MATCH"]
    Given param a=
    """
    {"b": 2,"a": 1}
    """
    Then Match #[a] with "#[b]" using matchConditions=["JSON_NON_EXTENSIBLE_OBJECT"]

  Scenario: Match JSONs with jsonNonExtensibleArray
    Given param a=
    """
    {"a": [1,2,3]}
    """
    And param b=
    """
    {"a": [1,2,3,4]}
    """
    Then Match #[a] with "#[b]" using matchConditions=["JSON_NON_EXTENSIBLE_ARRAY", "DO_NOT_MATCH"]
    Given param a=
    """
    {"a": [1,2,4,3]}
    """
    Then Match #[a] with "#[b]" using matchConditions=["JSON_NON_EXTENSIBLE_OBJECT"]

  Scenario: Match JSONs with jsonArrayStrictOrder
    Given param a=
    """
    {"a": [1,2,3]}
    """
    And param b=
    """
    {"a": [2,1,3]}
    """
    Then Match #[a] with "#[b]" using matchConditions=["JSON_STRICT_ORDER_ARRAY", "DO_NOT_MATCH"]
    Given param a=
    """
    {"a": [2,1,3]}
    """
    Then Match #[a] with "#[b]" using matchConditions=["JSON_STRICT_ORDER_ARRAY"]


  Scenario: Match JSONs with jsonNonExtensibleObject, jsonNonExtensibleArray and jsonArrayStrictOrder
    Given param a=
    """
    {"a": [1,2,3]}
    """
    And param b=
    """
    {"b":false, "a": [2,1,3,4]}
    """
    Then Match #[a] with "#[b]" using matchConditions=["JSON_NON_EXTENSIBLE_OBJECT","JSON_NON_EXTENSIBLE_ARRAY","JSON_STRICT_ORDER_ARRAY", "DO_NOT_MATCH"]
    Given param a=
    """
    {"a": [2,1,3], "b":false}
    """
    Then Match #[a] with "#[b]" using matchConditions=["JSON_NON_EXTENSIBLE_ARRAY","JSON_STRICT_ORDER_ARRAY", "DO_NOT_MATCH"]
    Given param a=
    """
    {"a": [2,1,3,4], "b":false}
    """
    Then Match #[a] with "#[b]" using matchConditions=["JSON_NON_EXTENSIBLE_OBJECT","JSON_STRICT_ORDER_ARRAY"]

  Scenario: Check unintentional regex chars at Json match
  This test scenario is valid only if logger is set to debug LEVEL or bellow
    # This should not log any warning related to regular expressions
    And Negative match {"a":"foobar"} with "{"a":"[0-9]"}"
    And Negative match {"a":"foobar"} with "{"[0-9]":"foobar"}"
    # This should log regex related warning messages
    And Negative match {"a":"[0-9]"} with "{"a":"[0-9]"}"
    And Negative match {"[0-9]":"foobar"} with "{"[0-9]":"foobar"}"

  Scenario: Match JSON with slashes against assign variable
    Given param a="{"path":"~[var]"}"
    Given param b="{"ignore":false, "path":"/tmp/n-config.export._21389211_2020-10-14T09:44:40.110821_4b501ca4-c75d-4c29-8607-c176483c8e6f.xml"}"
    Then Match #[a] with "#[b]"
    And Match \Q#[var]\E with "/tmp/n-config.export._21389211_2020-10-14T09:44:40.110821_4b501ca4-c75d-4c29-8607-c176483c8e6f.xml"
