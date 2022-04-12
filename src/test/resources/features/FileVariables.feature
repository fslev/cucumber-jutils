Feature: Property File feature

  Scenario: Test property files are successfully loaded from dir inside scenario variables
    When load vars from dir "props"
    Then [util] Match 1 with #[a]
    And [util] Match 2 with #[b]
    And [util] Match 3 with #[c]
    And [util] Match 4 with #[d]
    And [util] Match 6 with #[f]

  Scenario: Test property files are successfully loaded from each file inside scenario variables
    When load vars from file "props/dir1/a.xml"
    When load vars from file "props/dir1/b.json"
    When load vars from file "props/dir1/c.txt"
    When load vars from file "props/dir1/d.txt"
    When load vars from file "props/f.json"
    When load vars from file "props/dir1/p.properties"
    When load vars from file "props/dir1/y.yml"

    Then [util] Match 1 with #[a]
    And [util] Match 2 with #[b]
    And [util] Match 3 with #[c]
    And [util] Match 4 with #[d]
    And [util] Match 6 with #[f]
    And [util] Match lorem with #[bar]
    And [util] Match foo test with #[foo]

  Scenario: Test error when duplicated scenario variable files are detected
    * Load duplicated scenario vars from dir duplicated_file_props and expect exception

  Scenario: Test error when duplicated scenario variables are detected when loading from dir
    * Load duplicated scenario vars from dir duplicated_props/test1 and expect exception
    * Load duplicated scenario vars from dir duplicated_props/test2 and expect exception
    * Load duplicated scenario vars from dir duplicated_props/test3 and expect exception
    * Load duplicated scenario vars from dir duplicated_props/test4 and expect exception

  Scenario: Load scenario variable from invalid file type
    * Load scenario variable from invalid file type=props/invalid.extension
    # Load file from unknown location
    * Load scenario variable from unknown location=inexistent/test.json
    * Load scenario variables from unknown location=inexistent