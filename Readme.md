# Cucumber JUtils

[![Maven Central](https://img.shields.io/maven-central/v/io.github.fslev/cucumber-jutils.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.fslev%22%20AND%20a:%22cucumber-jutils%22)
![Java CI with Maven](https://github.com/fslev/cucumber-jutils/actions/workflows/maven.yml/badge.svg?branch=main)
[![Coverage Status](https://coveralls.io/repos/github/fslev/cucumber-jutils/badge.svg?branch=main)](https://coveralls.io/github/fslev/cucumber-jutils?branch=main)

Scenario variables, expressions and JSON/XML assertion steps for Cucumber-JVM

Cucumber JUtils adds three things to your Cucumber tests:

- **Variables**: share data between steps. Save a value in one step and use it in any later step of the same
  scenario, with no scenario context class to write.
- **Ready-made steps**: compare JSON, XML, text and tables (including HTTP responses), work with dates, and wait,
  without writing any Java.
- **Expressions**: compute values inside your feature files, such as `#{ 2 * 5 }`.

It also works in the steps you write yourself, so they get variables and expressions without extra code.

A first taste:

```gherkin
Scenario: Say hello
  # Save "Ann" in a variable called name
  * var name="Ann"
  # #[name] is replaced by "Ann" before the step runs.
  # Passes: "Hello Ann" matches "Hello Ann"
  * [util] Match Hello Ann with Hello #[name]
```

## Contents

1. [Key ideas](#1-key-ideas)
2. [Getting started](#2-getting-started)
3. [Guide](#3-guide): [variables](#31-share-data-between-steps), [files](#32-load-values-from-files),
   [checking values](#33-check-values-compare-json-xml-and-text), [capturing](#34-capture-values-from-a-result),
   [nested values](#35-read-nested-values), [expressions](#36-compute-values-with-expressions),
   [built-in variables](#37-built-in-variables), [templates](#38-templates-variables-that-use-later-variables),
   [dates](#39-dates-and-times), [waits and comments](#310-wait-comment-and-group-steps),
   [your own steps](#311-use-variables-in-your-own-steps), [HTTP responses](#312-check-http-responses)
4. [Reference](#4-reference)
5. [Troubleshooting](#5-troubleshooting)
6. [Related projects](#6-related-projects)
7. [License](#7-license)

## 1. Key ideas

### 1.1 Cucumber words used in this guide

| Word | Meaning |
|---|---|
| **Feature file** | A `.feature` text file with your scenarios, written in Gherkin (`Given` / `When` / `Then`). |
| **Step** | One line of a scenario. `*` can replace `Given`, `When`, `Then` or `And`. It just means "a step", and the examples use it to stay short. |
| **Step definition** | The Java method that runs a step. It has an annotation such as `@Given("…")` whose pattern matches the step text. |
| **Glue** | The Java packages where Cucumber looks for step definitions. You list them in the configuration ([§2.2](#22-configure-cucumber)). |
| **Doc string** | A block of text under a step, between two `"""` lines. Use it for long or multi-line values, such as a JSON document. |
| **Data table** | A table under a step, written with `\|` characters. |

### 1.2 The four symbols

| You write | Name | What it does | Example |
|---|---|---|---|
| `#[name]` | variable | Replaced by the value of the variable `name` | `Hello #[name]` → `Hello Ann` |
| `#{ … }` | expression | Computed with [SpEL](https://docs.spring.io/spring-framework/reference/core/expressions.html) (Spring Expression Language) | `#{ 2 * 5 }` → `10` |
| `~[name]` | capture | Only in the *expected* value of a match step. Saves the matching part of the *actual* value into the variable `name` | expected `order-~[id]`, actual `order-42` → `#[id]` is `42` |
| `{}` | parameter | Comes from Cucumber, not from this library: the "any value" parameter in a step definition. Your own steps need it to get `#[…]` and `#{…}` replaced | `@When("I order {}")` |

### 1.3 Built-in steps and your own steps

Steps that start with `[util]` or `[time-util]`, and the `var …` / `load vars …` steps, come with Cucumber JUtils.
Every other step is one you write yourself.

Most examples check a value with `[util] Match <expected> with <actual>`. It passes when the actual value matches
the expected one, and fails the scenario otherwise. [§3.3](#33-check-values-compare-json-xml-and-text) explains how matching works.

## 2. Getting started

This section builds a small Maven project that runs one scenario. You need Java 17 or newer, and Maven.

### 2.1 Add the dependencies

Add this to your `pom.xml`:

```xml
<properties>
    <maven.compiler.release>17</maven.compiler.release>
</properties>

<dependencyManagement>
    <dependencies>
        <!-- Keep all Cucumber and JUnit modules on matching versions -->
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-bom</artifactId>
            <version>7.34.9</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <dependency>
            <groupId>org.junit</groupId>
            <artifactId>junit-bom</artifactId>
            <version>6.1.3</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>io.github.fslev</groupId>
        <artifactId>cucumber-jutils</artifactId>
        <version>10.6</version>
        <scope>test</scope>
    </dependency>
    <!-- Cucumber JUtils needs these, but doesn't bring them itself -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-java</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-guice</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.google.inject</groupId>
        <artifactId>guice</artifactId>
        <version>7.0.0</version>
        <scope>test</scope>
    </dependency>
    <!-- Lets JUnit (and so Maven) run feature files -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-junit-platform-engine</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.platform</groupId>
        <artifactId>junit-platform-suite</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <!-- Runs the tests; older default versions can't run JUnit Platform tests -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.5.6</version>
        </plugin>
    </plugins>
</build>
```

- Cucumber JUtils works with Cucumber 6.10 or later, below 8.
- **cucumber-guice** lets Cucumber create your step classes and share objects between them. That's how a variable
  saved in one step reaches every other step. You don't write any Guice configuration.
- Don't add `cucumber-picocontainer`: Cucumber can use only one of the two.

### 2.2 Configure Cucumber

Create `src/test/resources/junit-platform.properties`:

```properties
# Packages with step definitions: Cucumber JUtils' steps, then your own
cucumber.glue=com.cucumber.utils,com.example.steps
# Print every step to the console
cucumber.plugin=pretty
```

Create the class that runs your feature files, `src/test/java/com/example/RunCucumberTest.java`:

```java
package com.example;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

// Runs every .feature file under src/test/resources/features
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class RunCucumberTest {
}
```

### 2.3 Write a feature file

Create `src/test/resources/features/first.feature`:

```gherkin
Feature: First steps with Cucumber JUtils

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
```

Your project now looks like this:

```
pom.xml
src/test/java/com/example/RunCucumberTest.java
src/test/resources/junit-platform.properties
src/test/resources/features/first.feature
```

### 2.4 Run it

```bash
mvn test
```

That's all the setup. The rest of this guide is examples you can paste into `first.feature`.

## 3. Guide

### 3.1 Share data between steps

Use this when several steps need the same value, for example a name you send in one step and check in another.

| Step | Saves |
|---|---|
| `var name="value"` | the value |
| `var name=` + doc string | the doc string |
| `var name from table` + data table | the rows, as a list with one map per row (keys are the header cells) |

```gherkin
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
```

Good to know:
- A variable lasts for one scenario. The next scenario starts with none.
- Saving a variable again replaces its value.
- Names may contain letters, digits and `_ $ - . @`.
- If no variable has that name, `#[name]` stays as text. So a typo shows up as a literal `#[nmae]` in the failure message.

### 3.2 Load values from files

Use this to keep test data out of feature files: request bodies, expected responses, settings per environment.

| Step | Loads |
|---|---|
| `var name from file "path"` | the whole file into one variable, with `#[…]` and `#{…}` in it resolved |
| `load vars from file "path"` | a `.properties` / `.yaml` / `.yml` file: one variable per top-level key. Any other supported file: one variable named after the file |
| `load vars from dir "path"` | every supported file in the folder and its subfolders, loaded as above. Fails if two files define the same name |

Supported files: `.properties`, `.yaml`, `.yml`, `.json`, `.xml`, `.txt`, `.text`, `.csv`, `.html`.

Paths start from `src/test/resources` (more precisely, from the root of the classpath), or are absolute. With these files:

```
src/test/resources/placeholders/properties/drinks/
├── drink.yaml     beer: Bergenbier
└── whisky.txt     Johnny Walker
```

```gherkin
Scenario: Load values from a folder
  * load vars from dir "placeholders/properties/drinks"
  # Passes: the YAML key "beer" became a variable
  * [util] Match Bergenbier with #[beer]
  # Passes: whisky.txt became a variable named after the file
  * [util] Match Johnny Walker with #[whisky]
```

### 3.3 Check values: compare JSON, XML and text

Use this to assert that a value is what you expect. `[util] Match` compares an expected value with an actual
value and fails the scenario if they differ. It figures out by itself whether the values are JSON, XML or text.

How values are compared, by default:

- **Text**: the expected value is a [regular expression](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/regex/Pattern.html).
  `.*` matches any text and `\d+` matches digits. To match characters such as `[ ( . ? *` literally, wrap the text
  in `\Q…\E`, as in `\Q[1.0]\E`.
- **JSON**: the actual JSON may have extra fields and extra array elements, and array order doesn't matter. Values
  inside are compared as text, so they are regexes too.
- **XML**: the same idea. The actual XML may have extra elements and attributes.

The plain comparison uses **with**. The variants for doc strings, tables, files and conditions use **against**:

| Step | Compares `<expected>` with |
|---|---|
| `[util] Match <expected> with <actual>` | `<actual>` |
| `[util] Match <expected> against` + doc string | the doc string |
| `[util] Match <expected> against table` + data table | the table rows |
| `[util] Match <expected> against file "<path>"` | the file content, with `#[…]` and `#{…}` resolved |
| `[util] Match <expected> against <actual> using matchConditions=[…]` | `<actual>`, with stricter rules ([§4.3](#43-match-conditions)) |
| `[util] Negative match <expected> with <actual>`, or `… against` + doc string | `<actual>`: passes when they **don't** match |
| `[util] Match <value> against NULL` | nothing: passes when `<value>` is null, for example a variable set to `[_null]` |

```gherkin
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
```

Matching is done by jtest-utils. Its documentation has the full rules for
[matching](https://github.com/fslev/jtest-utils#5-matching) and [capturing](https://github.com/fslev/jtest-utils#6-capture-placeholders).

### 3.4 Capture values from a result

Use this when a result contains a value you need later and can't know in advance, such as an id the service
generated. Put `~[name]` where that value is in the expected value: if the match passes, `name` holds the actual value.

```gherkin
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
```

When `~[name]` is the whole value, as with `owner`, it captures an entire JSON object, JSON array or set of XML
child elements. Inside a longer text, it captures only its part: expected `order-~[n]`, actual `order-42` gives `42`.

### 3.5 Read nested values

Use this to pick one field out of a variable that holds JSON, without capturing it first. Add a path after the
variable name, with `/` between the levels and array positions counted from 0.

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

This works for variables that hold a JSON string, a map or a list. The path syntax is
[JSON Pointer](https://datatracker.ietf.org/doc/html/rfc6901). A `/` inside a field name is written `~1`.

### 3.6 Compute values with expressions

Use this when a value has to be calculated: arithmetic, text formatting, random data, or a call to a Java method.
Write the expression inside `#{ … }`. It uses [SpEL](https://docs.spring.io/spring-framework/reference/core/expressions.html)
(Spring Expression Language). `#[…]` variables are replaced first, so expressions can use them.

```gherkin
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
```

Good to know:
- When the whole value is one expression, the result keeps its type (a number stays a number). An expression inside
  longer text becomes text.
- An invalid expression is left unchanged and logged as a warning.
- Every result is written to the scenario log in the Cucumber report.

### 3.7 Built-in variables

These variables always exist, and each use produces a new value:

| Variable | Value |
|---|---|
| `#[uid]` | a random UUID, such as `3f2b…` |
| `#[now]` | the current time in milliseconds since 1970 (epoch millis) |
| `#[short-random]` | a random number from 0 to 32766 |
| `#[int-random]` | a random number from 0 to 2147483646 |

```gherkin
Scenario: Built-in variables
  # Save one UUID, so every later #[orderId] gives the same value
  * var orderId="#[uid]"
  # Passes: two uses of #[orderId] are equal
  * [util] Match #[orderId] with #[orderId]
```

### 3.8 Templates: variables that use later variables

Use this for templates: a request body or an expected response that you load at the start and fill in later. A
`#[name]` whose variable doesn't exist yet stays as text, and is replaced when the value is read.

```gherkin
Scenario: Templates
  # firstName and lastName aren't set yet, so author holds the text "#[firstName] #[lastName]"
  * var author="#[firstName] #[lastName]"

  * var firstName="Herman"
  * var lastName="Melville"

  # Passes: author is filled in when it is read, and both names are set by now
  * [util] Match Herman Melville with #[author]
```

### 3.9 Dates and times

Use these to create dates relative to now, or to check the time between two dates. The `date var` steps have this shape:

```
[time-util] date var <name>=from millis <epoch millis> <PLUS or MINUS> <amount> <unit> with format pattern=<pattern>
[time-util] date var <name>=from date <date> <PLUS or MINUS> <amount> <unit> with format pattern=<pattern>
```

`<unit>` is one of `SECONDS`, `MINUTES`, `HOURS`, `DAYS`, `WEEKS`, `MONTHS`, `YEARS` (the
[`ChronoUnit`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/temporal/ChronoUnit.html)
names). `<pattern>` uses [`DateTimeFormatter`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/format/DateTimeFormatter.html)
letters: `yyyy-MM-dd`, `HH:mm:ss`, `Z` for the zone offset. Dates without a zone use the computer's time zone.

```gherkin
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
```

`Check period` takes `using date pattern` for dates only, and `using date time pattern` for dates with a time.

### 3.10 Wait, comment and group steps

```gherkin
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
```

Unlike a Gherkin `# comment`, these are steps, so they appear in the report. With
[cucumber-reporting-extended](https://github.com/fslev/cucumber-reporting-extended), `#>> title` … `#<<` becomes a
section you can collapse.

### 3.11 Use variables in your own steps

#### Parameters

Every `{}` parameter, doc string and data table that reaches your step definitions has its `#[…]` replaced and its
`#{…}` computed first. The result is then converted to the parameter's Java type. JSON text becomes a `Map`, a
`List`, an array, an enum or your own class (using Jackson).

| Gherkin input | Declare the parameter as | Not resolved if declared as |
|---|---|---|
| step argument | `{}` | `{string}`, `{int}` and other built-in parameter types |
| doc string | `StringBuilder` | `String` |
| data table | `List<Map<String, Object>>` | `List<Map<String, String>>` |

```java
package com.example.steps;

import com.cucumber.utils.context.ScenarioUtils;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.When;

import java.util.Map;

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

Two special values: `[_null]` as a step argument becomes `null`, and `[_blank]` in a data table cell becomes an
empty text (an empty cell is `null`).

#### Reading and saving variables in Java

All variables live in one `ScenarioVars` object per scenario. To use it in a step class:

- Add `@Inject` to a field. Cucumber (through Guice) then fills the field for you. You never call `new ScenarioVars()`.
- Add `@ScenarioScoped` to the class. Cucumber then creates the class once per scenario and shares the same
  `ScenarioVars` with every step class.

```java
package com.example.steps;

import com.cucumber.utils.context.ScenarioVarsUtils;
import com.cucumber.utils.context.vars.ScenarioVars;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

The other methods are listed in [§4.4](#44-java-api).

### 3.12 Check HTTP responses

Use this in your own step that calls an HTTP API. The library doesn't send requests, but it can check the response.
Convert the response to a `PlainHttpResponse` and compare it with `ObjectMatcher.matchHttpResponse`. Both come from
[jtest-utils](https://github.com/fslev/jtest-utils#matchhttpresponse), which Cucumber JUtils already includes.

In Gherkin, you write the expected response as JSON with any of the keys `status`, `reason`, `headers` and `body`.
Only the keys you write are checked. Because the step takes a `{}` parameter of type `PlainHttpResponse`, the JSON is
converted for you ([§3.11](#311-use-variables-in-your-own-steps)).

This step creates a notebook through a REST API, using the HTTP client that comes with Java:

```java
package com.example.steps;

import com.cucumber.utils.context.vars.ScenarioVars;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.When;
import io.jtest.utils.matcher.ObjectMatcher;
import io.jtest.utils.matcher.http.PlainHttpResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        // The first argument is an optional message added to the failure; null means none.
        // Values captured with ~[name] are returned, and putAll saves them as variables.
        scenarioVars.putAll(ObjectMatcher.matchHttpResponse(null, expected, toPlainHttpResponse(response)));
    }

    private static PlainHttpResponse toPlainHttpResponse(HttpResponse<String> response) {
        List<Map.Entry<String, Object>> headers = new ArrayList<>();
        response.headers().map().forEach((name, values) -> values.forEach(value -> headers.add(Map.entry(name, value))));
        return PlainHttpResponse.Builder.create()
                .status(response.statusCode())
                .entity(response.body())
                .headers(headers)
                .build();
    }
}
```

The scenario below loads its test data from this folder:

```
src/test/resources/features/notebook/
├── notebook.properties
└── createNotebookRequest.json
```

`notebook.properties` holds the API address. It becomes the variable `#[notebook.api]`:

```properties
notebook.api=http://localhost:8080/api
```

`createNotebookRequest.json` holds the request body. It becomes the variable `#[createNotebookRequest]`,
named after the file:

```json
{
  "name": "My notebook",
  "price": 1200
}
```

```gherkin
Scenario: Create a notebook
  # Load the API address (notebook.api) and the request body (createNotebookRequest) from the folder above
  * load vars from dir "features/notebook"

  # Passes if the API answers 201 and the body has an "id" field. ~[notebookId] captures that id.
  # Headers and reason aren't checked, because the expected JSON leaves them out
  * Create notebook with requestBody=#[createNotebookRequest] and check response={"status": 201, "body": {"id": "~[notebookId]"}}

  # From here on, #[notebookId] can be used in later steps, for example to read or delete the notebook
```

Each part of the response is compared with the rules in [§3.3](#33-check-values-compare-json-xml-and-text), and a failure says which part
differs. To check that one part does **not** match, pass `DO_NOT_MATCH_HTTP_RESPONSE_BY_STATUS`, `_BY_REASON`,
`_BY_HEADERS` or `_BY_BODY` as extra arguments to `matchHttpResponse`
([details](https://github.com/fslev/jtest-utils#matchhttpresponse)).

The [Cucumber JUtils Tutorial](https://github.com/fslev/cucumber-jutils-tutorial) has the complete version of this,
with Apache HttpClient, retrying until the response matches, and deleting the data it created.

## 4. Reference

### 4.1 Built-in steps

| Step | What it does |
|---|---|
| `var <name>="<value>"` | Saves the value |
| `var <name>=` + doc string | Saves the doc string |
| `var <name> from table` + data table | Saves the rows as a list of maps |
| `var <name> from file "<path>"` | Saves the file content, resolved |
| `load vars from file "<path>"` | Loads one file ([§3.2](#32-load-values-from-files)) |
| `load vars from dir "<path>"` | Loads every supported file in a folder tree |
| `[util] Match <expected> with <actual>` | Fails unless actual matches expected |
| `[util] Match <expected> against` + doc string | Same, with a doc string as actual |
| `[util] Match <expected> against table` + data table | Same, with a data table as actual |
| `[util] Match <expected> against file "<path>"` | Same, with the resolved file content as actual |
| `[util] Match <expected> against <actual> using matchConditions=[…]` | Same, with [match conditions](#43-match-conditions) |
| `[util] Match <value> against NULL` | Fails unless the value is null |
| `[util] Negative match <expected> with <actual>` | Fails if actual matches expected |
| `[util] Negative match <expected> against` + doc string | Same, with a doc string as actual |
| `[time-util] date var <name>=from millis <millis> <PLUS\|MINUS> <amount> <unit> with format pattern=<pattern>` | Saves a shifted date, formatted |
| `[time-util] date var <name>=from date <date> <PLUS\|MINUS> <amount> <unit> with format pattern=<pattern>` | Same, from a date in that pattern |
| `[time-util] date millis var <name>=from date <date> <PLUS\|MINUS> <amount> <unit> with format pattern=<pattern>` | Saves a shifted date as epoch millis |
| `[time-util] Check period from <date1> to <date2> is <amount> <unit> using date pattern <pattern>` | Fails unless the period matches (dates) |
| `[time-util] Check period from <date1> to <date2> is <amount> <unit> using date time pattern <pattern>` | Same, for date-times |
| `[time-util] Check period from <date1> to <date2> doesn't match <amount> <unit> using date pattern <pattern>` | Fails if the period matches (also `date time pattern`) |
| `[util] Wait <seconds>s` / `[util] Wait <minutes>m` | Pauses; decimals allowed |
| `# <text>` | Does nothing; shows as a comment in the report |
| `#>> <title>` … `#<<` | Do nothing; mark a collapsible group in the report |

### 4.2 Symbols and special values

| Symbol | Where | Meaning |
|---|---|---|
| `#[name]` | anywhere | Value of a variable ([§3.1](#31-share-data-between-steps)) |
| `#[name/path]` | anywhere | Nested value ([§3.5](#35-read-nested-values)) |
| `#{ … }` | anywhere | SpEL expression ([§3.6](#36-compute-values-with-expressions)) |
| `~[name]` | expected value of a match | Capture ([§3.4](#34-capture-values-from-a-result)) |
| `[_null]` | step argument | `null` |
| `[_blank]` | data table cell | empty text |
| `\Q…\E` | expected value of a match | Literal text, no regex |

### 4.3 Match conditions

Pass them as a JSON list: `using matchConditions=["JSON_STRICT_ORDER_ARRAY", "JSON_NON_EXTENSIBLE_OBJECT"]`.
They are defined by jtest-utils ([match conditions](https://github.com/fslev/jtest-utils#7-match-conditions)).

| Condition | Effect |
|---|---|
| `JSON_NON_EXTENSIBLE_OBJECT` | The actual JSON objects may not have extra fields |
| `JSON_NON_EXTENSIBLE_ARRAY` | The actual JSON arrays may not have extra elements |
| `JSON_STRICT_ORDER_ARRAY` | JSON array elements must be in the same order |
| `XML_CHILD_NODELIST_LENGTH` | XML elements must have the same number of children |
| `XML_CHILD_NODELIST_SEQUENCE` | XML child elements must be in the same order |
| `XML_ELEMENT_NUM_ATTRIBUTES` | XML elements must have the same number of attributes |
| `REGEX_DISABLED` | Compare text literally, not as a regex |
| `DO_NOT_MATCH` | Invert: pass when the values don't match |

### 4.4 Java API

| Class | Methods |
|---|---|
| `ScenarioVars` | `get`, `getAsString`, `put`, `putAll`, `containsVariable`, `nameSet` |
| `ScenarioVarsUtils` | `parse(filePath, vars)`, `loadScenarioVarsFromFile`, `loadScenarioVarsFromDir`, `loadFileAsScenarioVariable` |
| `ScenarioVarsParser` | `parse(text, vars)`: resolves `#[…]` and `#{…}` in a string |
| `ScenarioUtils` | `log(message, args…)`: writes to the scenario report; `getScenario()` |

## 5. Troubleshooting

**`The step 'var name="Ann"' and 4 other step(s) are undefined`**
If the undefined steps are built-in ones (`var …`, `load vars …`, `[util] …`, `[time-util] …`), then
`com.cucumber.utils` is missing from `cucumber.glue` ([§2.2](#22-configure-cucumber)).

**`#[name]` shows up unchanged**
- No variable has that name yet, or the name has a typo.
- Your step definition uses `{string}`, `{int}` or similar instead of `{}`.
- Your doc string parameter is a `String` instead of a `StringBuilder`.

**A text match fails although the values look equal**
The expected value is a regex, so `[ ] ( ) . ? * + |` have special meanings. Wrap the text in `\Q…\E`, or use
`using matchConditions=["REGEX_DISABLED"]`.

**`The step '[util] Match a against b' is undefined`**
The plain comparison is `[util] Match <expected> with <actual>`. `against` is only for doc strings, tables, files
and match conditions.

**`More than one Cucumber ObjectFactory was found on the classpath`**
The message lists `GuiceFactory` and `PicoFactory`. Remove `cucumber-picocontainer` from your dependencies:
Cucumber JUtils uses cucumber-guice.

**`Invalid variable name`**
Names may contain only letters, digits and `_ $ - . @`.

**`WARN No Root logger was configured, creating default ERROR-level Root logger`**
This is harmless: Cucumber JUtils' own logging configuration doesn't set a root logger. To configure logging
yourself, and make the warning go away, add `src/test/resources/log4j2-test.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%-5level %logger{36} - %msg%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Root level="warn">
            <AppenderRef ref="Console"/>
        </Root>
    </Loggers>
</Configuration>
```

## 6. Related projects

- [Cucumber JUtils Tutorial](https://github.com/fslev/cucumber-jutils-tutorial): a complete example project
- [jtest-utils](https://github.com/fslev/jtest-utils): the matching engine behind the assertion steps
- [json-compare](https://github.com/fslev/json-compare): the JSON comparison library used by [jtest-utils](https://github.com/fslev/jtest-utils)

## 7. License

[Apache License 2.0](LICENSE)
