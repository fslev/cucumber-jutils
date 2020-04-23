Feature: Compare XMLs

  Scenario: Compare XML with xmlChildListLength
    Given param a=
    """
<?xml version="1.0" encoding="UTF-8"?>
<messages>
  <note id="501">
    <to>Tove</to>
    <from>.*</from>
    <heading>Reminder</heading>
    <body>Don't forget me this weekend!</body>
  </note>
  <note id="[0-9]*">
    <to>Jani</to>
    <from>Tove</from>
    <heading>Re: Reminder</heading>
  </note>
</messages>
    """
    And param b=
    """
<?xml version="1.0" encoding="UTF-8"?>
<messages>
  <note id="501">
    <to>Tove</to>
    <from>Jani</from>
    <heading>Reminder</heading>
    <body>Don't forget me this weekend!</body>
  </note>
  <note id="502">
    <to>Jani</to>
    <from>Tove</from>
    <heading>Re: Reminder</heading>
    <body>I will not</body>
  </note>
</messages>
    """
    Then Compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=false, jsonArrayStrictOrder=false, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=false, jsonArrayStrictOrder=false, xmlChildListLength=true, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]


  Scenario: Compare XML with xmlChildListSequence
    Given param a=
    """
<?xml version="1.0" encoding="UTF-8"?>
<messages>
  <note id="502">
    <to>Jani</to>
    <from>Tove</from>
    <heading>.*minder</heading>
    <body>I will not</body>
  </note>
  <note id="501">
    <from>Jani</from>
    <to>Tove</to>
    <body>Don't forget me this weekend!</body>
    <heading>Reminder</heading>
  </note>
</messages>
    """
    And param b=
    """
<?xml version="1.0" encoding="UTF-8"?>
<messages>
  <note id="501">
    <to>Tove</to>
    <from>Jani</from>
    <heading>Reminder</heading>
    <body>Don't forget me this weekend!</body>
  </note>
  <note id="502">
    <to>Jani</to>
    <from>Tove</from>
    <heading>Re: Reminder</heading>
    <body>I will not</body>
  </note>
</messages>
    """
    Then Compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=false, jsonArrayStrictOrder=false, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=false, jsonArrayStrictOrder=false, xmlChildListLength=false, xmlChildListSequence=true, xmlElementNumAttributes=false and message=[_null]

  Scenario: Compare XML with xmlElementNumAttributes
    Given param a=
    """
<?xml version="1.0" encoding="UTF-8"?>
<messages>
  <note id="502">
    <to>Jani</to>
    <from>Tove</from>
    <heading>Re: Reminder</heading>
    <body>I will not</body>
  </note>
  <note id="501">
    <from>Jani</from>
    <to>Tove</to>
    <body>Don't forget me this weekend!</body>
    <heading>Reminder</heading>
  </note>
</messages>
    """
    And param b=
    """
<?xml version="1.0" encoding="UTF-8"?>
<messages>
  <note id="501">
    <to>Tove</to>
    <from>Jani</from>
    <heading>Reminder</heading>
    <body>Don't forget me this weekend!</body>
  </note>
  <note id="502" name="chat">
    <to>Jani</to>
    <from>Tove</from>
    <heading>Re: Reminder</heading>
    <body>I will not</body>
  </note>
</messages>
    """
    Then Compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=false, jsonArrayStrictOrder=false, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=false, jsonArrayStrictOrder=false, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=true and message=[_null]

  Scenario: Compare XML with extra expected attributes
    Given param a=
    """
<?xml version="1.0" encoding="UTF-8"?>
<messages>
  <note id="502" contract="1000">
    <to>Jani</to>
    <from>Tove</from>
    <heading>Re: Reminder</heading>
    <body>I will not</body>
  </note>
  <note id="501">
    <from>Jani</from>
    <to>Tove</to>
    <body>Don't forget me this weekend!</body>
    <heading>Reminder</heading>
  </note>
</messages>
    """
    And param b=
    """
<?xml version="1.0" encoding="UTF-8"?>
<messages>
  <note id="501">
    <to>Tove</to>
    <from>Jani</from>
    <heading>Reminder</heading>
    <body>Don't forget me this weekend!</body>
  </note>
  <note id="502">
    <to>Jani</to>
    <from>Tove</from>
    <heading>Re: Reminder</heading>
    <body>I will not</body>
  </note>
</messages>
    """
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=false, jsonArrayStrictOrder=false, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]

  Scenario: Compare XML with assign symbols
    Given param a=
    """
<?xml version="1.0" encoding="UTF-8"?>
<messages>
  <note id="502" contract="~[contract]">
    <to>Jani</to>
    <from>~[from]</from>
    <heading>Re: Reminder</heading>
    <body>I will not</body>
  </note>
  <note id="501">
    <from>Jani</from>
    <to>Tove</to>
    <body>Don't forget me this weekend!</body>
    <heading>Reminder</heading>
  </note>
</messages>
    """
    And param b=
    """
<?xml version="1.0" encoding="UTF-8"?>
<messages>
  <note id="501">
    <to>Tove</to>
    <from>Jani</from>
    <heading>Reminder</heading>
    <body>Don't forget me this weekend!</body>
  </note>
  <note id="502" contract="foo bar 1000">
    <to>Jani</to>
    <from>Tove</from>
    <heading>Re: Reminder</heading>
    <body>I will not</body>
  </note>
</messages>
    """
    Then Compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=false, jsonArrayStrictOrder=false, xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=true and message=[_null]
    And COMPARE #[contract] with "foo bar 1000"
    And COMPARE #[from] with "Tove"
    Then Negative compare #[a] against #[b] via jsonNonExtensibleObject=false, jsonNonExtensibleArray=false, jsonArrayStrictOrder=false, xmlChildListLength=false, xmlChildListSequence=true, xmlElementNumAttributes=false and message=Interesting


  Scenario: Check unintentional regex chars at XML compare
  This test scenario is valid only if logger is set to debug LEVEL or bellow
    * load all scenario props from dir "xml/regex_chars"
      # This should not log any warning related to regular expressions
    And Negative COMPARE <xml><a>foobar</a></xml> with "<xml><a>[0-9]</a></xml>"
    # This should log regex related warning messages
    And Negative COMPARE <xml><a>[0-9]</a></xml> with "<xml><a>[0-9]</a></xml>"
    And Negative COMPARE #[test1] with "<xml><a>[0-9]</a></xml>"