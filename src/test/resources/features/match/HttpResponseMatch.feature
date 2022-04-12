Feature: Match HTTP Responses

  Scenario: Match HTTP responses with json body
    Given var a=
    """
    {"status": 200, "body":{"1":"~[number]","true":false,"foo":"~[drink]","arr":[false,1,"s"]},"reason":"~[adjective] request", "headers":[{"a":1},{"b":true}]}
    """
    And var b=
    """
    {"status": 200, "body":{"foo":"bar","arr":[1,"s",false,4], "1":2,"true":false, "3":2},"reason":"Bad request", "headers":[{"a":1},{"b":true},{"c":"ok"}]}
    """
    Then Http Response Compare #[a] against #[b] with matchConditions=[] and message=[_null]
    And Poll Http Response and Compare #[a] against #[b] with matchConditions=[] and message=[_null]
    Then Http Response Compare #[a] against #[b] with matchConditions=["JSON_NON_EXTENSIBLE_ARRAY", "DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY"] and message=[_null]
    And [util] Match #[number] with 2
    And [util] Match #[drink] with bar
    And [util] Match #[adjective] with Bad
    And var a=
    """
    {"status": 200, "body":{"foo":"bar","arr":[1,"s",4,false], "1":2,"true":false, "3":2},"reason":"Bad request", "headers":[{"a":1},{"b":true},{"c":"ok"}]}
    """
    Then Http Response Compare #[a] against #[b] with matchConditions=["JSON_STRICT_ORDER_ARRAY", "DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY"] and message=[_null]

  Scenario: Negative Compare HTTP responses with json body
    Given var a=
    """
    {"status": 200, "body":{"1":"~[number]","true":false,"foo":"~[drink]","arr":[false,1,"s"]},"reason":"~[adjective] request", "headers":[{"a":1},{"b":true}]}
    """
    And var b=
    """
    {"status": 200, "body":{"foo":"bar","arr":[1,"s",false,4], "1":2,"true":true, "3":2},"reason":"Bad request", "headers":[{"a":1},{"b":true},{"c":"ok"}]}
    """
    Then Poll Http Response and Compare #[a] against #[b] with matchConditions=["DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY"] and message=[_null]
    And Http Response Compare #[a] against #[b] with matchConditions=["DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY"] and message=[_null]

  Scenario: Compare HTTP responses with xml body
    Given var a=
    """
    {"status": 200, "body":"<chat><date>2020-~[month]-06</date><conv><from>~[from]</from><to  con=\"~[var1]\" id=\"101\">florin</to></conv></chat>","reason":"~[adjective] request", "headers":[{"b":true},{"a":1}]}
    """
    And var b=
    """
    {"status": 200, "body":"<chat><conv><to id=\"101\" con=\"test\">florin</to><from>Sonya</from></conv><date>2020-02-06</date></chat>","reason":"Bad request", "headers":[{"a":1},{"b":true}]}
    """
    And Http Response Compare #[a] against #[b] with matchConditions=["JSON_STRICT_ORDER_ARRAY","JSON_NON_EXTENSIBLE_OBJECT","JSON_NON_EXTENSIBLE_ARRAY", "XML_CHILD_NODELIST_LENGTH", "XML_ELEMENT_NUM_ATTRIBUTES"] and message=[_null]
    And Http Response Compare #[a] against #[b] with matchConditions=["DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY", "XML_CHILD_NODELIST_SEQUENCE"] and message=[_null]
    And [util] Match #[month] with 02
    And [util] Match #[var1] with test
    And [util] Match #[from] with Sonya
    And [util] Match #[adjective] with Bad

