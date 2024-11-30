package es.teldavega.command;

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
    public void execute(String[] args) throws IOException {
        int id = expenseManager.getExpenses().size() + 1;
        String description = null;
        BigDecimal amount = BigDecimal.ZERO;
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--description")) {
                description = args[i + 1];
                if (!validDescription(description)) return;
            } else if (args[i].equals("--amount")) {
                amount = new BigDecimal(args[i + 1]);
                if (!validAmount(amount)) return;
            }
        }

        if (!validArguments(description, amount)) return;

        addExpense(id, description, amount);
    }


    private void addExpense(int id, String description, BigDecimal amount) throws IOException {
        Expense expense = new Expense(id, description, amount, new Date());
        expenseManager.getExpenses().put(id, expense);
        System.out.println("Expense added successfully (ID: " + id + ")");
        expenseManager.writeExpensesFile();
    }

    private boolean validArguments(String description, BigDecimal amount) {
        if (description == null || BigDecimal.ZERO.equals(amount)) {
            System.out.println("Description and amount are required");
            return false;
        }
        return true;
    }

    private boolean validAmount(BigDecimal amount) {
        if (amount.scale() > 2 || amount.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Invalid amount");
            return false;
        }
        return true;
    }

    private boolean validDescription(String description) {
        if ("".equals(description)) {
            System.out.println("Invalid description");
            return false;
        }
        return true;
    }
}
