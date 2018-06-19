package stepdefs;

import com.google.inject.Inject;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;

public class Example2 {

    @Inject
    AbstractExample example;

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
    public void blah2() {
        System.out.println("GOT this: " + example.s);
        // Assert.assertEquals("ZEST", arg1);
    }

    @When("use data")
    public void useData() {
        System.out.println("Data: " + data.getData());
    }
}
