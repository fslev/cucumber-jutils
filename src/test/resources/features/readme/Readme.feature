Feature: README examples
  Every Gherkin example from Readme.md, kept here so the README stays correct.
  Scenarios named "README check" verify claims the README makes in prose.

  # 3. Quickstart
  Scenario: Variables, SpEL and matching
    # Set two variables. #[uid] is a built-in that generates a random UUID
    * var user="Ann"
    * var id="#[uid]"

    # Build a value with SpEL: #[user] is replaced first, then #{ ... } is evaluated
    * var greeting="#{ 'Hello, #[user]'.toUpperCase() }"
    # Passes: greeting is "HELLO, ANN"
    * [util] Match HELLO, ANN with #[greeting]

    # Store a JSON document, as one of your own steps would after calling a service
    * var response=
      """
      {"id": "#[id]", "user": "Ann", "role": "admin", "age": 31}
      """

    # Passes: "A.*" is a regex, the extra field "role" is ignored,
    # and ~[age] captures the actual age into a new variable
    * [util] Match {"id": "#[id]", "user": "A.*", "age": "~[age]"} with #[response]
    # Passes: the captured age is 31
    * [util] Match 31 with #[age]

  # 4.1 Setting variables
  Scenario: Set variables
    # From a value
    * var animal="rabbit"
    # Passes: animal is "rabbit"
    * [util] Match rabbit with #[animal]

    # From a data table: a list with one map per row
    * var animals from table
      | feline  | marsupial |
      | lioness | kangaroo  |
    # Passes: animals holds the row, keyed by header
    * [util] Match [{"feline": "lioness", "marsupial": "kangaroo"}] with #[animals]

    # From a directory: drink.yaml contains "beer: Bergenbier", whisky.txt contains "Johnny Walker"
    * load vars from dir "placeholders/properties/drinks"
    # Passes: the YAML key "beer" became a variable
    * [util] Match Bergenbier with #[beer]
    # Passes: whisky.txt became a variable named after the file
    * [util] Match Johnny Walker with #[whisky]

  # 4.2 Reading variables
  Scenario: Read nested values
    * var book=
      """
      {"title": "Moby Dick", "tags": ["novel", "sea"]}
      """

    # Passes: book/title is the "title" field
    * [util] Match Moby Dick with #[book/title]
    # Passes: book/tags/1 is the second element of "tags"
    * [util] Match sea with #[book/tags/1]

  Scenario: Refer to variables defined later
    # firstName and lastName aren't set yet, so author holds the text "#[firstName] #[lastName]"
    * var author="#[firstName] #[lastName]"

    * var firstName="Herman"
    * var lastName="Melville"

    # Passes: author is resolved when it is read, and both names are set by now
    * [util] Match Herman Melville with #[author]

  # 4.3 From Java
  Scenario: README check - ScenarioVars from Java
    * var location="forest"
    * the story is told
    * [util] Match wolf with #[animal]

  # 5. SpEL
  Scenario: SpEL
    * var number="5"

    # #[number] is replaced first, giving #{ 5 % 2 != 0 }, which evaluates to true
    * var isOdd="#{ #[number] % 2 != 0 }"
    # Passes: isOdd is true
    * [util] Match true with #[isOdd]

    # Call Java: format the number with three digits
    * var code="#{ T(java.lang.String).format('%03d', #[number]) }"
    # Passes: code is "005"
    * [util] Match 005 with #[code]

  # 6. Assertion steps
  Scenario: Match and capture
    * var actual=
      """
      {"name": "John", "age": 30, "cars": ["BMW", "Ford", "Fiat"], "owner": {"name": "Ann"}}
      """

    # Passes: "J.*n" matches "John", "age" isn't checked, ~[car] captures "BMW",
    # and ~[owner] captures the whole object {"name": "Ann"}
    * [util] Match {"name": "J.*n", "cars": ["Ford", "~[car]", "Fiat"], "owner": "~[owner]"} with #[actual]
    # Passes: the captured car is BMW
    * [util] Match BMW with #[car]
    # Passes: a captured object is JSON text, so use it without quotes
    * [util] Match {"owner": #[owner]} with {"owner": {"name": "Ann"}}

    # Passes because the values do NOT match: the names differ
    * [util] Negative match {"name": "Ann"} with #[actual]
    # Passes because the values do NOT match: JSON_NON_EXTENSIBLE_ARRAY rejects the extra "Fiat",
    # and DO_NOT_MATCH makes that mismatch a pass
    * [util] Match {"cars": ["BMW", "Ford"]} against #[actual] using matchConditions=["JSON_NON_EXTENSIBLE_ARRAY", "DO_NOT_MATCH"]

  # 7. Date, time and other steps
  Scenario: Dates
    # Store today and today + 1 year (from epoch millis), formatted as yyyy-MM-dd
    * var millis="#[now]"
    * [time-util] date var today=from millis #[millis] PLUS 0 DAYS with format pattern=yyyy-MM-dd
    * [time-util] date var nextYear=from millis #[millis] PLUS 1 YEARS with format pattern=yyyy-MM-dd
    # Passes: there is one year between them
    * [time-util] Check period from #[today] to #[nextYear] is 1 YEARS using date pattern yyyy-MM-dd

    # Store a date-time minus 15 minutes, in the same format
    * [time-util] date var start=from date 2020-04-28 18:05:58 MINUS 15 MINUTES with format pattern=yyyy-MM-dd HH:mm:ss
    # Passes: start is 15 minutes earlier
    * [util] Match 2020-04-28 17:50:58 with #[start]

    # Store a date-time plus 1 hour, as epoch millis
    * [time-util] date millis var ts=from date 2021-03-07 18:44:27.345+0000 PLUS 1 HOURS with format pattern=yyyy-MM-dd HH:mm:ss.SSSZ
    # Passes: 2021-03-07 19:44:27.345 UTC in epoch millis
    * [util] Match 1615146267345 with #[ts]

    # Passes: there is 1 hour between the two date-times, once their zones are applied
    * [time-util] Check period from 2019-02-03 23:58:12+0200 to 2019-02-04 01:59:10+0300 is 1 HOURS using date time pattern yyyy-MM-dd HH:mm:ssZ
    # Passes because the period does NOT match: it is 366 days, not 36
    * [time-util] Check period from 2020-02-03 to 2021-02-03 doesn't match 36 DAYS using date pattern yyyy-MM-dd

  Scenario: Other steps
    # Pause for half a second, or for 0.01 minutes
    * [util] Wait 0.5s
    * [util] Wait 0.01m

    # A step that does nothing: its text shows up in the report as a comment
    * # Orders are created below

  # 8. Writing your own steps
  Scenario: Writing your own steps
    * var quantity="3"
    # The step receives the map {id=<random UUID>, quantity=6}
    * I order {"id": "#[uid]", "quantity": #{ #[quantity] * 2 }}
    * README check: the last order has a UUID id and quantity 6

  Scenario: README check - what the default transformer resolves
    * var animal="rabbit"
    # {string} parameters and String doc strings are not resolved
    * README check: string parameter "#[animal]" stays raw
    * README check: doc string declared as String stays raw
      """
      #[animal]
      """
    # [_null] as a step argument becomes null, [_blank] does not become ""
    * README check: [_null] is null
    * README check: [_blank] stays the text [_blank]
    # In a data table, [_blank] becomes "" and an empty cell is null
    * README check: table cells
      | blank    | empty |
      | [_blank] |       |
