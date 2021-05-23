@ignore
Feature: Test SHELL feature

  Scenario: Run shell command and check output
    * SHELL execute command "ls -alh" and check response=".*"
    * SHELL execute command "ls -alh" and check response=
    """
    .*
    """
    * SHELL execute command "echo foobar" and check response="foobar\n"
    * SHELL execute command "invalidcommand" and check response=".*command not found.*"
    Given var multilineCmd=
    """
    a='foo'
    b='bar'
    echo "$a $b"
    """
    Then SHELL execute command "#[multilineCmd]" and check response="foo bar\n"

