Feature: Test placeholder fill

  Scenario: Test scenario var assigned to itself
    Given var a="test"
    And var a="#[a]"
    Then Match #[a] with "test"

  Scenario Outline: Test scenario variable from example table
    * Match <expected> with "<withProperties>"
    Examples:
      | withProperties  | expected        |
      | val-with-#[now] | val-with-[0-9]+ |

  Scenario: Test placeholder fill with global values
    Given load vars from file "scenario.properties"
    Given The string with global placeholders "Username is #[username] and password is #[passWord]"
    Then Check filled string equals "Username is jtravolta and password is swordfish"

  Scenario: Test placeholder fill with scenario values
    Given var animal="r\"a$b\\"b[it"
    And var location="forest"
    And The string with scenario placeholders "The #[animal] is running through the #[location]"
    Then Check filled string equals "The r\"a$b\\"b[it is running through the forest"

  Scenario: Test placeholder fill with scenario variable file
    * load vars from file "placeholders/figure.text"
    And The string with scenario placeholders "This is a #[figure]"
    Then Check filled string equals "This is a circle"

  Scenario: Test scenario variable loaded from file
    * load file "placeholders/figure.text" to var "var"
    And The string with scenario placeholders "This is a #[var]"
    Then Check filled string equals "This is a circle"

  Scenario: Test placeholder fill with global and scenario values
    Given var animal="bear"
    And load vars from file "scenario.properties"
    Given The string with scenario placeholders "The #[animal] is attacking #[username]"
    Then Check filled string equals "The bear is attacking jtravolta"

  Scenario: Test placeholder fill with scenario values from properties file
    Given var lastName="Jones"
    Given load vars from file "placeholders/scenario.properties"
    Given var animal="bear"
    Given The string with scenario placeholders "The #[animal] is attacking #[name]"
    Then Check filled string equals "The bear is attacking David Jones"

  Scenario: Test placeholder fill from properties file and scenario params regardless of declaration order
    Given load vars from file "placeholders/scenario1.properties"
    Given var lastName="Rey"
    Given var enemyName="#[enemyFirstName] #[enemyLastName]"
    Given var enemyFirstName="Ben"
    Given var enemyLastName=
  """
  #[a]#[b]
  """
    Given var a="S"
    Given var b="olo"
    Given The string with scenario placeholders "#[enemyName] is attacking #[name]"
    Then Check filled string equals "Ben Solo is attacking Scavenger Rey"

  Scenario: Test placeholder fill with scenario values from yaml file
    Given load vars from file "placeholders/scenario.yaml"
    Given var lastName="Jones"
    Given var animal="bear"
    Given The string with scenario placeholders "The #[animal] is attacking #[name]"
    Then Check filled string equals "The bear is attacking David Jones"


  Scenario: Test placeholder fill with all scenario values recursively loaded from directory
    Given load vars from dir "placeholders/properties"
    Given The string with scenario placeholders "Soda=#[soda], food=#[food], whisky=#[whisky], burger=#[burger], cheese=#[cheese] and ignore=#[ignore]"
    Then Check filled string equals "Soda=Coca-Cola, food=burger, whisky=Johnny Walker, burger=Cheeseburger, cheese=Mozzarela and ignore=#[ignore]"


  Scenario: Test dynamic scenario variables
  There are cases where dynamic scenario variables (such as #[uid], #[now]) are generated with same value
  if they reside in the same file, or inside the same argument

    Given var a="unique1-#[uid]-and-unique2-#[uid]"
    When Match unique1-~[val1]-and-unique2-~[val2] with "#[a]"
    # Two different values should be generated.
    Then Negative match #[val1] with "#[val2]"

    Given load vars from file "placeholders/properties/jsonWithUids.json"
    # Same here, Two different values should be generated.
    When Match {"a":"~[val1]", "b":"~[val2]"} with "#[jsonWithUids]"
    Then Negative match #[val1] with "#[val2]"
