package stepdefs;

import cucumber.api.DataTable;
import cucumber.api.java.en.When;

public class Example2 {

    Data data;

    public Example2(Data data) {
        this.data = data;
    }

    public Example2() {

    }

    {
        System.out.println("-------- Instance 2 -----------");
    }

    @When("^blah (.*) and (.*)$")
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

    @When("use data")
    public void useData() {
        System.out.println("Data: " + data.getData());
    }
}
