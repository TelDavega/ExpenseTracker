package es.teldavega.command;

import es.teldavega.expense.Expense;
import es.teldavega.expense.ExpenseManager;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Locale;

public class SummaryCommand extends Command {

    public SummaryCommand(ExpenseManager expenseManager) {
        super(expenseManager);
    }

    @Override
    public void execute(String[] args) throws IOException {

        if (expenseManager.getExpenses().isEmpty()) {
            System.out.println("No expenses found");
            return;
        }

        if (args.length > 1) {
            if (args[1].equals("--month")) {
                int month = Integer.parseInt(args[2]);
                BigDecimal total = BigDecimal.ZERO;
                Calendar calendar = Calendar.getInstance();
                for (Expense expense : expenseManager.getExpenses().values()) {
                    calendar.setTime(expense.getDate());
                    if ((calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR))
                            && (calendar.get(Calendar.MONTH) + 1 == month)) {
                        total = total.add(expense.getAmount());
                    }
                }
                System.out.printf("Total expenses for month %s: $%.2f%n", calendar.getDisplayName(Calendar.MONTH,
                        Calendar.LONG, Locale.ENGLISH), total);
                return;
            } else {
                System.err.println("Unknown option: " + args[1]);
                return;
            }
        }

        BigDecimal total = BigDecimal.ZERO;
        for (Expense expense : expenseManager.getExpenses().values()) {
            total = total.add(expense.getAmount());
        }
        System.out.printf("Total expenses: $%.2f%n", total);
    }
}
