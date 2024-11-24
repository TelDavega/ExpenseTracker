package es.teldavega;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ExpenseTrackerCLI {


    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("No arguments provided. Please provide a command.");
            return;
        }

        ExpenseManager expenseManager = new ExpenseManager();

        String command = args[0];
        switch (command) {
            case "add":
                expenseManager.addExpense(args);
                break;
            case "update":
//                updateExpense(args);
                break;
            case "delete":
//                deleteExpense(args);
                break;
            case "list":
//                listExpenses(args);
                break;
            default:
                System.err.println("Invalid command. Please provide a valid command.");
        }
    }


}