@ignore @jsch_cleanup
Feature: Test JSCH feature

  Scenario: Connect via SSH to a machine and execute ls command
    Given JSCH connection from properties file "config/jsch/jsch.properties"
    Then JSCH execute command "hostname -f" and check response="vm-test\d+.sandbox.lan"
    Then JSCH execute command "echo test" and check response="test"
    Then JSCH execute command "ls -alh" and check response=".*"
    Then JSCH execute command "invalidcommand" and check response=".*command not found.*"
    Given var multilineCmd=
    """
    a='foo'
    b='bar'
    echo "$a $b"
    """
    Then JSCH execute command "#[multilineCmd]" and check response="foo bar"

