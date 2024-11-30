package es.teldavega.command;

import es.teldavega.expense.ExpenseManager;

import java.io.IOException;
import java.math.BigDecimal;

public class UpdateCommand extends Command {


    public UpdateCommand(ExpenseManager expenseManager) {
        super(expenseManager);
    }

    @Override
    public void execute(String[] args) throws IOException {

        if (args.length < 2) {
            System.err.println("No arguments provided. Please provide an ID.");
            return;
        }

        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--id")) {
                int id = Integer.parseInt(args[i + 1]);
                if (expenseManager.getExpenses().containsKey(id)) {
                    String description = null;
                    BigDecimal amount = BigDecimal.ZERO;
                    for (int j = i + 2; j < args.length; j++) {
                        if (args[j].equals("--description")) {
                            description = args[j + 1];
                        } else if (args[j].equals("--amount")) {
                            amount = new BigDecimal(args[j + 1]);
                        }
                    }
                    if (description == null && BigDecimal.ZERO.equals(amount)) {
                        System.err.println("Description and amount are required");
                        return;
                    }
                    if (description != null) {
                        expenseManager.getExpenses().get(id).setDescription(description);
                    }

                    if (!amount.equals(BigDecimal.ZERO)) {
                        expenseManager.getExpenses().get(id).setAmount(amount);
                    }

                    System.out.println("Expense updated successfully (ID: " + id + ")");
                    expenseManager.writeExpensesFile();
                } else {
                    System.err.println("Expense not found (ID: " + id + ")");
                }
                return;
            }
        }
    }
}
