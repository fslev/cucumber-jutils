Feature: README examples
  Every Gherkin example from Readme.md, kept here so the README stays correct.
  Scenarios named "README check" verify claims the README makes in prose.

  Scenario: Say hello
    # Save "Ann" in a variable called name
    * var name="Ann"
    # #[name] is replaced by "Ann" before the step runs.
    # Passes: "Hello Ann" matches "Hello Ann"
    * [util] Match Hello Ann with Hello #[name]

  Scenario: Save, reuse and check values
    # Save a value
    * var name="Ann"
    # Passes: #[name] is replaced by "Ann"
    * [util] Match Hello Ann with Hello #[name]

    # Save a JSON document, as one of your own steps would after calling a service
    * var user=
      """
      {"id": 42, "name": "Ann", "role": "admin"}
      """

    # Passes: the actual JSON has these fields (extra fields such as "role" are allowed),
    # and ~[userId] saves the actual id in a new variable
    * [util] Match {"name": "Ann", "id": "~[userId]"} with #[user]
    # Passes: the captured id is 42
    * [util] Match 42 with #[userId]

  Scenario: Save values
    # From a value
    * var animal="rabbit"
    # Passes: animal is "rabbit"
    * [util] Match rabbit with #[animal]

    # From a data table: a list with one map per row
    * var animals from table
      | feline  | marsupial |
      | lioness | kangaroo  |
    # Passes: animals holds the row, keyed by the header cells
    * [util] Match [{"feline": "lioness", "marsupial": "kangaroo"}] with #[animals]

  Scenario: Load values from a folder
    * load vars from dir "placeholders/properties/drinks"
    # Passes: the YAML key "beer" became a variable
    * [util] Match Bergenbier with #[beer]
    # Passes: whisky.txt became a variable named after the file
    * [util] Match Johnny Walker with #[whisky]

  Scenario: Check values
    # Passes: "\d+" is a regex for digits
    * [util] Match order-\d+ with order-42

    * var actual=
      """
      {"name": "John", "age": 30, "cars": ["BMW", "Ford", "Fiat"]}
      """
    # Passes: "J.*n" matches "John". "age" isn't in the expected JSON, so it isn't checked
    * [util] Match {"name": "J.*n", "cars": ["Fiat", "BMW"]} with #[actual]

    # Passes because the values do NOT match: the names differ
    * [util] Negative match {"name": "Ann"} with #[actual]

    # Passes because the values do NOT match: JSON_NON_EXTENSIBLE_ARRAY forbids extra array elements
    # (actual has "Fiat"), and DO_NOT_MATCH turns that mismatch into a pass
    * [util] Match {"cars": ["BMW", "Ford"]} against #[actual] using matchConditions=["JSON_NON_EXTENSIBLE_ARRAY", "DO_NOT_MATCH"]

  Scenario: Capture values
    * var response=
      """
      {"id": 7, "status": "CREATED", "owner": {"name": "Ann"}}
      """

    # Passes, and saves 7 in id and the whole owner object in owner
    * [util] Match {"id": "~[id]", "status": "CREATED", "owner": "~[owner]"} with #[response]

    # Passes: the captured id is 7
    * [util] Match 7 with #[id]
    # Passes: a captured object is JSON text, so it's used without quotes
    * [util] Match {"owner": #[owner]} with {"owner": {"name": "Ann"}}

  Scenario: Read nested values
    * var book=
      """
      {"title": "Moby Dick", "tags": ["novel", "sea"]}
      """

    # Passes: book/title is the "title" field
    * [util] Match Moby Dick with #[book/title]
    # Passes: book/tags/1 is the second element of "tags"
    * [util] Match sea with #[book/tags/1]

  Scenario: Expressions
    * var number="5"

    # #[number] is replaced first, giving #{ 5 % 2 != 0 }, which is true
    * var isOdd="#{ #[number] % 2 != 0 }"
    # Passes: isOdd is true
    * [util] Match true with #[isOdd]

    # T(...) calls a Java class: format the number with three digits
    * var code="#{ T(java.lang.String).format('%03d', #[number]) }"
    # Passes: code is "005"
    * [util] Match 005 with #[code]

  Scenario: Built-in variables
    # Save one UUID, so every later #[orderId] gives the same value
    * var orderId="#[uid]"
    # Passes: two uses of #[orderId] are equal
    * [util] Match #[orderId] with #[orderId]

  Scenario: Templates
    # firstName and lastName aren't set yet, so author holds the text "#[firstName] #[lastName]"
    * var author="#[firstName] #[lastName]"

    * var firstName="Herman"
    * var lastName="Melville"

    # Passes: author is filled in when it is read, and both names are set by now
    * [util] Match Herman Melville with #[author]

  Scenario: Dates
    # Save today and today + 1 year, formatted as yyyy-MM-dd
    * var millis="#[now]"
    * [time-util] date var today=from millis #[millis] PLUS 0 DAYS with format pattern=yyyy-MM-dd
    * [time-util] date var nextYear=from millis #[millis] PLUS 1 YEARS with format pattern=yyyy-MM-dd
    # Passes: there is one year between them
    * [time-util] Check period from #[today] to #[nextYear] is 1 YEARS using date pattern yyyy-MM-dd

    # Save a date-time minus 15 minutes, in the same format
    * [time-util] date var start=from date 2020-04-28 18:05:58 MINUS 15 MINUTES with format pattern=yyyy-MM-dd HH:mm:ss
    # Passes: start is 15 minutes earlier
    * [util] Match 2020-04-28 17:50:58 with #[start]

    # Save a date-time plus 1 hour, as epoch millis
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

    # Steps that do nothing, but show up in the report:
    # a comment, then a group of steps
    * # Orders are created below
    * #>> Create the orders
    * [util] Match 1 with 1
    * #<<

  Scenario: Writing your own steps
    * var quantity="3"
    # The step receives the map {id=<random UUID>, quantity=6}
    * I order {"id": "#[uid]", "quantity": #{ #[quantity] * 2 }}
    * README check: the last order has a UUID id and quantity 6

  Scenario: README check - ScenarioVars from Java
    * var location="forest"
    * the story is told
    * [util] Match wolf with #[animal]

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

  Scenario: README check - Match against NULL
    # Passes: a variable set to [_null] holds null
    * var nothing="[_null]"
    * [util] Match #[nothing] against NULL
