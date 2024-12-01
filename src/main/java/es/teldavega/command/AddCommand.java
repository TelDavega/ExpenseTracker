package es.teldavega.command;

import es.teldavega.arguments.DefaultArgumentParser;
import es.teldavega.expense.Expense;
import es.teldavega.expense.ExpenseManager;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

public class AddCommand extends Command {


    public AddCommand(ExpenseManager expenseManager) {
        super(expenseManager);
    }

    @Override
    public void performExecute(String[] args) throws IOException {
        int id = expenseManager.getExpenses().size() + 1;
        String description = parser.getString("description");
        BigDecimal amount = parser.getBigDecimal("amount");
        addExpense(id, description, amount);
    }

    @Override
    public boolean validArguments(String[] args) {
        String description = parser.getString("description");
        BigDecimal amount = parser.getBigDecimal("amount");

        return validArguments(description, amount);
    }


    private void addExpense(int id, String description, BigDecimal amount) throws IOException {
        Expense expense = new Expense(id, description, amount, new Date());
        expenseManager.getExpenses().put(id, expense);
        System.out.println("Expense added successfully (ID: " + id + ")");
        expenseManager.writeExpensesFile();
    }


    private boolean validArguments(String description, BigDecimal amount) {
        if (description == null || BigDecimal.ZERO.equals(amount) || amount == null) {
            System.out.println("Description and amount are required");
            return false;
        }
        return validAmount(amount) && validDescription(description);
    }

    private boolean validAmount(BigDecimal amount) {
        if ((amount.scale() > 2 || amount.compareTo(BigDecimal.ZERO) <= 0)) {
            System.out.println("Invalid amount");
            return false;
        }
        return true;
    }

    private boolean validDescription(String description) {
        if ("".equals(description) || description == null) {
            System.out.println("Invalid description");
            return false;
        }
        return true;
    }
}
