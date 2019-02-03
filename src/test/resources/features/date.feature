Feature: Date feature

  Scenario: Compare two time dates
    Given Date pattern="yyyy-MM-dd HH:mm:ss"
    Then Date check period between "2018-02-03 01:00:00" and "2019-02-03 01:00:00" is 1year
    And Date check period between "2018-02-03 01:00:00" and "2019-02-02 12:01:10" is 364days
    And Date check period between "2019-02-03 01:02:12" and "2019-02-03 23:59:10" is 22hours
    And Date check period between "2019-02-03 22:02:12" and "2019-02-03 23:59:10" is 116minutes
    And Date check period between "2019-02-03 23:58:12" and "2019-02-03 23:59:10" is 58seconds
