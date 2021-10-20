Feature: Path variables feature

  Scenario: Test path variables from defined variables
    * var x=
    """json
    {
      "a": 1,
      "b": true,
      "c": null,
      "d": "test",
      "e": "\\d+",
      "f":"~[var1]",
      "g": {
        "g1": "some value",
        "g2": ["val1",1,"val2",null,true],
        "g3": {
          "g31": "google",
          "g32": {
            "g321": {
              "g3211": [
                {"ab.ra": "test"},
                false,
                null,
                "zoro"
              ],
              "g3211/x":1022,
              "g3212":""
            }
          }
        }
      }
    }
    """
    * Match #[x/a] with "1"
    * Match \Q#[/x/a]\E with "#[/x/a]"
    * Match \Q#[x/a/]\E with "#[x/a/]"
    * Match #[x/b] with "true"
    * Match #[x/c] with "null"
    * Match #[x/d] with "test"
    * Match \Q#[x/e]\E with "\d+"
    * Match #[x/f] with "Luna"
    * Match #[var1] with "Luna"
    * Match #[x/g/g1] with "some value"
    * Match #[x/g/g2/0] with "val1"
    * Match #[x/g/g2/1] with "1"
    * Match #[x/g/g2/2] with "val2"
    * Match #[x/g/g2/3] with "null"
    * Match #[x/g/g2/4] with "true"
    * Match \Q#[x/g/g2/5]\E with "#[x/g/g2/5]"
    * Match #[x/g/g3/g31] with "google"
    * Match #[x/g/g3/g32/g321/g3211/0] with "{"ab.ra":"test"}"
    * Match #[x/g/g3/g32/g321/g3211/0/ab.ra] with "test"
    * Match #[x/g/g3/g32/g321/g3211/1] with "false"
    * Match #[x/g/g3/g32/g321/g3211~1x] with "1022"
    * Match #[x/g/g3/g32/g321/g3212] with ""


  Scenario: Test path variables from matches
    *  var x=
    """
    {"a":{"a1":{"a11":1}}}
    """
    * Match {"a1":~[var1]} with "#[x/a]"
    * Match #[var1] with "{"a11":1}"
    * Match #[var1/a11] with "1"

  Scenario: Test path variables from dir loaded variables
    * load vars from dir "props"
    * Match #[actualLargeJson/body/events/0/payload/type] with "TEMPLATE_DONE"
    * Match #[foo1/foo11] with "foo11 test"


