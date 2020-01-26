Feature: Test comparator

  Scenario: Compare simple values
    Given param a="1"
    Given param a="1"
    And param b="1"
    Then COMPARE #[a] with "#[b]"
    And COMPARE 1 with "1"

  Scenario: Compare values with inner quotes
    Given param expected="te"st"
    Given param actual=
    """
    te"st
    """
    Then COMPARE #[expected] with "#[actual]"

  Scenario: Compare simple values negative
    Given param a="1"
    And param b="2"
    Then Negative COMPARE #[a] with "#[b]"

  Scenario: Compare jsons
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
    Then COMPARE #[json1] with "#[json2]"
    And COMPARE #[car] with "BMW"
    Then COMPARE #[json1] with
    """
  {
	"name": "John",
	"age": 30,
	"cars": ["BMW","Ford","Fiat"]
  }
    """

  Scenario: Compare Jsons with escaped values
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
    Then COMPARE #[json1] with "#[json2]"
    And COMPARE #[expectedResultedJson] with "#[json2]"
    And Compare JSON #[expectedResultedJson] with #[json2]

  Scenario: Compare jsons negative
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
    Then Negative COMPARE #[json1] with "#[json2]"

  Scenario: Compare data tables
    Given param a="replaced_value"
    And table expectedTable=
      | firstName | lastName |
      | #[a]      | travolta |
      | sam       | .*       |
      | bruce     | ~[name]  |

    Then COMPARE #[expectedTable] with table
      | firstName      | lastName |
      | replaced_value | travolta |
      | sam            | carter   |
      | bruce          | willis   |
    And COMPARE #[name] with "willis"

  Scenario: Compare data tables with empty string and null values against JSON
    Given param a="replaced_value"
    And table expectedTable=
      | firstName | lastName |
      | #[a]      | [_blank] |
      | sam       | .*       |
      |           | ~[name]  |
    Then COMPARE #[expectedTable] with
    """
      [
        {"firstName": "replaced_value","lastName":""},
        {"firstName": "sam","lastName":"some"},
        {"firstName": null,"lastName":"John"}
      ]
    """

  Scenario: Compare empty null value data tables
    Given table empty_table=
      |  |
    Then COMPARE #[empty_table] with table
      |  |

  Scenario: Compare empty data tables
    Given table empty_table=
      | [_blank] |
    Then COMPARE #[empty_table] with table
      | [_blank] |

  Scenario: Compare lists
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

    Then COMPARE #[expectedTable1] with table
      | apples | strawberries | pineapples | cherries |
    And COMPARE #[expectedTable2] with table
      | #[header]    |
      | apples       |
      | strawberries |
      | pineapples   |
      | cherries     |

  Scenario: Compare resource content containing placeholders
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
    And param expected from file path "placeholders/expected1.json"
    Then COMPARE #[expected] with
    """
    {
      "status": #[status],
      "body": {
        "orderType": "KVM"
      },
      "headers": {
        "Content-type": "application/json",
        "Accept": "application/json"
      }
    }
    """
    And COMPARE #[expected] with content from path "placeholders/actual1.json"

  Scenario: Compare empty values
    Given param a=""
    Then COMPARE #[a] with ""

  Scenario: Compare XMLs
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
    Then COMPARE #[expected] with "#[actual]"
    And COMPARE #[var1] with "booking:4663740"

  Scenario: Compare XMLs from HTTP response bodies
    Given param expectedResponse=
      """
    {
      "status": 200,
      "body": "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bookingResponse><bookingId>dlc:~[var1]</bookingId></bookingResponse>"
    }
      """
    Then Create HTTP response wrapper with content <?xml version="1.0" encoding="UTF-8" standalone="yes"?><bookingResponse><bookingId>dlc:booking:4663740</bookingId></bookingResponse> and compare with #[expectedResponse]
    And COMPARE #[var1] with "booking:4663740"
