package com.cucumber.utils.context.stepdefs;

import com.cucumber.utils.context.props.ScenarioProps;
import com.cucumber.utils.context.utils.ScenarioUtils;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;

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

    @Then("Check period from \"{}\" to \"{}\" is {} {} using date pattern {}")
    public void compareDates(String date1, String date2, long value, ChronoUnit chronoUnit, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        logger.log("Check date period from '{}' to '{}' is {}{} using date pattern {}", date1, date2, value, chronoUnit, pattern);
        LocalDate localDate1 = LocalDate.parse(date1, dateTimeFormatter);
        LocalDate localDate2 = LocalDate.parse(date2, dateTimeFormatter);
        assertEquals(chronoUnit + " differ", value, chronoUnit.between(localDate1, localDate2));
    }

    @Then("Check period from \"{}\" to \"{}\" is {} {} using date time pattern {}")
    public void compareDateTimes(String date1, String date2, long value, ChronoUnit chronoUnit, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        logger.log("Check date period from '{}' to '{}' is {}{} using date time pattern {}", date1, date2, value, chronoUnit, pattern);
        LocalDateTime localDate1 = LocalDateTime.parse(date1, dateTimeFormatter);
        LocalDateTime localDate2 = LocalDateTime.parse(date2, dateTimeFormatter);
        assertEquals(chronoUnit + " differ", value, chronoUnit.between(localDate1, localDate2));
    }

    @Then("date param {}=\"now {} {} {}\" with format pattern={}")
    public void formatDateNow(String param, Operation operation, int value, ChronoUnit chronoUnit, String formatPattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatPattern);
        switch (operation) {
            case MINUS:
                scenarioProps.put(param, LocalDateTime.now().minus(value, chronoUnit).format(dateTimeFormatter));
            case PLUS:
                scenarioProps.put(param, LocalDateTime.now().plus(value, chronoUnit).format(dateTimeFormatter));
        }
    }

    @Then("date param {}=\"from {} {} {} {}\" with format pattern={}")
    public void setDateFormattedParam(String param, String date, Operation operation, int value, ChronoUnit chronoUnit, String formatPattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatPattern);
        switch (operation) {
            case MINUS:
                scenarioProps.put(param, LocalDateTime.parse(date, dateTimeFormatter).minus(value, chronoUnit).format(dateTimeFormatter));
            case PLUS:
                scenarioProps.put(param, LocalDateTime.parse(date, dateTimeFormatter).plus(value, chronoUnit).format(dateTimeFormatter));
        }
    }
}
