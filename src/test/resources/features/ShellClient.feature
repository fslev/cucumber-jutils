@ignore
Feature: Test SHELL feature

  Scenario: Run shell command and check output
    * [shell-util] Execute ls -alh and check response=.*
    * [shell-util] Execute ls -alh and check response is
    """
    .*
    """
    * [shell-util] Execute echo foobar and check response=foobar\n
    * [shell-util] Execute invalidcommand and check response=.*command not found.*
    Given var multilineCmd=
    """
    a='foo'
    b='bar'
    echo "$a $b"
    """
    Then [shell-util] Execute #[multilineCmd] and check response=foo bar\n

