package es.teldavega.command;

import es.teldavega.expense.Expense;
import es.teldavega.expense.ExpenseManager;

import java.io.IOException;

public class SummaryCommand extends Command {

    public SummaryCommand(ExpenseManager expenseManager) {
        super(expenseManager);
    }

    @Override
    public void execute(String[] args) throws IOException {
        double total = 0;
        for (Expense expense : expenseManager.getExpenses().values()) {
            total += expense.getAmount();
        }
        System.out.printf("Total expenses: $%.2f%n", total);
    }
}
