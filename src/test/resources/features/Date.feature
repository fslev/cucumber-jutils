Feature: Date feature

  Scenario: Compare two time dates
    Then Check period from "2018-02-03 01:00:00" to "2019-02-03 01:00:00" is 1 YEARS using date time pattern yyyy-MM-dd HH:mm:ss
    Then Check period from "2018-02-03 01:00:00" to "2019-02-02 12:01:10" is 364 DAYS using date time pattern yyyy-MM-dd HH:mm:ss
    Then Check period from "2019-02-03 01:02:12" to "2019-02-03 23:59:10" is 22 HOURS using date time pattern yyyy-MM-dd HH:mm:ss
    Given param expected="116"
    Then Check period from "2019-02-03 22:02:12" to "2019-02-03 23:59:10" is #[expected] MINUTES using date time pattern yyyy-MM-dd HH:mm:ss
    Given param expected="58"
    Then Check period from "2019-02-03 23:58:12" to "2019-02-03 23:59:10" is #[expected] SECONDS using date time pattern yyyy-MM-dd HH:mm:ss
    Then Check period from "2019-02-03 01:02:12" to "2019-02-03 23:59:10" doesn't match 36 HOURS using date time pattern yyyy-MM-dd HH:mm:ss

  Scenario: Compare two dates
    Then Check period from "2019-01-03" to "2019-02-03" is 1 MONTHS using date pattern yyyy-MM-dd
    Then Check period from "2018-01-03" to "2019-02-03" is 1 YEARS using date pattern yyyy-MM-dd
    Then Check period from "2018-02-03" to "2019-02-03" is 365 DAYS using date pattern yyyy-MM-dd
    Then Check period from "2020-02-03" to "2021-02-03" is 1 YEARS using date pattern yyyy-MM-dd
    Then Check period from "2020-02-03" to "2021-02-03" is 366 DAYS using date pattern yyyy-MM-dd
    Then Check period from "2020-02-03" to "2021-02-03" doesn't match 36 DAYS using date pattern yyyy-MM-dd


  Scenario: Format dates from current date
    Given date param currentTime="now PLUS 0 YEARS" with format pattern=yyyy-MM-dd HH:mm:ss
    And date param years="now PLUS 15 YEARS" with format pattern=yyyy-MM-dd HH:mm:ss
    And date param days="now PLUS 15 DAYS" with format pattern=yyyy-MM-dd HH:mm:ss
    And date param months="now PLUS 15 MONTHS" with format pattern=yyyy-MM-dd HH:mm:ss
    And date param minutes="now PLUS 15 MINUTES" with format pattern=yyyy-MM-dd HH:mm:ss

    Then Check period from "#[currentTime]" to "#[years]" is 15 YEARS using date time pattern yyyy-MM-dd HH:mm:ss
    Then Check period from "#[currentTime]" to "#[days]" is 15 DAYS using date time pattern yyyy-MM-dd HH:mm:ss
    Then Check period from "#[currentTime]" to "#[months]" is 15 MONTHS using date time pattern yyyy-MM-dd HH:mm:ss
    Then Check period from "#[currentTime]" to "#[minutes]" is 15 MINUTES using date time pattern yyyy-MM-dd HH:mm:ss


  Scenario: Format dates from custom date
    Given date param currentTime="from 2020-04-28 18:05:58 PLUS 0 YEARS" with format pattern=yyyy-MM-dd HH:mm:ss
    And date param years="from 2020-04-28 18:05:58 PLUS 1 YEARS" with format pattern=yyyy-MM-dd HH:mm:ss
    And date param months="from 2020-04-28 18:05:58 PLUS 1 MONTHS" with format pattern=yyyy-MM-dd HH:mm:ss
    And date param days="from 2020-04-28 18:05:58 PLUS 1 DAYS" with format pattern=yyyy-MM-dd HH:mm:ss
    And date param minutes="from 2020-04-28 18:05:58 PLUS 1 MINUTES" with format pattern=yyyy-MM-dd HH:mm:ss

    Then Check period from "#[currentTime]" to "#[years]" is 1 YEARS using date time pattern yyyy-MM-dd HH:mm:ss
    Then Check period from "#[currentTime]" to "#[days]" is 1 DAYS using date time pattern yyyy-MM-dd HH:mm:ss
    Then Check period from "#[currentTime]" to "#[months]" is 1 MONTHS using date time pattern yyyy-MM-dd HH:mm:ss
    Then Check period from "#[currentTime]" to "#[minutes]" is 1 MINUTES using date time pattern yyyy-MM-dd HH:mm:ss