package es.teldavega.command;

import es.teldavega.arguments.DefaultArgumentParser;
import es.teldavega.expense.ExpenseManager;

import java.io.IOException;

public class DeleteCommand extends Command {
    public DeleteCommand(ExpenseManager expenseManager) {
        super(expenseManager);
    }

    @Override
    public void performExecute(String[] args) throws IOException {
        Integer id = parser.getInt("id");
        expenseManager.getExpenses().remove(id);
        System.out.println("Expense deleted successfully");
        expenseManager.writeExpensesFile();
    }

    @Override
    public boolean validArguments(String[] args) {
        Integer id = parser.getInt("id");
        if (id == null) {
            System.out.println("No arguments provided. Please provide an ID.");
            return false;
        }

        if (!expenseManager.getExpenses().containsKey(id)) {
            System.out.println("Expense not found (ID: " + id + ")");
            return false;
        }
        return true;
    }

}
