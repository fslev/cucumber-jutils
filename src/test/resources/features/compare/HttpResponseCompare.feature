Feature: Compare HTTP Responses

  Scenario: Compare HTTP responses with json body
    Given param a=
    """
    {"status": 200, "body":{"1":"~[number]","true":false,"foo":"~[drink]","arr":[false,1,"s"]},"reason":"~[adjective] request", "headers":{"a":1,"b":true}}
    """
    And param b=
    """
    {"status": 200, "body":{"foo":"bar","arr":[1,"s",false,4], "1":2,"true":false, "3":2},"reason":"Bad request", "headers":{"a":1,"b":true,"c":"ok"}}
    """
    Then Compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=false, jsonArrayStrictOrder=false, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=true, jsonArrayStrictOrder=false, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]
    And COMPARE #[number] with "2"
    And COMPARE #[drink] with "bar"
    And COMPARE #[adjective] with "Bad"
    And param a=
    """
    {"status": 200, "body":{"foo":"bar","arr":[1,"s",4,false], "1":2,"true":false, "3":2},"reason":"Bad request", "headers":{"a":1,"b":true,"c":"ok"}}
    """
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=false, jsonArrayStrictOrder=true, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]

  Scenario: Compare HTTP responses with xml body
    Given param a=
    """
    {"status": 200, "body":"<chat><date>2020-~[month]-06</date><conv><from>~[from]</from><to  con=\"~[var1]\" id=\"101\">florin</to></conv></chat>","reason":"~[adjective] request", "headers":{"b":true,"a":1}}
    """
    And param b=
    """
    {"status": 200, "body":"<chat><conv><to id=\"101\" con=\"test\">florin</to><from>Sonya</from></conv><date>2020-02-06</date></chat>","reason":"Bad request", "headers":{"a":1,"b":true}}
    """
    Then Compare #[a] against #[b] via jsonNonExtensibleObject=true, jsonNonExtensibleArray=true, jsonArrayStrictOrder=true, xmlChildListLength=true, xmlChildListSequence=false, xmlElementNumAttributes=true and message=[_null]
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=false, jsonArrayStrictOrder=false, xmlChildListLength=false, xmlChildListSequence=true, xmlElementNumAttributes=false and message=[_null]
    And COMPARE #[month] with "02"
    And COMPARE #[var1] with "test"
    And COMPARE #[from] with "Sonya"
    And COMPARE #[adjective] with "Bad"

