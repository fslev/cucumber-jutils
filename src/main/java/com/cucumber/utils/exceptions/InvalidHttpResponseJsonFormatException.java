package com.cucumber.utils.exceptions;

import org.apache.logging.log4j.message.ParameterizedMessage;

public class InvalidHttpResponseJsonFormatException extends Exception {
    private final static String MESSAGE = "Invalid HTTP Response Json format\nExpected format:\n{}\nBut Got:\n{}";
    private final static String EXPECTED_FORMAT = "{\n" +
            "  \"status\": <int> | \"<string>\",\n" +
            "  \"body\": {<jsonObject>} | [<jsonArray>] | \"<string>\",\n" +
            "  \"headers\": {<jsonObject>},\n" +
            "  \"reason\": \"<string>\"\n" +
            "}";

    public InvalidHttpResponseJsonFormatException(String source) {
        super(ParameterizedMessage.format(MESSAGE, new String[]{EXPECTED_FORMAT, source}));
    }
}
