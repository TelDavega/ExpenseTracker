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

    private static Map<Integer, Expense> expenses = new HashMap<>();

    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("No arguments provided. Please provide a command.");
            return;
        }

        String command = args[0];
        switch (command) {
            case "add":
                addExpense(args);
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

    private static void addExpense(String[] args) {
        int id = expenses.size() + 1;
        String description = null;
        double amount = 0;
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--description")) {
                description = args[i + 1];
            } else if (args[i].equals("--amount")) {
                amount = Double.parseDouble(args[i + 1]);
            }
        }

        if (description == null || amount == 0) {
            System.err.println("Description and amount are required");
            return;
        }

        Expense expense = new Expense(id, description, amount, new Date());
        expenses.put(id, expense);
        System.out.println("Expense added successfully (ID: " + id + ")");
    }
}