package com.cucumber.utils.engineering.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class StringFormatUtils {

    /**
     * Format strings into columns
     *
     * @param columnWidth
     * @param columns
     * @return
     */
    public static String toColumns(int columnWidth, String... columns) {
        return StringColumnFormatter.format(columnWidth, columns);
    }
}

class StringColumnFormatter {

    public static String format(int columnWidth, String... columns) {
        checkArguments(columnWidth, columns);
        if (columns == null) {
            return null;
        }
        if (columns.length == 1) {
            return columns[0];
        }
        List<String[]> columnLinesList = splitColumnLines(columnWidth, columns);
        int maxColumnLines = columnLinesList.stream().max(Comparator.comparing(array -> array != null ? array.length : 1)).get().length;
        Map<Integer, List<String>> rows = new HashMap<>();
        for (int i = 0; i < maxColumnLines; i++) {
            List<String> rowEntry = new ArrayList<>();
            for (String[] columnLines : columnLinesList) {
                rowEntry.add(columnLines != null ? columnLines.length > i ? columnLines[i] : "" : null);
            }
            rows.put(i, rowEntry);
        }
        return rowsToString(columnsFormat(columnWidth, columns.length), rows);
    }

    private static List<String[]> splitColumnLines(int columnWidth, String... columns) {
        List<String[]> columnLinesList = new ArrayList<>();
        Arrays.stream(columns).forEach(column ->
                columnLinesList.add(column != null ? column.split("\\n|\\r|(?<=\\G.{" + columnWidth + "})") : new String[]{null})
        );
        return columnLinesList;
    }

    private static void checkArguments(int columnWidth, String... columns) {
        if (columnWidth < 1) {
            throw new IllegalArgumentException("Column width must be greater than 0");
        }
    }

    private static String columnsFormat(int columnWidth, int columnsNr) {
        return StringUtils.repeat("%-" + columnWidth + "s      ", columnsNr);
    }

    private static String rowsToString(String format, Map<Integer, List<String>> rows) {
        StringBuilder result = new StringBuilder();
        rows.forEach((k, v) -> result.append(String.format(format, v.toArray())).append("\n"));
        return result.toString();
    }
}
