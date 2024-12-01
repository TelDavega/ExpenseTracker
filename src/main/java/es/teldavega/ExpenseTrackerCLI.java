package es.teldavega;


import es.teldavega.expense.ExpenseManager;

import java.io.IOException;

public class ExpenseTrackerCLI {


    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.err.println("No arguments provided. Please provide a command.");
            return;
        }

        ExpenseManager expenseManager = new ExpenseManager();

        String command = args[0];
        expenseManager.executeCommand(command, args);
    }


}