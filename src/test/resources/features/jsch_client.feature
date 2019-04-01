@Ignore
Feature: Test JSCH feature

  Scenario: Connect via SSH to a machine and execute ls command
    Given JSCH connection from properties file "config/jsch/jsch.properties"
    Then JSCH execute command "hostname -f" and check response="vm-test\d+.sandbox.lan"
    Then JSCH execute command "echo test" and check response="test"
    Then JSCH execute command "ls -alh" and check response=".*"
    And JSCH disconnect