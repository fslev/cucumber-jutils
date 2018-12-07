@Ignore
Feature: Test clients

  Scenario: Test MYSQL client
    Given MYSQL data source from file path database/mysql.properties
    Then MYSQL execute query "select * from domains order by id asc limit 3" and compare result with
      | id | name                                         | type   |
      | .* | tdm-test-generic-error2030416905.bike        | NATIVE |
      | .* | tdm-test-1943744146-fastikk-test-lop-ok.bike | NATIVE |
      | .* | tdm-test-transfer-pending2030416905.bike     | NATIVE |





