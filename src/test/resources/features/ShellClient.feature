@ignore
Feature: Test SHELL feature

  Scenario: Run shell command and check output
    * [shell-util] Execute ["bash","-c","ls -alh"] and check response=.*
    * [shell-util] Execute ["bash","-c","ls -alh"] and check response is
    """
    .*
    """
    * [shell-util] Execute ["bash","-c","echo foobar"] and check response=foobar\n
    * [shell-util] Execute ["bash","-c","invalid command"] and check response=.*command not found.*
    * [shell-util] Execute ["bash","-c","ls -alh"] and check 10s until response=.*
    * [shell-util] Execute ["bash","-c","ls -alh"] and check 10s until response is
    """
    .*
    """
    Given var cmd=
    """
    a=foo && echo $a
    """
    * [shell-util] Execute ["bash","-c","#[cmd]"] and check 0s until response=foo\n

