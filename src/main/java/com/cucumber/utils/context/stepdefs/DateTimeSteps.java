package com.cucumber.utils.context.stepdefs;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.utils.ScenarioUtils;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;

@ScenarioScoped
public class DateTimeSteps {
    private DateTimeFormatter formatter;
    @Inject
    private ScenarioUtils logger;
    @Inject
    ScenarioProps scenarioProps;

    public enum Operation {
        PLUS, MINUS
    }

    @Given("DateTime pattern=\"{}\"")
    public void format(String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Then("DateTime check period from \"{}\" to \"{}\" is {}year(s)")
    public void compareYears(String date1, String date2, Object y) {
        logger.log("Check period from '{}' to '{}' is {}years", date1, date2, y);
        LocalDateTime localDateTime1 = LocalDateTime.parse(date1, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(date2, formatter);
        assertEquals("Years differ", Long.parseLong(y.toString()), ChronoUnit.YEARS.between(localDateTime1, localDateTime2));
    }

    @Then("DateTime check period from \"{}\" to \"{}\" is {}day(s)")
    public void compareDays(String date1, String date2, Object d) {
        logger.log("Check period from '{}' to '{}' is {}days", date1, date2, d);
        LocalDateTime localDateTime1 = LocalDateTime.parse(date1, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(date2, formatter);
        assertEquals("Days differ", Long.parseLong(d.toString()), ChronoUnit.DAYS.between(localDateTime1, localDateTime2));
    }

    @Then("DateTime check period from \"{}\" to \"{}\" is {}hour(s)")
    public void compareHours(String date1, String date2, Object h) {
        logger.log("Check period from '{}' to '{}' is {}hours", date1, date2, h);
        LocalDateTime localDateTime1 = LocalDateTime.parse(date1, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(date2, formatter);
        assertEquals("Hours differ", Long.parseLong(h.toString()), Duration.between(localDateTime1, localDateTime2).toHours());
    }

    @Then("DateTime check period from \"{}\" to \"{}\" is {}minute(s)")
    public void compareMinutes(String date1, String date2, Object m) {
        logger.log("Check period from '{}' to '{}' is {}mins", date1, date2, m);
        LocalDateTime localDateTime1 = LocalDateTime.parse(date1, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(date2, formatter);
        assertEquals("Minutes differ", Long.parseLong(m.toString()), Duration.between(localDateTime1, localDateTime2).toMinutes());
    }

    @Then("DateTime check period from \"{}\" to \"{}\" is {}second(s)")
    public void compareSeconds(String date1, String date2, Object s) {
        logger.log("Check period from '{}' to '{}' is {}seconds", date1, date2, s);
        LocalDateTime localDateTime1 = LocalDateTime.parse(date1, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(date2, formatter);
        assertEquals("Seconds differ", Long.parseLong(s.toString()), Duration.between(localDateTime1, localDateTime2).getSeconds());
    }

    @Then("Date check period from \"{}\" to \"{}\" is {}year(s)")
    public void compareYearsFromDates(String date1, String date2, Object y) {
        logger.log("Check date period from '{}' to '{}' is {}years", date1, date2, y);
        LocalDate localDate1 = LocalDateTime.parse(date1, formatter).toLocalDate();
        LocalDate localDate2 = LocalDateTime.parse(date2, formatter).toLocalDate();
        assertEquals("Years differ", Long.parseLong(y.toString()), ChronoUnit.YEARS.between(localDate1, localDate2));
    }

    @Then("Date check period from \"{}\" to \"{}\" is {}months(s)")
    public void compareMonthsFromDates(String date1, String date2, Object m) {
        logger.log("Check date period from '{}' to '{}' is {}years", date1, date2, m);
        LocalDate localDate1 = LocalDateTime.parse(date1, formatter).toLocalDate();
        LocalDate localDate2 = LocalDateTime.parse(date2, formatter).toLocalDate();
        assertEquals("Years differ", Long.parseLong(m.toString()), ChronoUnit.MONTHS.between(localDate1, localDate2));
    }

    @Then("Date check period from \"{}\" to \"{}\" is {}day(s)")
    public void compareDaysFromDates(String date1, String date2, Object d) {
        logger.log("Check date period from '{}' to '{}' is {}days", date1, date2, d);
        LocalDate localDate1 = LocalDateTime.parse(date1, formatter).toLocalDate();
        LocalDate localDate2 = LocalDateTime.parse(date2, formatter).toLocalDate();
        assertEquals("Days differ", Long.parseLong(d.toString()), ChronoUnit.DAYS.between(localDate1, localDate2));
    }

    @Then("date param {}=\"now {} {} {}\" with format pattern={}")
    public void formatDate(String param, Operation operation, int value, ChronoUnit chronoUnit, String formatPattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatPattern);
        switch (operation) {
            case MINUS:
                scenarioProps.put(param, LocalDateTime.now().minus(value, chronoUnit).format(dateTimeFormatter));
            case PLUS:
                scenarioProps.put(param, LocalDateTime.now().plus(value, chronoUnit).format(dateTimeFormatter));
        }
    }


}
