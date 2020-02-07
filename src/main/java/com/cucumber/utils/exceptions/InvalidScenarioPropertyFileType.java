package com.cucumber.utils.exceptions;

import java.util.Arrays;

import static com.cucumber.utils.context.props.ScenarioProps.FileExtension.allExtensions;

public class InvalidScenarioPropertyFileType extends RuntimeException {
    public InvalidScenarioPropertyFileType() {
        super("\nFile type not supported for reading scenario properties." +
                "\nMust have one of the extensions: " + Arrays.toString(allExtensions()));
    }
}
