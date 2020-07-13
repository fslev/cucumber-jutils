package com.cucumber.utils.exceptions;

import org.apache.logging.log4j.message.ParameterizedMessage;

public class InvalidHttpResponseFormatException extends InvalidTypeException {
    private final static String MESSAGE = "Invalid HTTP Response Type\nExpected JSON format:\n{}\nBut Got:\n{}";
    private final static String EXPECTED_FORMAT = "{\n" +
            "  \"status\": <int> | \"<string>\",\n" +
            "  \"body\": {<jsonObject>} | [<jsonArray>] | \"<string>\",\n" +
            "  \"headers\": {<jsonObject>},\n" +
            "  \"reason\": \"<string>\"\n" +
            "}";

    public InvalidHttpResponseFormatException(String source) {
        super(ParameterizedMessage.format(MESSAGE, new String[]{EXPECTED_FORMAT, source}));
    }
}
