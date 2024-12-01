package es.teldavega.arguments;

import java.math.BigDecimal;
import java.util.HashMap;

public class DefaultArgumentParser extends ArgumentParser {


    public DefaultArgumentParser(String[] args) {
        super(args, "--");
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

}
