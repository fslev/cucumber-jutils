package com.cucumber.utils.exceptions;

import java.util.Arrays;

import static com.cucumber.utils.context.vars.ScenarioVars.FileExtension.allExtensions;

public class InvalidScenarioVarFileType extends RuntimeException {
    public InvalidScenarioVarFileType() {
        super("\nFile type not supported for reading scenario variables." +
                "\nMust have one of the extensions: " + Arrays.toString(allExtensions()));
    }
}
