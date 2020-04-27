Feature: Date feature

  Scenario: Compare two time dates
    Given DateTime pattern="yyyy-MM-dd HH:mm:ss"
    Then DateTime check period from "2018-02-03 01:00:00" to "2019-02-03 01:00:00" is 1year
    And DateTime check period from "2018-02-03 01:00:00" to "2019-02-02 12:01:10" is 364days
    And DateTime check period from "2019-02-03 01:02:12" to "2019-02-03 23:59:10" is 22hours
    Given param expected="116"
    And DateTime check period from "2019-02-03 22:02:12" to "2019-02-03 23:59:10" is #[expected]minutes
    Given param expected="58"
    And DateTime check period from "2019-02-03 23:58:12" to "2019-02-03 23:59:10" is #[expected]seconds

  Scenario: Compare two dates
    Given DateTime pattern="yyyy-MM-dd HH:mm:ss"
    Then Date check period from "2018-02-03 01:00:00" to "2019-02-03 00:00:00" is 1year
    Then Date check period from "2018-02-03 01:00:00" to "2019-02-03 00:00:00" is 365days
    Then Date check period from "2020-02-03 01:00:00" to "2021-02-03 00:00:00" is 1year
    Then Date check period from "2020-02-03 01:00:00" to "2021-02-03 00:00:00" is 366days

    Scenario: Format dates
      Given DateTime pattern="yyyy-MM-dd HH:mm:ss"
      Given date param currentTime="now PLUS 0 YEARS" with format pattern=yyyy-MM-dd HH:mm:ss
      And date param years="now PLUS 15 YEARS" with format pattern=yyyy-MM-dd HH:mm:ss
      And date param days="now PLUS 15 DAYS" with format pattern=yyyy-MM-dd HH:mm:ss
      And date param months="now PLUS 15 MONTHS" with format pattern=yyyy-MM-dd HH:mm:ss
      And date param minutes="now PLUS 15 MINUTES" with format pattern=yyyy-MM-dd HH:mm:ss

      Then Date check period from "#[currentTime]" to "#[years]" is 15year
      Then Date check period from "#[currentTime]" to "#[days]" is 15days
      Then Date check period from "#[currentTime]" to "#[months]" is 15months
      Then DateTime check period from "#[currentTime]" to "#[minutes]" is 15minutes