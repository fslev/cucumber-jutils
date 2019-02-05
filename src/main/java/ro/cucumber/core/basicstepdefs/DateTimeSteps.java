package ro.cucumber.core.basicstepdefs;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;

@ScenarioScoped
public class DateTimeSteps {
    private Logger log = LogManager.getLogger();
    private DateTimeFormatter formatter;

    @Given("DateTime pattern=\"{cstring}\"")
    public void format(String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Then("DateTime check period from \"{cstring}\" to \"{cstring}\" is {int}year(s)")
    public void compareYears(String date1, String date2, int y) {
        log.info("Check period from {} to {}", date1, date2);
        LocalDateTime localDateTime1 = LocalDateTime.parse(date1, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(date2, formatter);
        assertEquals("Years differ", y, ChronoUnit.YEARS.between(localDateTime1, localDateTime2));
    }

    @Then("DateTime check period from \"{cstring}\" to \"{cstring}\" is {int}day(s)")
    public void compareDays(String date1, String date2, int d) {
        log.info("Check period from {} to {}", date1, date2);
        LocalDateTime localDateTime1 = LocalDateTime.parse(date1, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(date2, formatter);
        assertEquals("Days differ", d, ChronoUnit.DAYS.between(localDateTime1, localDateTime2));
    }

    @Then("DateTime check period from \"{cstring}\" to \"{cstring}\" is {long}hour(s)")
    public void compareHours(String date1, String date2, long h) {
        log.info("Check period from {} to {}", date1, date2);
        LocalDateTime localDateTime1 = LocalDateTime.parse(date1, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(date2, formatter);
        assertEquals("Hours differ", h, Duration.between(localDateTime1, localDateTime2).toHours());
    }

    @Then("DateTime check period from \"{cstring}\" to \"{cstring}\" is {long}minute(s)")
    public void compareMinutes(String date1, String date2, long m) {
        log.info("Check period from {} to {}", date1, date2);
        LocalDateTime localDateTime1 = LocalDateTime.parse(date1, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(date2, formatter);
        assertEquals("Minutes differ", m, Duration.between(localDateTime1, localDateTime2).toMinutes());
    }

    @Then("DateTime check period from \"{cstring}\" to \"{cstring}\" is {long}second(s)")
    public void compareSeconds(String date1, String date2, long s) {
        log.info("Check period from {} to {}", date1, date2);
        LocalDateTime localDateTime1 = LocalDateTime.parse(date1, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(date2, formatter);
        assertEquals("Seconds differ", s, Duration.between(localDateTime1, localDateTime2).toSeconds());
    }
}
