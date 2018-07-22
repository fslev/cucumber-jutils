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
    Given param a = test
    Then compare {cstring} against table
      | firstName | lastName |
      | #[a]      | travolta |
      | sam       | carter   |