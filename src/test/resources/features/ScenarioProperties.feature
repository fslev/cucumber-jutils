Feature: Test placeholder fill

  Scenario: Test scenario prop assigned to itself
    Given param a="test"
    And param a="#[a]"
    Then COMPARE #[a] with "test"

  Scenario Outline: Test scneario property from example table
    * COMPARE <expected> with "<withProperties>"
    Examples:
      | withProperties  | expected        |
      | val-with-#[now] | val-with-[0-9]+ |

  Scenario: Test placeholder fill with global values
    Given load scenario props from file "scenario.properties"
    Given The string with global placeholders "Username is #[username] and password is #[passWord]"
    Then Check filled string equals "Username is jtravolta and password is swordfish"

  Scenario: Test placeholder fill with scenario values
    Given param animal="r\"a$b\\"b[it"
    And param location="forest"
    And The string with scenario placeholders "The #[animal] is running through the #[location]"
    Then Check filled string equals "The r\"a$b\\"b[it is running through the forest"

  Scenario: Test placeholder fill with scenario property file
    * load scenario props from file "placeholders/figure.property"
    And The string with scenario placeholders "This is a #[figure]"
    Then Check filled string equals "This is a circle"

  Scenario: Test placeholder fill with global and scenario values
    Given param animal="bear"
    And load scenario props from file "scenario.properties"
    Given The string with scenario placeholders "The #[animal] is attacking #[username]"
    Then Check filled string equals "The bear is attacking jtravolta"

  Scenario: Test placeholder fill with scenario values from properties file
    Given param lastName="Jones"
    Given load scenario props from file "placeholders/scenario.properties"
    Given param animal="bear"
    Given The string with scenario placeholders "The #[animal] is attacking #[name]"
    Then Check filled string equals "The bear is attacking David Jones"

  Scenario: Test placeholder fill from properties file and scenario params regardless of declaration order
    Given load scenario props from file "placeholders/scenario1.properties"
    Given param lastName="Rey"
    Given param enemyName="#[enemyFirstName] #[enemyLastName]"
    Given param enemyFirstName="Ben"
    Given param enemyLastName=
  """
  #[a]#[b]
  """
    Given param a="S"
    Given param b="olo"
    Given The string with scenario placeholders "#[enemyName] is attacking #[name]"
    Then Check filled string equals "Ben Solo is attacking Scavenger Rey"

  Scenario: Test placeholder fill with scenario values from yaml file
    Given load scenario props from file "placeholders/scenario.yaml"
    Given param lastName="Jones"
    Given param animal="bear"
    Given The string with scenario placeholders "The #[animal] is attacking #[name]"
    Then Check filled string equals "The bear is attacking David Jones"


  Scenario: Test placeholder fill with all scenario values recursively loaded from directory
    Given load all scenario props from dir "placeholders/properties"
    Given The string with scenario placeholders "Soda=#[soda], food=#[food], whisky=#[whisky], burger=#[burger], cheese=#[cheese] and ignore=#[ignore]"
    Then Check filled string equals "Soda=Coca-Cola, food=burger, whisky=Johnny Walker, burger=Cheeseburger, cheese=Mozzarela and ignore=#[ignore]"
