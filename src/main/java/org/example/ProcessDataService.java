package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProcessDataService {
    private final static String COMMAND_FILE = "Commands";
    private final static String REGEX_FOR_NUMBER = "[+-]?[0-9]+(\\.[0-9]+)?";
    private final static String MARK = ":";
    private Map<String, String> commands;

    public ProcessDataService() {
        commands = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(COMMAND_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(MARK);
                if (parts.length == 2) {
                    commands.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String processData(String data) {
        if (commands.containsKey(data)) {
            return commands.get(data);
        }
        if (data.matches(REGEX_FOR_NUMBER)) {
            return String.valueOf(Double.parseDouble(data) * 1000);
        } else {
            return validation(data);
        }
    }

    private String validation(String data) {
        int[] count = {0};
        return IntStream.range(0, data.length())
                .mapToObj(i -> {
                    char c = data.charAt(i);
                    if (Character.isLetter(c)) {
                        count[0]++;
                        return (count[0] % 2 == 0) ? Character.toLowerCase(c) : Character.toUpperCase(c);
                    } else {
                        return (Character.isWhitespace(c)) ? '_' : String.valueOf(c);
                    }
                })
                .map(Objects::toString)
                .collect(Collectors.joining());
    }
}