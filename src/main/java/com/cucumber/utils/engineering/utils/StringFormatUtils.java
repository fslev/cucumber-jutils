package com.cucumber.utils.engineering.utils;

import java.util.*;

public class StringFormatUtils {

    public static String toColumns(String... args) {
        if (args.length == 1) {
            return args[0];
        }
        List<String[]> columnLinesList = new ArrayList<>();
        String columnFormat = "%-80s".repeat(args.length);
        Arrays.stream(args).forEach(arg -> columnLinesList.add(arg.split("\\n|\\r|(?<=\\G.{40})")));
        int maxColumnLines = columnLinesList.stream().max(Comparator.comparing(array -> array.length)).get().length;
        Map<Integer, List<String>> rows = new HashMap<>();
        for (int i = 0; i < maxColumnLines; i++) {
            List<String> rowEntry = new ArrayList<>();
            for (String[] columnLines : columnLinesList) {
                rowEntry.add(columnLines.length > i ? columnLines[i] : "");
            }
            rows.put(i, rowEntry);
        }
        return rowsToString(columnFormat, rows);
    }

    private static String rowsToString(String format, Map<Integer, List<String>> rows) {
        StringBuilder result = new StringBuilder();
        rows.forEach((k, v) -> result.append(String.format(format, v.toArray())).append("\n"));
        return result.toString();
    }


    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(toColumns("[\n" + "  {\n" + "    \"_id\": \"5b4fa3f8c2741fde34e4d5c8\",\n"
                    + "    \"index\": 0,\n" + "    \"latitude\": -73.952152,\n"
                    + "    \"longitude\": \"~[longitude]\",\n" + "    \"tags\": [\n"
                    + "      \"irure\",\n" + "      \"et\",\n" + "      \"ex\",\n"
                    + "      \"fugiat\",\n" + "      \"aute\",\n" + "      \"laboris\",\n"
                    + "      \"sit\"\n" + "    ],\n" + "    \"friends\": [\n" + "      {\n"
                    + "        \"id\": \"~[friendId]\",\n" + "        \"name\": \"Meagan Martinez\"\n"
                    + "      },\n" + "      {\n" + "        \"id\": 2,\n"
                    + "        \"name\": \"Sloan Yang\"\n" + "      }\n" + "    ],\n"
                    + "    \"greeting\": \"Hello, Holly Hawkins! You have 1 unread messages.\",\n"
                    + "    \"favoriteFruit\": \"banana\"\n" + "  }\n" + "]", "[\n" + "  {\n" + "    \"_id\": \"5b4fa3f8c2741fde34e4d5c8\",\n"
                    + "    \"index\": 0,\n"
                    + "    \"guid\": \"6bf5b919-ec09-444b-b9ec-fff820b9c591\",\n"
                    + "    \"isActive\": false,\n" + "    \"balance\": \"$3,756.68\",\n"
                    + "    \"picture\": \"http://placehold.it/32x32\",\n" + "    \"age\": 27,\n"
                    + "    \"eyeColor\": \"brown\",\n" + "    \"name\": \"Holly Hawkins\",\n"
                    + "    \"gender\": \"female\",\n" + "    \"company\": \"FUELTON\",\n"
                    + "    \"email\": \"hollyhawkins@fuelton.com\",\n"
                    + "    \"phone\": \"+1 (997) 554-3416\",\n"
                    + "    \"address\": \"825 Powers Street, Noxen, Iowa, 7981\",\n"
                    + "    \"about\": \"Ullamco sunt ex reprehenderit velit tempor nulla exercitation laborum consectetur ullamco veniam. Veniam est aliqua deserunt excepteur. Veniam fugiat laboris esse dolor deserunt. Reprehenderit sit velit anim laborum fugiat veniam occaecat exercitation occaecat commodo in quis sunt. Tempor mollit excepteur nulla voluptate aliqua sunt velit pariatur deserunt.\\r\\n\",\n"
                    + "    \"registered\": \"2015-05-28T07:50:30 -03:00\",\n"
                    + "    \"latitude\": -73.952152,\n" + "    \"longitude\": -90.447286,\n"
                    + "    \"tags\": [\n" + "      \"irure\",\n" + "      \"et\",\n" + "      \"ex\",\n"
                    + "      \"fugiat\",\n" + "      \"aute\",\n" + "      \"laboris\",\n"
                    + "      \"sit\"\n" + "    ],\n" + "    \"friends\": [\n" + "      {\n"
                    + "        \"id\": 0,\n" + "        \"name\": \"Jodie Gaines\"\n" + "      },\n"
                    + "      {\n" + "        \"id\": 1,\n" + "        \"name\": \"Meagan Martinez\"\n"
                    + "      },\n" + "      {\n" + "        \"id\": 2,\n"
                    + "        \"name\": \"Sloan Yang\"\n" + "      }\n" + "    ],\n"
                    + "    \"greeting\": \"Hello, Holly Hawkins! You have 1 unread messages.\",\n"
                    + "    \"favoriteFruit\": \"banana\"\n" + "  }\n" + "]"));
        }
    }
}
