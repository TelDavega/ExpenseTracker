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
        String category = parser.getString("category");

        if (description != null) {
            expenseManager.getExpenses().get(id).setDescription(description);
        }

        if (amount != null) {
            expenseManager.getExpenses().get(id).setAmount(amount);
        }

        if (category != null) {
            expenseManager.getExpenses().get(id).setCategory(category);
        }

        System.out.println("Expense updated successfully (ID: " + id + ")");
        expenseManager.writeExpensesFile();

    }

    @Override
    public boolean validArguments(String[] args) {

        if (!validArgumentsLength(args)) return false;

        int id = parser.getInt("id");
        if (!validId(id)) return false;
        String description = parser.getString("description");
        String category = parser.getString("category");
        BigDecimal amount = null;
        try {
            amount = parser.getBigDecimal("amount");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
        if (!validAmount(amount)) return false;

        if (description == null && amount == null && category == null) {
            System.out.println("Description, amount, or category is required");
            return false;
        }

        return true;
    }

    private boolean validAmount(BigDecimal amount) {
        if (amount != null) {
            int scale = amount.scale();
            if ((scale > 2 || amount.compareTo(BigDecimal.ZERO) <= 0)) {
                System.out.println("Invalid amount");
                return false;
            }
        }
        return true;
    }

    private boolean validId(int id) {
        if (!expenseManager.getExpenses().containsKey(id)) {
            System.out.println("Expense not found (ID: " + id + ")");
            return false;
        }
        return true;
    }

    private boolean validArgumentsLength(String[] args) {
        if (args.length < 2) {
            System.out.println("No arguments provided. Please provide an ID.");
            return false;
        }

        if (args.length > 8) {
            System.out.println("Too many arguments provided. Please provide an ID, description, category, and/or amount.");
            return false;
        }
        return true;
    }

}
