package org.example;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.Properties;

public class ExpenseTrackerCLI {

    private static final Logger log = LogManager.getLogger(ExpenseTrackerCLI.class);

    public static void main(String[] args) {
        Properties properties = loadProperties();
        String logLevel = properties.getProperty("log.level", "info").toUpperCase();
        setLogLevel(logLevel);
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try {
            properties.load(ExpenseTrackerCLI.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (Exception e) {
            log.error("Error loading properties file", e);
        }
        return properties;
    }

    private static void setLogLevel(String logLevel) {
        try {
            Level level = Level.valueOf(logLevel);
            Configurator.setRootLevel(level);
            if (log.isDebugEnabled()) {
                log.debug("Log level set to {}", logLevel);
            }
        } catch (IllegalArgumentException e) {
            log.error("Invalid log level {}. Using default level INFO", logLevel);
        }
    }
}