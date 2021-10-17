Feature: Path variables feature

  Scenario: Test path variables
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
                {"abra": "test"},
                false,
                null,
                "zoro"
              ]
            }
          }
        }
      }
    }
    """
    * Match #[x.a] with "1"
    * Match #[x.b] with "true"
    * Match #[x.c] with "null"
    * Match #[x.d] with "test"
    * Match \Q#[x.e]\E with "\d+"
    * Match #[x.f] with "Luna"
    * Match #[var1] with "Luna"
    * Match #[x.g.g1] with "some value"
    * Match #[x.g.g2[3]] with "val2"

