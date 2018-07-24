Feature: Test comparator

  Scenario: Compare simple values
    Given param a = 1
    And param b = 1
    Then COMPARE #[a] with #[b]
    And COMPARE 1 with 1

  Scenario: Compare jsons
    Given param json1 =
    """
  {
    "name": "J.*n",
    "age": "\\d+",
    "cars": ["Ford", "~[car]", "Fiat"]
  }
    """
    And param json2 =
    """
  {
	"name": "John",
	"age": 30,
	"cars": ["BMW","Ford","Fiat"]
  }
    """
    Then COMPARE #[json1] with #[json2]
    And COMPARE #[car] with BMW
    Then COMPARE #[json1] with
    """
  {
	"name": "John",
	"age": 30,
	"cars": ["BMW","Ford","Fiat"]
  }
    """

  Scenario: Compare data tables
    Given param a=replaced_value
    And table expectedTable=
      | firstName | lastName |
      | #[a]      | travolta |
      | sam       | .*       |
      | bruce     | ~[name]  |

    Then COMPARE #[expectedTable] against table
      | firstName      | lastName |
      | replaced_value | travolta |
      | sam            | carter   |
      | bruce          | willis   |
    And COMPARE #[name] with willis

  Scenario: Compare empty data tables
    Given table empty_table=
      |  |
    Then COMPARE #[empty_table] against table
      |  |

