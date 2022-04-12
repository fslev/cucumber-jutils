Feature: Test comparator

  Scenario: Match with special characters
  This is/was a bug (https://github.com/cucumber/cucumber-jvm/issues/1881)
    Given var a="%"
    Given var b="%"
    Then [util] Match #[a] with #[b]

  Scenario: Match characters with slashes against assign variable
    Given var a="~[var]"
    Given var b="/tmp/n-config.export._21389211_2020-10-14T09:44:40.110821_4b501ca4-c75d-4c29-8607-c176483c8e6f.xml"
    Then [util] Match #[a] with #[b]
    And [util] Match #[var] with /tmp/n-config.export._21389211_2020-10-14T09:44:40.110821_4b501ca4-c75d-4c29-8607-c176483c8e6f.xml

  Scenario: Match with new lines
    Given var a="test\n"
    And var b=
    """
    test

    """
    Then [util] Match #[a] with #[b]
    When var b=
    """
    test
    some other values
    """
    Then [util] Negative match #[a] with #[b]
    And [util] Match #[a] against #[b] using matchConditions=["DO_NOT_MATCH"]
    When var a="test\n.*"
    Then [util] Match #[a] with #[b]

  Scenario: Match simple values
    Given var a="1"
    Given var a="1"
    And var b="1"
    Then [util] Match #[a] with #[b]
    And [util] Match 1 with 1

  Scenario: Match values with inner quotes
    Given var expected="te"st"
    Given var actual=
    """
    te"st
    """
    Then [util] Match #[expected] with #[actual]

  Scenario: Match simple values negative
    Given var a="1"
    And var b="2"
    Then [util] Negative match #[a] with #[b]

  Scenario: Match jsons
    Given var a="da"
    And var b="oho"
    Given var json1 =
    """
  {
    "name": "J.*n",
    "age": "\\d+",
    "cars": ["Ford", "~[car]", "Fiat"]
  }
    """
    And var json2=
    """
  {
	"name": "John",
	"age": 30,
	"cars": ["BMW","Ford","Fiat"]
  }
    """
    Then [util] Match #[json1] with #[json2]
    And [util] Match #[car] with BMW
    Then [util] Match #[json1] against
    """
  {
	"name": "John",
	"age": 30,
	"cars": ["BMW","Ford","Fiat"]
  }
    """

  Scenario: Match jsons with match conditions
    Given var json1 =
    """
  {
    "name": "J.*n",
    "age": "\\d+",
    "cars": ["Ford", "~[car]", "Fiat"]
  }
    """
    And var json2=
    """
  {
	"name": "John",
	"age": 30,
	"cars": ["BMW","Ford","Fiat","Other"]
  }
    """
    Then [util] Match #[json1] against #[json2] using matchConditions=["JSON_NON_EXTENSIBLE_ARRAY", "DO_NOT_MATCH"]

  Scenario: Match Jsons with escaped values
    Given var json1 =
    """
  { "b": "~[val1]" }
    """
    And var json2=
    """
  { "a": "test",
    "b": "some val\\Q he \\n re\" for test" }
    """
    And var expectedResultedJson=
    """
  { "b": "\\Q#[val1]\\E" }
    """
    Then [util] Match #[json1] with #[json2]
    And [util] Match #[expectedResultedJson] with #[json2]
    And Compare JSON #[expectedResultedJson] with #[json2]

  Scenario: Match jsons negative
    Given var json1 =
    """
  {
    "name": "J.*n",
    "age": "\\d+",
    "cars": ["Ford", "lol", "Fiat"]
  }
    """
    And var json2=
    """
  {
	"name": "John",
	"age": 30,
	"cars": ["BMW","Ford","Fiat"]
  }
    """
    Then [util] Negative match #[json1] with #[json2]

  Scenario: Match big JSONs
    When load vars from dir "props/bigJsons"
    Then [util] Match #[expectedLargeJson] with #[actualLargeJson]
    Then [util] Negative match #[expectedWrongLargeJson] with #[actualLargeJson]

  Scenario: Match data tables
    Given var a="replaced_value"
    And var expectedTable from table
      | firstName | lastName |
      | #[a]      | travolta |
      | sam       | .*       |
      | bruce     | ~[name]  |

    Then [util] Match #[expectedTable] against table
      | firstName      | lastName |
      | replaced_value | travolta |
      | sam            | carter   |
      | bruce          | willis   |
    And [util] Match #[name] with willis

  Scenario: Match data tables with empty string and null values against JSON
    Given var a="replaced_value"
    And var expectedTable from table
      | firstName | lastName |
      | #[a]      | [_blank] |
      | sam       | .*       |
      |           | ~[name]  |
    Then [util] Match #[expectedTable] against
    """
      [
        {"firstName": "replaced_value","lastName":""},
        {"firstName": "sam","lastName":"some"},
        {"firstName": null,"lastName":"John"}
      ]
    """

  Scenario: Match empty null value data tables
    Given var empty_table from table
      |  |
    Then [util] Match #[empty_table] against table
      |  |

  Scenario: Match empty data tables
    Given var empty_table from table
      | [_blank] |
    Then [util] Match #[empty_table] against table
      | [_blank] |

  Scenario: Match lists
    Given var a="cherries"
    And var header="fruits"
    And var expectedTable1 from table
      | pineapples | #[a] | .* | strawberries |
    And var expectedTable2 from table
      | fruits       |
      | pineapples   |
      | cherries     |
      | .*           |
      | strawberries |

    Then [util] Match #[expectedTable1] against table
      | apples | strawberries | pineapples | cherries |
    And [util] Match #[expectedTable2] against table
      | #[header]    |
      | apples       |
      | strawberries |
      | pineapples   |
      | cherries     |

  Scenario: Match resource content containing placeholders
    Given var status="200"
    And var contentType="application/json"
    And var accept="application/json"
    And var orderType="KVM"
    And var body=
    """
    {
      "orderType": "#[orderType]"
    }
    """
    # a nice un-intended feature: #[body] is defined on multiple lines
    And var expected from file "placeholders/expected1.json"
    Then [util] Match #[expected] against
    """
    {
      "status": #[status],
      "body": {
        "orderType": "KVM"
      },
      "headers": [{
        "Content-type": "application/json"},
        {"Accept": "application/json"}
      ]
    }
    """
    And [util] Match #[expected] against file "placeholders/actual1.json"

  Scenario: Match empty values
    Given var a=""
    Given var b=""
    Then [util] Match #[a] with #[b]

  Scenario: Match XMLs
    Given var expected=
      """
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <bookingResponse>
        <bookingId>dlc:~[var1]</bookingId>
    </bookingResponse>
      """
    And var actual=
      """
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?><bookingResponse><bookingId>dlc:booking:4663740</bookingId></bookingResponse>
      """
    Then [util] Match #[expected] with #[actual]
    And [util] Match #[var1] with booking:4663740

  Scenario: Match XMLs from HTTP response bodies
    Given var expectedResponse=
      """
    {
      "status": 200,
      "body": "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bookingResponse><bookingId>dlc:~[var1]</bookingId></bookingResponse>"
    }
      """
    Then Create HTTP response wrapper with content <?xml version="1.0" encoding="UTF-8" standalone="yes"?><bookingResponse><bookingId>dlc:booking:4663740</bookingId></bookingResponse> and compare with #[expectedResponse]
    And [util] Match #[var1] with booking:4663740

  Scenario: Check unintentional regex chars at String match
  This test scenario is valid only if logger is set to debug LEVEL or bellow
    # This should not log any warning related to regular expressions
    And [util] Negative match abc with [0-9]
    # This should log regex related warning messages
    And [util] Negative match [0-9] with [0-9]
    And [util] Match \Q[0-9]\E with [0-9]

  Scenario: Test negative match
    * Test negative match
