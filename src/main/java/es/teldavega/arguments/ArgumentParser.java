package es.teldavega.arguments;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {
    protected final Map<String, String> arguments = new HashMap<>();
    protected final String key;

    public ArgumentParser(String[] args, String key) {
        this.key = key;
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].startsWith(key)) {
                arguments.put(args[i], args[i + 1]);
            }
        }
    }

    public boolean contains(String string) {
        return arguments.containsKey(key + string);
    }

    public String getString(String string) {
        return arguments.get(key + string);
    }

    public Integer getInt(String integer) {
        String value = arguments.get(key + integer);
        return value != null ? Integer.parseInt(value) : null;
    }

    public BigDecimal getBigDecimal(String bigDecimal) {
        String value = arguments.get(key + bigDecimal);
        if (value == null) {
            return null;
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount");
        }
    }

    public String getStringWithoutValue(String[] args, String string) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(key + string)) {
                return args[i];
            }
        }
        return null;
    }
}
