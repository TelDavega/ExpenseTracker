package es.teldavega.arguments;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {
    private final Map<String, String> arguments = new HashMap<>();


    public ArgumentParser(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].startsWith("--")) {
                arguments.put(args[i], args[i + 1]);
            }
        }
    }

    public String getString(String key) {
        return arguments.get(key);
    }

    public Integer getInt(String key) {
        String value = arguments.get(key);
        return value != null ? Integer.parseInt(value) : null;
    }

    public BigDecimal getBigDecimal(String key) {
        String value = arguments.get(key);
        return value != null ? new BigDecimal(value) : null;
    }

    public boolean contains(String key) {
        return arguments.containsKey(key);
    }
}
