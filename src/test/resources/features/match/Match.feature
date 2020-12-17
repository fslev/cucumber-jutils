Feature: Test comparator

  Scenario: Match with special characters
  This is/was a bug (https://github.com/cucumber/cucumber-jvm/issues/1881)
    Given param a="%"
    Given param b="%"
    Then Match #[a] with "#[b]"

  Scenario: Match characters with slashes against assign variable
    Given param a="~[var]"
    Given param b="/tmp/n-config.export._21389211_2020-10-14T09:44:40.110821_4b501ca4-c75d-4c29-8607-c176483c8e6f.xml"
    Then Match #[a] with "#[b]"
    And Match #[var] with "/tmp/n-config.export._21389211_2020-10-14T09:44:40.110821_4b501ca4-c75d-4c29-8607-c176483c8e6f.xml"

  Scenario: Match with new lines
    Given param a="test\n"
    And param b=
    """
    test

    """
    Then Match #[a] with "#[b]"
    When param b=
    """
    test
    some other values
    """
    Then Negative match #[a] with "#[b]"
    And Match #[a] with "#[b]" using matchConditions=["DO_NOT_MATCH"]
    When param a="test\n.*"
    Then Match #[a] with "#[b]"

  Scenario: Match simple values
    Given param a="1"
    Given param a="1"
    And param b="1"
    Then Match #[a] with "#[b]"
    And Match 1 with "1"

  Scenario: Match values with inner quotes
    Given param expected="te"st"
    Given param actual=
    """
    te"st
    """
    Then Match #[expected] with "#[actual]"

  Scenario: Match simple values negative
    Given param a="1"
    And param b="2"
    Then Negative match #[a] with "#[b]"

  Scenario: Match jsons
    Given param a="da"
    And param b="oho"
    Given param json1 =
    """
  {
    "name": "J.*n",
    "age": "\\d+",
    "cars": ["Ford", "~[car]", "Fiat"]
  }
    """
    And param json2=
    """
  {
	"name": "John",
	"age": 30,
	"cars": ["BMW","Ford","Fiat"]
  }
    """
    Then Match #[json1] with "#[json2]"
    And Match #[car] with "BMW"
    Then Match #[json1] with
    """
  {
	"name": "John",
	"age": 30,
	"cars": ["BMW","Ford","Fiat"]
  }
    """

  Scenario: Match jsons with match conditions
    Given param json1 =
    """
  {
    "name": "J.*n",
    "age": "\\d+",
    "cars": ["Ford", "~[car]", "Fiat"]
  }
    """
    And param json2=
    """
  {
	"name": "John",
	"age": 30,
	"cars": ["BMW","Ford","Fiat","Other"]
  }
    """
    Then Match #[json1] with "#[json2]" using matchConditions=["JSON_NON_EXTENSIBLE_ARRAY", "DO_NOT_MATCH"]

  Scenario: Match Jsons with escaped values
    Given param json1 =
    """
  { "b": "~[val1]" }
    """
    And param json2=
    """
  { "a": "test",
    "b": "some val\\Q he \\n re\" for test" }
    """
    And param expectedResultedJson=
    """
  { "b": "\\Q#[val1]\\E" }
    """
    Then Match #[json1] with "#[json2]"
    And Match #[expectedResultedJson] with "#[json2]"
    And Compare JSON #[expectedResultedJson] with #[json2]

  Scenario: Match jsons negative
    Given param json1 =
    """
  {
    "name": "J.*n",
    "age": "\\d+",
    "cars": ["Ford", "lol", "Fiat"]
  }
    """
    And param json2=
    """
  {
	"name": "John",
	"age": 30,
	"cars": ["BMW","Ford","Fiat"]
  }
    """
    Then Negative match #[json1] with "#[json2]"

  Scenario: Match big JSONs
    When load all scenario props from dir "props/bigJsons"
    Then Match #[expectedLargeJson] with "#[actualLargeJson]"
    Then Negative match #[expectedWrongLargeJson] with "#[actualLargeJson]"

  Scenario: Match data tables
    Given param a="replaced_value"
    And table expectedTable=
      | firstName | lastName |
      | #[a]      | travolta |
      | sam       | .*       |
      | bruce     | ~[name]  |

    Then Match #[expectedTable] with table
      | firstName      | lastName |
      | replaced_value | travolta |
      | sam            | carter   |
      | bruce          | willis   |
    And Match #[name] with "willis"

  Scenario: Match data tables with empty string and null values against JSON
    Given param a="replaced_value"
    And table expectedTable=
      | firstName | lastName |
      | #[a]      | [_blank] |
      | sam       | .*       |
      |           | ~[name]  |
    Then Match #[expectedTable] with
    """
      [
        {"firstName": "replaced_value","lastName":""},
        {"firstName": "sam","lastName":"some"},
        {"firstName": null,"lastName":"John"}
      ]
    """

  Scenario: Match empty null value data tables
    Given table empty_table=
      |  |
    Then Match #[empty_table] with table
      |  |

  Scenario: Match empty data tables
    Given table empty_table=
      | [_blank] |
    Then Match #[empty_table] with table
      | [_blank] |

  Scenario: Match lists
    Given param a="cherries"
    And param header="fruits"
    And table expectedTable1=
      | pineapples | #[a] | .* | strawberries |
    And table expectedTable2=
      | fruits       |
      | pineapples   |
      | cherries     |
      | .*           |
      | strawberries |

    Then Match #[expectedTable1] with table
      | apples | strawberries | pineapples | cherries |
    And Match #[expectedTable2] with table
      | #[header]    |
      | apples       |
      | strawberries |
      | pineapples   |
      | cherries     |

  Scenario: Match resource content containing placeholders
    Given param status="200"
    And param contentType="application/json"
    And param accept="application/json"
    And param orderType="KVM"
    And param body=
    """
    {
      "orderType": "#[orderType]"
    }
    """
    # a nice un-intended feature: #[body] is defined on multiple lines
    And param expected from file path "placeholders/expected1.json"
    Then Match #[expected] with
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
    And Match #[expected] with content from path "placeholders/actual1.json"

  Scenario: Match empty values
    Given param a=""
    Then Match #[a] with ""

  Scenario: Match XMLs
    Given param expected=
      """
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <bookingResponse>
        <bookingId>dlc:~[var1]</bookingId>
    </bookingResponse>
      """
    And param actual=
      """
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?><bookingResponse><bookingId>dlc:booking:4663740</bookingId></bookingResponse>
      """
    Then Match #[expected] with "#[actual]"
    And Match #[var1] with "booking:4663740"

  Scenario: Match XMLs from HTTP response bodies
    Given param expectedResponse=
      """
    {
      "status": 200,
      "body": "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bookingResponse><bookingId>dlc:~[var1]</bookingId></bookingResponse>"
    }
      """
    Then Create HTTP response wrapper with content <?xml version="1.0" encoding="UTF-8" standalone="yes"?><bookingResponse><bookingId>dlc:booking:4663740</bookingId></bookingResponse> and compare with #[expectedResponse]
    And Match #[var1] with "booking:4663740"

  Scenario: Check unintentional regex chars at String match
  This test scenario is valid only if logger is set to debug LEVEL or bellow
    # This should not log any warning related to regular expressions
    And Negative match abc with "[0-9]"
    # This should log regex related warning messages
    And Negative match [0-9] with "[0-9]"
    And Match \Q[0-9]\E with "[0-9]"
