Feature: Compare XMLs

  Scenario: Compare XMLs simple
    Given param a=
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
    Then Compare #[a] against #[b] via xmlChildListLength=false, xmlChildListSequence=false, xmlElementNumAttributes=false and message=[_null]