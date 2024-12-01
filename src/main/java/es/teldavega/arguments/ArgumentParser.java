package es.teldavega.arguments;

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
}
