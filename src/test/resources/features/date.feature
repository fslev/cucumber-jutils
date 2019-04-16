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
