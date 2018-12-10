@Ignore
Feature: Test clients

  Scenario: Test MYSQL client
    Given SQL data source from file path "config/database/mysql.properties"
    Then SQL execute query "select * from domains order by id asc limit 3" and compare result with
      | id | name                                         | type   |
      | .* | tdm-test-generic-error2030416905.bike        | NATIVE |
      | .* | tdm-test-1943744146-fastikk-test-lop-ok.bike | NATIVE |
      | .* | tdm-test-transfer-pending2030416905.bike     | NATIVE |

  Scenario: Test POSTGRESQL client
    Given SQL data source from file path "config/database/psql.properties"
    Then SQL execute query "select * from external_domain order by id asc limit 2" and compare result with
      | provisioning_id | version |
      | 3929585         | 261951  |
      | 3929581         | 262173  |

  Scenario: Test SYBASE client
    Given SQL data source from file path "config/database/sybase.properties"
    Then SQL execute query "select TOP 2 domainname, adminc from domain order by domain_id asc" and compare result with
      | domainname                | adminc |
      | hypothekenfinanzierung.de | 1549   |
      | asb-bw.de                 | 1568   |






