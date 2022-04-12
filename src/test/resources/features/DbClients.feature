@ignore
Feature: Test clients

  Scenario: Test MYSQL client select with empty result set in string format
    Given [sql-util] Load data source from file path "config/database/mysql.properties"
    Then [sql-util] Execute query select * from gift_wf_2012 where person_id=0 order by person_id asc limit 3 and check result=[]

  Scenario: Test MYSQL client select with empty result set in data table format
    Given [sql-util] Load data source from file path "config/database/mysql.properties"
    Then [sql-util] Execute query select * from gift_wf_2012 where person_id=0 order by person_id asc limit 3 and check result is
      |  |

  Scenario: Test MYSQL client select and match with table
    Given [sql-util] Load data source from file path "config/database/mysql.properties"
    Then [sql-util] Execute query select * from gift_wf_2012 order by person_id asc limit 3 and check result is
      | person_id | gift                                                 |
      | .*        | .*                                                   |
      | 21189037  | fun & joy for everybody!                             |
      | 21193939  | Leica M9-P Hermes Edition: http://vimeo.com/42108675 |

  Scenario: Test MYSQL client select and match with json
    Given [sql-util] Load data source from file path "config/database/mysql.properties"
    Then [sql-util] Execute query select * from gift_wf_2012 order by person_id asc limit 1 and check result=[{"person_id": "*", "gift": ".*"}]

  Scenario: Test MYSQL client select and match with poll
    Given [sql-util] Load data source from file path "config/database/mysql.properties"
    Then [sql-util] Execute query select * from gift_wf_2012 order by person_id asc limit 2 and check 1s until result is
      | person_id | gift |
      | .*        | .*   |
      | 16        | null |

  Scenario: TEST MYSQL client delete
    Given [sql-util] Load data source from file path "config/database/mysql.properties"
    Then [sql-util] Execute update delete from gift_wf_2012 where person_id<100

  Scenario: Test MYSQL client simple insert
    Given [sql-util] Load data source from file path "config/database/mysql.properties"
    Then [sql-util] Execute update insert into gift_wf_2012 (person_id,gift) values ('10','some test')

  Scenario: Test MYSQL client simple insert with tabular data
    Given [sql-util] Load data source from file path "config/database/mysql.properties"
    Then [sql-util] INSERT into table gift_wf_2012 the following data
      | person_id | gift              |
      | 14        | http://heheheh.ro |
      | 16        | null              |
      | 17        | wow               |

  Scenario: Test MYSQL update table with tabular data
    Given [sql-util] Load data source from file path "config/database/mysql.properties"
    Then [sql-util] UPDATE table gift_wf_2012 WHERE person_id=124325 and gift='wa' with the following data
      | gift |
      | null |

  Scenario: Test POSTGRESQL client
    Given [sql-util] Load data source from file path "config/database/psql.properties"
    Then [sql-util] Execute query select * from external_domain order by id asc limit 2 and check result is
      | provisioning_id | version |
      | 3929585         | 261951  |
      | 3929581         | 262173  |

  Scenario: Test SYBASE client
    * var expected="1549"
    Given [sql-util] Load data source from file path "config/database/sybase.properties"
    Then [sql-util] Execute query select TOP 2 domainname, adminc from domain order by domain_id asc and check result is
      | domainname                | adminc      |
      | hypothekenfinanzierung.de | #[expected] |
      | asb-bw.de                 | 1568        |






