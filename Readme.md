# Cucumber JUtils

[![Maven Central](https://img.shields.io/maven-central/v/io.github.fslev/cucumber-jutils.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.fslev%22%20AND%20a:%22cucumber-jutils%22)
![Java CI with Maven](https://github.com/fslev/cucumber-jutils/actions/workflows/maven.yml/badge.svg?branch=main)
[![Coverage Status](https://coveralls.io/repos/github/fslev/cucumber-jutils/badge.svg?branch=main)](https://coveralls.io/github/fslev/cucumber-jutils?branch=main)

Scenario variables, SpEL expressions and JSON/XML/text assertion steps for Cucumber-JVM

Cucumber JUtils is a [Cucumber-Guice](https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-guice) extension
for Java test suites. Steps share data through scenario-scoped variables, feature files can contain expressions,
and the built-in steps compare JSON, XML, text and data tables. Placeholders are resolved in every step argument,
doc string and data table cell, including those of your own steps.

## 1. Features

- **Scenario variables**: set them in Gherkin, Java or from files. Read them with `#[name]`, or read a nested field with `#[name/path]`
- **SpEL**: `#{ … }` expressions are evaluated in step arguments and resource files
- **Assertion steps**: match JSON, XML, text and tables with regex support, and capture actual values with `~[name]`
- **HTTP responses**: check status, reason, headers and body from your own steps, with the expected response written as JSON in Gherkin
- **Date/time steps**: shift dates and check the period between two dates
- **Works in your own steps**: `{}` parameters, doc strings and data tables are resolved and converted to the declared Java type

## 2. Installation

```xml
<dependency>
    <groupId>io.github.fslev</groupId>
    <artifactId>cucumber-jutils</artifactId>
    <version>10.6</version>
</dependency>
```

Requires Java 17+. Your project must also include `cucumber-java` and `cucumber-guice` (6.10 or later, below 8)
and `guice` (4.2.1+).

Add `com.cucumber.utils` to the glue path:

```java
@CucumberOptions(glue = {"com.cucumber.utils", "your.steps.package"})
```

## 3. Quickstart

```gherkin
Feature: Quickstart

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
```

## 4. Scenario variables

A scenario variable lasts for one scenario and every step in it can use it, whether the step is Gherkin or Java.

### 4.1 Setting variables

| Step | Stores |
|---|---|
| `var name="value"` | the value |
| `var name=` + doc string | the doc string |
| `var name from table` + data table | the rows, as a list of maps keyed by header |
| `var name from file "path"` | the file content, with placeholders resolved |
| `load vars from file "path"` | `.properties` / `.yaml` / `.yml`: one variable per top-level key. Other supported files: one variable named after the file, holding its content |
| `load vars from dir "path"` | every supported file in the directory tree, loaded as above. Fails if two files define the same name |

Supported files: `.properties`, `.yaml`, `.yml`, `.json`, `.xml`, `.txt`, `.text`, `.csv`, `.html`.
Paths are relative to the classpath (for example `src/test/resources`) or absolute.

In these examples, `[util] Match` steps (see [§6](#6-assertion-steps)) only check that a variable holds the expected value.

```gherkin
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
```

### 4.2 Reading variables

Write `#[name]` in any `{}` step argument, doc string or data table cell, or in a file that a step reads.

- **Nested values**: `#[name/path]` reads a field or array element inside a variable that holds structured data
  (a JSON string, a map or a list). The path uses [JSON Pointer](https://datatracker.ietf.org/doc/html/rfc6901)
  syntax, for example `#[order/items/0/price]`.
- **Built-ins**: `#[uid]` (random UUID), `#[now]` (epoch millis), `#[short-random]`, `#[int-random]`.
  Each occurrence gets a new value.
- **Resolved when read**: if a variable isn't set yet, its placeholder stays as text and is resolved when the value
  is read. So a variable can refer to one that is defined later (example below).
- Variable names may contain letters, digits and `_ $ - . @`.

```gherkin
Scenario: Read nested values
  * var book=
    """
    {"title": "Moby Dick", "tags": ["novel", "sea"]}
    """

  # Passes: book/title is the "title" field
  * [util] Match Moby Dick with #[book/title]
  # Passes: book/tags/1 is the second element of "tags"
  * [util] Match sea with #[book/tags/1]
```

```gherkin
Scenario: Refer to variables defined later
  # firstName and lastName aren't set yet, so author holds the text "#[firstName] #[lastName]"
  * var author="#[firstName] #[lastName]"

  * var firstName="Herman"
  * var lastName="Melville"

  # Passes: author is resolved when it is read, and both names are set by now
  * [util] Match Herman Melville with #[author]
```

### 4.3 From Java

Inject `ScenarioVars` into a step class. Variables set in Java can be read in Gherkin, and the other way round.

```java
@ScenarioScoped
public class StorySteps {

    @Inject
    private ScenarioVars scenarioVars;

    @Given("the story is told")
    public void tellStory() {
        scenarioVars.put("animal", "wolf");                      // #[animal] in later steps
        String location = scenarioVars.getAsString("location");  // set earlier, in Gherkin or Java
        // some_text.txt: "The #[animal] lives in #[location]"
        String story = ScenarioVarsUtils.parse("features/readme/scene/some_text.txt", scenarioVars);
        assertEquals("The wolf lives in " + location, story);
    }
}
```

| Class | Methods |
|---|---|
| `ScenarioVars` | `get`, `getAsString`, `put`, `putAll`, `containsVariable`, `nameSet` |
| `ScenarioVarsUtils` | `parse(filePath, vars)`, `loadScenarioVarsFromFile`, `loadScenarioVarsFromDir`, `loadFileAsScenarioVariable` |
| `ScenarioVarsParser` | `parse(text, vars)` resolves placeholders and SpEL in a string |

## 5. SpEL

`#{ expression }` is evaluated with [Spring Expression Language](https://docs.spring.io/spring-framework/reference/core/expressions.html)
after the `#[…]` placeholders are replaced, so expressions can use variables. When the whole value is one
expression, the result keeps its type. When the expression is inside other text, the result is converted to text.
An invalid expression is left unchanged and logged as a warning. Every evaluation is written to the scenario log.

```gherkin
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
```

## 6. Assertion steps

| Step | Passes when |
|---|---|
| `[util] Match <expected> with <actual>` | `actual` matches `expected` |
| `[util] Match <expected> against` + doc string | the doc string matches |
| `[util] Match <expected> against table` + data table | the table rows match |
| `[util] Match <expected> against file "<path>"` | the file content, with placeholders resolved, matches |
| `[util] Match <expected> against <actual> using matchConditions=[…]` | it matches under the given conditions |
| `[util] Negative match <expected> with <actual>`, or `… against` + doc string | it does **not** match |
| `[util] Match <value> against NULL` | `value` is null |

Matching is done by [jtest-utils](https://github.com/fslev/jtest-utils#5-matching), which reads each side as JSON
first, then XML, then text. By default:

- Expected strings are regular expressions. To match literal text, wrap it in `\Q…\E` or use the `REGEX_DISABLED` condition.
- The actual JSON can contain extra fields and array elements, and the order of array elements doesn't matter.
- `~[name]` in the expected value captures the matching part of the actual value into the variable `name`. If the
  placeholder is the whole value, it captures an entire JSON object, JSON array or set of XML child elements.

Match conditions: `JSON_NON_EXTENSIBLE_OBJECT`, `JSON_NON_EXTENSIBLE_ARRAY`, `JSON_STRICT_ORDER_ARRAY`,
`XML_CHILD_NODELIST_LENGTH`, `XML_CHILD_NODELIST_SEQUENCE`, `XML_ELEMENT_NUM_ATTRIBUTES`, `REGEX_DISABLED`,
`DO_NOT_MATCH` ([details](https://github.com/fslev/jtest-utils#7-match-conditions)).

```gherkin
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
```

### 6.1 HTTP responses

The library doesn't send HTTP requests: your own step does that, with any HTTP client. To check the response,
convert it to jtest-utils' `PlainHttpResponse` and match it with `ObjectMatcher.matchHttpResponse`. jtest-utils is
already a dependency of Cucumber JUtils. The expected response is written in Gherkin as JSON with any of the keys
`status`, `reason`, `headers` and `body`. Only the keys you write are checked. The default transformer
([§8](#8-writing-your-own-steps)) converts that JSON into a `PlainHttpResponse` parameter for you.

This step creates a notebook through a REST API, using the JDK's `HttpClient`:

```java
@ScenarioScoped
public class NotebookSteps {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    @Inject
    private ScenarioVars scenarioVars;

    @When("Create notebook with requestBody={} and check response={}")
    public void createNotebook(String requestBody, PlainHttpResponse expected) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(scenarioVars.getAsString("notebook.api") + "/notebooks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        List<Map.Entry<String, Object>> headers = new ArrayList<>();
        response.headers().map().forEach((name, values) -> values.forEach(value -> headers.add(Map.entry(name, value))));
        PlainHttpResponse actual = PlainHttpResponse.Builder.create()
                .status(response.statusCode())
                .entity(response.body())
                .headers(headers)
                .build();

        // Fails on mismatch. Values captured with ~[name] become scenario variables
        scenarioVars.putAll(ObjectMatcher.matchHttpResponse(null, expected, actual));
    }
}
```

```gherkin
Scenario: Create a notebook
  # Load the API address (notebook.api, from a .properties file) and the request body (createNotebookRequest.json)
  * load vars from dir "features/notebook"

  # Passes if the API answers 201 and the body has an "id" field. ~[notebookId] captures that id.
  # Headers and reason aren't checked, because the expected JSON leaves them out
  * Create notebook with requestBody=#[createNotebookRequest] and check response={"status": 201, "body": {"id": "~[notebookId]"}}

  # From here on, #[notebookId] can be used in later steps, for example to read or delete the notebook
```

The [Cucumber JUtils Tutorial](https://github.com/fslev/cucumber-jutils-tutorial) has the complete version of this,
with Apache HttpClient, polling until the response matches, and cleanup of created data.

Each part of the response is matched with the rules above, and the failure message says which part differs. To
check that a single part does **not** match, pass `DO_NOT_MATCH_HTTP_RESPONSE_BY_STATUS`, `_BY_REASON`,
`_BY_HEADERS` or `_BY_BODY` as match conditions
([details](https://github.com/fslev/jtest-utils#matchhttpresponse)).

If you'd rather not write Java, store the response as JSON in a variable and use `[util] Match` like for any
other JSON. That gives you the same rules, but not the per-part failure messages or the per-part conditions.

## 7. Date, time and other steps

```gherkin
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
```

Units are [`ChronoUnit`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/temporal/ChronoUnit.html)
names (`SECONDS`, `MINUTES`, `HOURS`, `DAYS`, `MONTHS`, `YEARS`, …), and patterns follow `DateTimeFormatter`.
Dates without a zone use the system default zone.

Other steps:

```gherkin
# Pause for half a second, or for 0.01 minutes
* [util] Wait 0.5s
* [util] Wait 0.01m

# A step that does nothing: its text shows up in the report as a comment
* # Orders are created below
```

## 8. Writing your own steps

Every `{}` parameter, doc string and data table passed to your step definitions goes through the library's
default transformer. It first replaces `#[…]` placeholders, then evaluates `#{…}` expressions. It then converts
the result to the parameter's Java type with Jackson (maps, lists, arrays, enums, POJOs).

| Gherkin input | Declare the parameter as |
|---|---|
| step argument | an anonymous `{}` parameter (built-in types like `{string}` and `{int}` are not resolved) |
| doc string | `StringBuilder` |
| data table | `List<Map<String, Object>>` |

`[_null]` as a step argument becomes `null`. `[_blank]` in a data table cell becomes an empty string.

```java
@ScenarioScoped
public class OrderSteps {

    @Inject
    private ScenarioUtils scenarioUtils;

    @When("I order {}")
    public void order(Map<String, Object> order) {
        scenarioUtils.log("Order: {}", order);  // written to the scenario report
    }
}
```

```gherkin
* var quantity="3"
# The step receives the map {id=<random UUID>, quantity=6}
* I order {"id": "#[uid]", "quantity": #{ #[quantity] * 2 }}
```

## 9. Related projects

- [Cucumber JUtils Tutorial](https://github.com/fslev/cucumber-jutils-tutorial): a complete example project
- [jtest-utils](https://github.com/fslev/jtest-utils): the matching engine behind the assertion steps
- [json-compare](https://github.com/fslev/json-compare): the JSON comparison library used by jtest-utils

## 10. License

[Apache License 2.0](LICENSE)
