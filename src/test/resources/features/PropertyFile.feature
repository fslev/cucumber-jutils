Feature: Property File feature

  Scenario: Test property files are successfully loaded from dir inside scenario properties
    When load all scenario props from dir "props"
    Then Match 1 with "#[a]"
    And Match 2 with "#[b]"
    And Match 3 with "#[c]"
    And Match 4 with "#[d]"
    And Match 6 with "#[f]"

  Scenario: Test property files are successfully loaded from each file inside scenario properties
    When load scenario props from file "props/dir1/a.xml"
    When load scenario props from file "props/dir1/b.json"
    When load scenario props from file "props/dir1/c.txt"
    When load scenario props from file "props/dir1/d.property"
    When load scenario props from file "props/f.json"
    When load scenario props from file "props/dir1/p.properties"
    When load scenario props from file "props/dir1/y.yml"

    Then Match 1 with "#[a]"
    And Match 2 with "#[b]"
    And Match 3 with "#[c]"
    And Match 4 with "#[d]"
    And Match 6 with "#[f]"
    And Match lorem with "#[bar]"
    And Match foo test with "#[foo]"

  Scenario: Test error when duplicated scenario property files are detected
    * Load duplicated scenario props from dir duplicated_file_props and expect exception

  Scenario: Test error when duplicated scenario properties are detected when loading from dir
    * Load duplicated scenario props from dir duplicated_props/test1 and expect exception
    * Load duplicated scenario props from dir duplicated_props/test2 and expect exception
    * Load duplicated scenario props from dir duplicated_props/test3 and expect exception
    * Load duplicated scenario props from dir duplicated_props/test4 and expect exception