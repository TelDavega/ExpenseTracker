package es.teldavega.command;

import es.teldavega.expense.Expense;
import es.teldavega.expense.ExpenseManager;

import java.io.IOException;
import java.util.Date;

public class AddCommand extends Command {


    public AddCommand(ExpenseManager expenseManager) {
        super(expenseManager);
    }

    @Override
    public void execute(String[] args) throws IOException {
        int id = expenseManager.getExpenses().size() + 1;
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
        expenseManager.getExpenses().put(id, expense);
        System.out.println("Expense added successfully (ID: " + id + ")");
        expenseManager.writeExpensesFile();
    }
}
