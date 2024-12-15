package es.teldavega.command;

import es.teldavega.arguments.ArgumentParser;
import es.teldavega.arguments.DefaultArgumentParser;
import es.teldavega.expense.Expense;
import es.teldavega.expense.ExpenseManager;

import java.io.IOException;
import java.util.Map;

public abstract class Command {
    protected final ExpenseManager expenseManager;
    protected ArgumentParser parser;

    protected Command(ExpenseManager expenseManager) {
        this.expenseManager = expenseManager;
    }

    public final void execute(String[] args) throws IOException {
        setParser(args);
        if (validArguments(args)) {
            performExecute(args);
        }
    }


    public abstract void performExecute(String[] args) throws IOException;

    public abstract boolean validArguments(String[] args);

    public void setParser(String[] args) {
        this.parser = new DefaultArgumentParser(args);
    }

    protected void filterExpensesByCategory(Map<Integer, Expense> expenses) {
        String category = parser.getString("category");
        // remove all expenses that do not match the category
        if (category != null) {
            expenses.entrySet().removeIf(entry -> !entry.getValue().getCategory().toLowerCase()
                    .equalsIgnoreCase(category));
        }
    }


}
