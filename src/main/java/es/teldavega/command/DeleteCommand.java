package es.teldavega.command;

import es.teldavega.expense.ExpenseManager;

import java.io.IOException;

public class DeleteCommand extends Command {
    public DeleteCommand(ExpenseManager expenseManager) {
        super(expenseManager);
    }

    @Override
    public void execute(String[] args) throws IOException {
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--id")) {
                int id = Integer.parseInt(args[i + 1]);
                if (expenseManager.getExpenses().containsKey(id)) {
                    expenseManager.getExpenses().remove(id);
                    System.out.println("Expense deleted successfully");
                    expenseManager.writeExpensesFile();
                } else {
                    System.err.println("Expense not found (ID: " + id + ")");
                }
                return;
            }
        }
    }
}
