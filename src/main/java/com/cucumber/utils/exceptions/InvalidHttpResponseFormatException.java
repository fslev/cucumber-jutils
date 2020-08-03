package com.cucumber.utils.exceptions;

import org.apache.logging.log4j.message.ParameterizedMessage;

public class InvalidHttpResponseFormatException extends InvalidTypeException {
    private final static String MESSAGE = "Invalid HTTP Response Type\nExpected JSON format:\n{}\nBut Got:\n{}";
    private final static String EXPECTED_FORMAT = "{\n" +
            "  \"status\": <number> | \"<text>\",\n" +
            "  \"body\": {<jsonObject>} | [<jsonArray>] | \"<text>\",\n" +
            "  \"headers\": {<jsonObject>},\n" +
            "  \"reason\": \"<text>\"\n" +
            "}";

    public InvalidHttpResponseFormatException(String source) {
        super(ParameterizedMessage.format(MESSAGE, new String[]{EXPECTED_FORMAT, source}));
    }
}
