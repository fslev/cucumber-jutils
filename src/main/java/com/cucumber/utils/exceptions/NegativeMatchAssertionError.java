package com.cucumber.utils.exceptions;

public class NegativeMatchAssertionError extends AssertionError {
    public NegativeMatchAssertionError(String detailedMessage) {
        super("Elements match: " + detailedMessage);
    }
}
