package stepdefs;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class Example2 {

    {
        System.out.println("-------- Instance 2 -----------");
    }

    @Given("^blah (.*) and (.*)$")
    public void blah(String a, String b) {
        System.out.println();
        System.out.print(a);
        System.out.print(b);
        System.out.println();
    }

    @When("blah2")
    public void blah2(DataTable z) {
        System.out.println("Test: " + z.getGherkinRows().get(0).getCells());
        // Assert.assertEquals("ZEST", arg1);
    }
}
