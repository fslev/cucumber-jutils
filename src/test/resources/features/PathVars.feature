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
    * [util] Match #[x/a] with 1
    * [util] Match \Q#[/x/a]\E with #[/x/a]
    * [util] Match \Q#[x/a/]\E with #[x/a/]
    * [util] Match #[x/b] with true
    * [util] Match #[x/c] with null
    * [util] Match #[x/d] with test
    * [util] Match \Q#[x/e]\E with \d+
    * [util] Match #[x/f] with Luna
    * [util] Match #[var1] with Luna
    * [util] Match #[x/g/g1] with some value
    * [util] Match #[x/g/g2/0] with val1
    * [util] Match #[x/g/g2/1] with 1
    * [util] Match #[x/g/g2/2] with val2
    * [util] Match #[x/g/g2/3] with null
    * [util] Match #[x/g/g2/4] with true
    * [util] Match \Q#[x/g/g2/5]\E with #[x/g/g2/5]
    * [util] Match #[x/g/g3/g31] with google
    * [util] Match #[x/g/g3/g32/g321/g3211/0] with {"ab.ra":"test"}
    * [util] Match #[x/g/g3/g32/g321/g3211/0/ab.ra] with test
    * [util] Match #[x/g/g3/g32/g321/g3211/1] with false
    * [util] Match #[x/g/g3/g32/g321/g3211~1x] with 1022
    * var empty=""
    * [util] Match #[x/g/g3/g32/g321/g3212] with #[empty]

    * var x=
    """json
    {"book":{
        "details":{"title":"Moby Dick"}
      }
    }
    """
    * [util] Match #[x/book/details/title] with Moby Dick


  Scenario: Test path variables from matches
    *  var x=
    """
    {"a":{"a1":{"a11":1}}}
    """
    * [util] Match {"a1":~[var1]} with #[x/a]
    * [util] Match #[var1] with {"a11":1}
    * [util] Match #[var1/a11] with 1

  Scenario: Test path variables from dir loaded variables
    * load vars from dir "props"
    * [util] Match #[actualLargeJson/body/events/0/payload/type] with TEMPLATE_DONE
    * [util] Match #[foo1/foo11] with foo11 test


