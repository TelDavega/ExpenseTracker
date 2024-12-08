package es.teldavega.command;

import es.teldavega.expense.Expense;
import es.teldavega.expense.ExpenseManager;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

public class AddCommand extends Command {


    public static final String CATEGORY = "category";
    public static final String DESCRIPTION = "description";
    public static final String AMOUNT = "amount";

    public AddCommand(ExpenseManager expenseManager) {
        super(expenseManager);
    }

    @Override
    public void performExecute(String[] args) throws IOException {
        int id = expenseManager.getExpenses().size() + 1;
        String description = parser.getString(DESCRIPTION);
        BigDecimal amount = parser.getBigDecimal(AMOUNT);
        String category = parser.getString(CATEGORY);
        addExpense(id, description, amount, category);
    }

    @Override
    public boolean validArguments(String[] args) {
        String description = parser.getString(DESCRIPTION);
        String category = parser.getString(CATEGORY);
        BigDecimal amount = null;

        try {
            amount = parser.getBigDecimal(AMOUNT);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return validArguments(description, amount, category);
    }


    private void addExpense(int id, String description, BigDecimal amount, String category) throws IOException {
        Expense expense = new Expense(id, description, amount, new Date(), category);
        expenseManager.getExpenses().put(id, expense);
        System.out.println("Expense added successfully (ID: " + id + ")");
        expenseManager.writeExpensesFile();
    }


    private boolean validArguments(String description, BigDecimal amount, String category) {
        if (description == null || BigDecimal.ZERO.equals(amount) || amount == null || category == null) {
            System.out.println("Description, amount and category are required");
            return false;
        }
        return validAmount(amount) && validStringArgument(description, DESCRIPTION)
                && validStringArgument(category, CATEGORY);
    }

    private boolean validAmount(BigDecimal amount) {
        if ((amount.scale() > 2 || amount.compareTo(BigDecimal.ZERO) <= 0)) {
            System.out.println("Invalid amount");
            return false;
        }
        return true;
    }

    private boolean validStringArgument(String argument, String name) {
        if ("".equals(argument) || argument == null) {
            System.out.println("Invalid " + name);
            return false;
        }
        return true;
    }
}
