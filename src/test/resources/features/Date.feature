Feature: Date feature

  Scenario: Match two time dates
    Then [time-util] Check period from 2018-02-03 01:00:00 to 2019-02-03 01:00:00 is 1 YEARS using date time pattern yyyy-MM-dd HH:mm:ss
    Then [time-util] Check period from 2018-02-03 01:00:00 to 2019-02-02 12:01:10 is 364 DAYS using date time pattern yyyy-MM-dd HH:mm:ss
    Then [time-util] Check period from 2019-02-03 01:02:12 to 2019-02-03 23:59:10 is 22 HOURS using date time pattern yyyy-MM-dd HH:mm:ss
    Given var expected="116"
    Then [time-util] Check period from 2019-02-03 22:02:12 to 2019-02-03 23:59:10 is #[expected] MINUTES using date time pattern yyyy-MM-dd HH:mm:ss
    Given var expected="58"
    Then [time-util] Check period from 2019-02-03 23:58:12 to 2019-02-03 23:59:10 is #[expected] SECONDS using date time pattern yyyy-MM-dd HH:mm:ss
    Then [time-util] Check period from 2019-02-03 01:02:12 to 2019-02-03 23:59:10 doesn't match 36 HOURS using date time pattern yyyy-MM-dd HH:mm:ss
    # Use zone
    Then [time-util] Check period from 2019-02-03 23:58:12+0200 to 2019-02-04 01:59:10+0300 is 1 HOURS using date time pattern yyyy-MM-dd HH:mm:ssZ

  Scenario: Match two dates
    Then [time-util] Check period from 2019-01-03 to 2019-02-03 is 1 MONTHS using date pattern yyyy-MM-dd
    Then [time-util] Check period from 2018-01-03 to 2019-02-03 is 1 YEARS using date pattern yyyy-MM-dd
    Then [time-util] Check period from 2018-02-03 to 2019-02-03 is 365 DAYS using date pattern yyyy-MM-dd
    Then [time-util] Check period from 2020-02-03 to 2021-02-03 is 1 YEARS using date pattern yyyy-MM-dd
    Then [time-util] Check period from 2020-02-03 to 2021-02-03 is 366 DAYS using date pattern yyyy-MM-dd
    Then [time-util] Check period from 2020-02-03 to 2021-02-03 doesn't match 36 DAYS using date pattern yyyy-MM-dd

  Scenario: Format date times from current date
    * var currentMillis="#[now]"
    And [time-util] date var years=from millis #[currentMillis] PLUS 15 YEARS with format pattern=yyyy-MM-dd HH:mm:ss
    And [time-util] date var days=from millis #[currentMillis] PLUS 15 DAYS with format pattern=yyyy-MM-dd HH:mm:ss
    And [time-util] date var daysBefore=from millis #[currentMillis] MINUS 15 DAYS with format pattern=yyyy-MM-dd HH:mm:ss
    And [time-util] date var months=from millis #[currentMillis] PLUS 15 MONTHS with format pattern=yyyy-MM-dd HH:mm:ss
    And [time-util] date var minutes=from millis #[currentMillis] PLUS 15 MINUTES with format pattern=yyyy-MM-dd HH:mm:ss
    Given [time-util] date var currentTime=from millis #[currentMillis] PLUS 0 YEARS with format pattern=yyyy-MM-dd HH:mm:ss

    Then [time-util] Check period from #[currentTime] to #[years] is 15 YEARS using date time pattern yyyy-MM-dd HH:mm:ss
    Then [time-util] Check period from #[currentTime] to #[days] is 15 DAYS using date time pattern yyyy-MM-dd HH:mm:ss
    Then [time-util] Check period from #[currentTime] to #[daysBefore] is -15 DAYS using date time pattern yyyy-MM-dd HH:mm:ss
    Then [time-util] Check period from #[daysBefore] to #[currentTime] is 15 DAYS using date time pattern yyyy-MM-dd HH:mm:ss
    Then [time-util] Check period from #[currentTime] to #[months] is 15 MONTHS using date time pattern yyyy-MM-dd HH:mm:ss
    Then [time-util] Check period from #[currentTime] to #[minutes] is 15 MINUTES using date time pattern yyyy-MM-dd HH:mm:ss

  Scenario: Format dates from current date
    * var currentMillis="#[now]"
    Given [time-util] date var currentDate=from millis #[currentMillis] PLUS 0 YEARS with format pattern=yyyy-MM-dd
    And [time-util] date var futureDateYears=from millis #[currentMillis] PLUS 15 YEARS with format pattern=yyyy-MM-dd
    And [time-util] date var futureDateDays=from millis #[currentMillis] PLUS 1 DAYS with format pattern=yyyy-MM-dd
    And [time-util] date var pastDateDays=from millis #[currentMillis] MINUS 31 DAYS with format pattern=yyyy-MM-dd

    Then [time-util] Check period from #[currentDate] to #[futureDateYears] is 15 YEARS using date pattern yyyy-MM-dd
    And [time-util] Check period from #[currentDate] to #[futureDateDays] is 1 DAYS using date pattern yyyy-MM-dd
    And [time-util] Check period from #[currentDate] to #[pastDateDays] is -31 DAYS using date pattern yyyy-MM-dd
    And [time-util] Check period from #[pastDateDays] to #[currentDate] is 31 DAYS using date pattern yyyy-MM-dd

  Scenario: Format dates from custom date time
    Given [time-util] date var currentTime=from date 2020-04-28 18:05:58 PLUS 0 YEARS with format pattern=yyyy-MM-dd HH:mm:ss
    And [time-util] date var years=from date 2020-04-28 18:05:58 PLUS 1 YEARS with format pattern=yyyy-MM-dd HH:mm:ss
    And [time-util] date var months=from date 2020-04-28 18:05:58 PLUS 1 MONTHS with format pattern=yyyy-MM-dd HH:mm:ss
    And [time-util] date var days=from date 2020-04-28 18:05:58 PLUS 1 DAYS with format pattern=yyyy-MM-dd HH:mm:ss
    And [time-util] date var minutes=from date 2020-04-28 18:05:58 PLUS 1 MINUTES with format pattern=yyyy-MM-dd HH:mm:ss
    And [time-util] date var seconds=from date #[currentTime] PLUS 2 SECONDS with format pattern=yyyy-MM-dd HH:mm:ss
    And [time-util] date var secondsBefore=from date #[minutes] MINUS 61 SECONDS with format pattern=yyyy-MM-dd HH:mm:ss

    Then [time-util] Check period from #[currentTime] to #[years] is 1 YEARS using date time pattern yyyy-MM-dd HH:mm:ss
    Then [time-util] Check period from #[currentTime] to #[days] is 1 DAYS using date time pattern yyyy-MM-dd HH:mm:ss
    Then [time-util] Check period from #[currentTime] to #[months] is 1 MONTHS using date time pattern yyyy-MM-dd HH:mm:ss
    Then [time-util] Check period from #[currentTime] to #[minutes] is 1 MINUTES using date time pattern yyyy-MM-dd HH:mm:ss
    Then [time-util] Check period from #[currentTime] to #[seconds] is 0 MINUTES using date time pattern yyyy-MM-dd HH:mm:ss
    Then [time-util] Check period from #[currentTime] to #[seconds] is 2 SECONDS using date time pattern yyyy-MM-dd HH:mm:ss
    Then [time-util] Check period from #[currentTime] to #[secondsBefore] is 0 YEARS using date time pattern yyyy-MM-dd HH:mm:ss
    Then [time-util] Check period from #[currentTime] to #[secondsBefore] is 0 MINUTES using date time pattern yyyy-MM-dd HH:mm:ss
    Then [time-util] Check period from #[currentTime] to #[secondsBefore] is -1 SECONDS using date time pattern yyyy-MM-dd HH:mm:ss

  Scenario: Set date in millis param
    * [time-util] date millis var ts=from date 2021-03-07 18:44:27.345+0000 PLUS 1 HOURS with format pattern=yyyy-MM-dd HH:mm:ss.SSSZ
    * [util] Match #[ts] with 1615146267345
    * [time-util] date millis var ts=from date 2021-03-07 18:44:27.345+0200 PLUS 0 HOURS with format pattern=yyyy-MM-dd HH:mm:ss.SSSZ
    * [util] Match #[ts] with 1615135467345
    * [time-util] date millis var ts=from date 2021-03-07 18:44:27.345+0200 MINUS 1 HOURS with format pattern=yyyy-MM-dd HH:mm:ss.SSSZ
    * [util] Match #[ts] with 1615131867345
