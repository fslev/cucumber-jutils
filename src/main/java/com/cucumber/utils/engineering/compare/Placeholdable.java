package com.cucumber.utils.engineering.compare;

import java.util.Map;

public interface Placeholdable {

    /**
     * @return A Map of placeholders names with their corresponding values
     */
    Map<String, Object> compare();
}
