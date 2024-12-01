package es.teldavega.command;

import es.teldavega.arguments.DefaultArgumentParser;
import es.teldavega.expense.ExpenseManager;

import java.io.IOException;
import java.math.BigDecimal;

public class UpdateCommand extends Command {


    public UpdateCommand(ExpenseManager expenseManager) {
        super(expenseManager);
    }

    @Override
    public void performExecute(String[] args) throws IOException {

        int id = parser.getInt("id");
        String description = parser.getString("description");
        BigDecimal amount = parser.getBigDecimal("amount");

        if (description != null) {
            expenseManager.getExpenses().get(id).setDescription(description);
        }

        if (amount != null) {
            expenseManager.getExpenses().get(id).setAmount(amount);
        }

        System.out.println("Expense updated successfully (ID: " + id + ")");
        expenseManager.writeExpensesFile();

    }

    @Override
    public boolean validArguments(String[] args) {

        if (args.length < 2) {
            System.out.println("No arguments provided. Please provide an ID.");
            return false;
        }

        int id = parser.getInt("id");
        if (!expenseManager.getExpenses().containsKey(id)) {
            System.out.println("Expense not found (ID: " + id + ")");
            return false;
        }
        String description = parser.getString("description");
        BigDecimal amount = parser.getBigDecimal("amount");
        if (description == null && amount == null) {
            System.out.println("Description or amount are required");
            return false;
        }
        return true;
    }

}
