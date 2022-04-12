@ignore @jsch_cleanup
Feature: Test JSCH feature

  Scenario: Connect via SSH to a machine and execute ls command
    Given [jsch-util] Load config from properties file "config/jsch/jsch.properties"
    Then [jsch-util] Execute hostname -f and check response=vm-test\d+.sandbox.lan
    Then [jsch-util] Execute echo test and check response=test
    Then [jsch-util] Execute ls -alh and check response=.*
    Then [jsch-util] Execute invalidcommand and check response=.*command not found.*
    Given var multilineCmd=
    """
    a='foo'
    b='bar'
    echo "$a $b"
    """
    Then [jsch-util] Execute #[multilineCmd] and check response=foo bar

