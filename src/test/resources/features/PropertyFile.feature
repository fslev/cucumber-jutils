Feature: Property File feature

  Scenario: Test property files are successfully loaded from dir inside scenario properties
    When load all scenario props from dir "props"
    Then COMPARE 1 with "#[a]"
    And COMPARE 2 with "#[b]"
    And COMPARE 3 with "#[c]"
    And COMPARE 4 with "#[d]"
    And COMPARE 6 with "#[f]"

  Scenario: Test property files are successfully loaded from each file inside scenario properties
    When load scenario props from file "props/dir1/a.xml"
    When load scenario props from file "props/dir1/b.json"
    When load scenario props from file "props/dir1/c.txt"
    When load scenario props from file "props/dir1/d.property"
    When load scenario props from file "props/f.json"

    Then COMPARE 1 with "#[a]"
    And COMPARE 2 with "#[b]"
    And COMPARE 3 with "#[c]"
    And COMPARE 4 with "#[d]"
    And COMPARE 6 with "#[f]"


  Scenario: Test error when duplicated property files are detected
    * Load duplicated scenario props from dir props/dir2 and expect exception