package com.cucumber.utils.context.stepdefs;

import com.cucumber.utils.context.ScenarioUtils;
import com.cucumber.utils.context.props.ScenarioProps;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertEquals;

@ScenarioScoped
public class DateTimeSteps {
    @Inject
    private ScenarioUtils logger;
    @Inject
    private ScenarioProps scenarioProps;

    public enum Operation {
        PLUS, MINUS
    }

    @Then("Check period from \"{}\" to \"{}\" is {} {} using date pattern {}")
    public void compareDates(String date1, String date2, long value, ChronoUnit chronoUnit, String pattern) {
        logger.log("Check date period from '{}' to '{}' is {}{} using date pattern '{}'", date1, date2, value, chronoUnit, pattern);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate localDate1 = LocalDate.parse(date1, dateTimeFormatter);
        LocalDate localDate2 = LocalDate.parse(date2, dateTimeFormatter);
        assertEquals(chronoUnit + " differ", value, chronoUnit.between(localDate1, localDate2));
    }

    @Then("Check period from \"{}\" to \"{}\" is {} {} using date time pattern {}")
    public void compareDateTimes(String date1, String date2, long value, ChronoUnit chronoUnit, String pattern) {
        logger.log("Check date period from '{}' to '{}' is {}{} using date time pattern '{}'", date1, date2, value, chronoUnit, pattern);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault());
        ZonedDateTime zonedDateTime1 = ZonedDateTime.parse(date1, dateTimeFormatter);
        ZonedDateTime zonedDateTime2 = ZonedDateTime.parse(date2, dateTimeFormatter);
        assertEquals(chronoUnit + " differ", value, chronoUnit.between(zonedDateTime1, zonedDateTime2));
    }

    @Then("Check period from \"{}\" to \"{}\" doesn't match {} {} using date pattern {}")
    public void negativeCompareDates(String date1, String date2, long value, ChronoUnit chronoUnit, String pattern) {
        logger.log("Negative check date period from '{}' to '{}' is {}{} using date pattern '{}'", date1, date2, value, chronoUnit, pattern);
        try {
            compareDates(date1, date2, value, chronoUnit, pattern);
        } catch (AssertionError e) {
            logger.log("Negative compare passes {}", e.getMessage());
            return;
        }
        throw new AssertionError("Compared dates match");
    }

    @Then("Check period from \"{}\" to \"{}\" doesn't match {} {} using date time pattern {}")
    public void negativeCompareDateTimes(String date1, String date2, long value, ChronoUnit chronoUnit, String pattern) {
        logger.log("Negative check date period from '{}' to '{}' is {}{} using date time pattern '{}'", date1, date2, value, chronoUnit, pattern);
        try {
            compareDateTimes(date1, date2, value, chronoUnit, pattern);
        } catch (AssertionError e) {
            logger.log("Negative compare passes {}", e.getMessage());
            return;
        }
        throw new AssertionError("Compared dates match");
    }

    @Then("date param {}=\"from millis {} {} {} {}\" with format pattern={}")
    public void setDateFormattedParam(String param, Long millis, Operation operation, int value, ChronoUnit chronoUnit, String formatPattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatPattern).withZone(ZoneId.systemDefault());
        switch (operation) {
            case MINUS:
                scenarioProps.put(param, ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())
                        .minus(value, chronoUnit).format(dateTimeFormatter));
                break;
            case PLUS:
                scenarioProps.put(param, ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())
                        .plus(value, chronoUnit).format(dateTimeFormatter));
                break;
        }
        logger.log("Date param {}='{}'", param, scenarioProps.get(param));
    }

    @Then("date param {}=\"from date {} {} {} {}\" with format pattern={}")
    public void setDateFormattedParam(String param, String date, Operation operation, int value, ChronoUnit chronoUnit, String formatPattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatPattern).withZone(ZoneId.systemDefault());
        switch (operation) {
            case MINUS:
                scenarioProps.put(param, ZonedDateTime.parse(date, dateTimeFormatter).minus(value, chronoUnit).format(dateTimeFormatter));
                break;
            case PLUS:
                scenarioProps.put(param, ZonedDateTime.parse(date, dateTimeFormatter).plus(value, chronoUnit).format(dateTimeFormatter));
                break;
        }
        logger.log("Date param {}='{}'", param, scenarioProps.get(param));
    }

    @Then("date millis param {}=\"from date {} {} {} {}\" with format pattern={}")
    public void setDateInMillisParam(String param, String date, Operation operation, int value, ChronoUnit chronoUnit, String formatPattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatPattern).withZone(ZoneId.systemDefault());
        switch (operation) {
            case MINUS:
                scenarioProps.put(param, ZonedDateTime.parse(date, dateTimeFormatter).minus(value, chronoUnit).toInstant().toEpochMilli());
                break;
            case PLUS:
                scenarioProps.put(param, ZonedDateTime.parse(date, dateTimeFormatter).plus(value, chronoUnit).toInstant().toEpochMilli());
                break;
        }
        logger.log("Date millis param {}='{}'", param, scenarioProps.get(param));
    }
}