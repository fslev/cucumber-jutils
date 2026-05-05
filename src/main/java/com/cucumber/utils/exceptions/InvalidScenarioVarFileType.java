package com.cucumber.utils.exceptions;

import java.util.Arrays;

import static com.cucumber.utils.context.vars.ScenarioVars.FileExtension.allExtensions;

public class InvalidScenarioVarFileType extends RuntimeException {
    public InvalidScenarioVarFileType() {
        super("""

                File type not supported for reading scenario variables.
                Must have one of the extensions: %s""".formatted(Arrays.toString(allExtensions())));
    }
}
