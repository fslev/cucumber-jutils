Feature: Test comparator

  Scenario: Compare simple values
    Given param a = 1
    And param b = 1
    Then compare #[a] with #[b]
    And compare 1 with 1

  Scenario: Compare jsons
    Given param json1 =
    """
  {
    "name": "J.*n",
    "age": "\\\\d+",
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
    Then compare #[json1] with #[json2]
    And compare #[car] with BMW
    Then compare #[json1] with
    """
  {
	"name": "John",
	"age": 30,
	"cars": ["BMW","Ford","Fiat"]
  }
    """

  Scenario: compare data tables
    Given param a= replaced_value
    And table expectedTable=
      | firstName | lastName |
      | #[a]      | travolta |
      | sam       | carter   |

    Then compare #[expectedTable] against table
      | firstName      | lastName |
      | replaced_value | travolta |
      | sam            | carter   |

