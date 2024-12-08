package es.teldavega.command;

import es.teldavega.expense.ExpenseManager;

import java.text.SimpleDateFormat;

public class ListCommand extends Command {
    public ListCommand(ExpenseManager expenseManager) {
        super(expenseManager);
    }

    @Override
    public void performExecute(String[] args) {
        if (expenseManager.getExpenses().isEmpty()) {
            System.out.println("No expenses found");
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm z yyyy");

        // Define format for the table
        String headerFormat = "%-5s %-27s %-20s %-20s %-10s%n";
        String rowFormat = "%-5d %-27s %-20s %-20s $%-9.2f%n";
        System.out.printf(headerFormat, "ID", "        Date        ", "Description  ", "Category  ", "Amount ");
        expenseManager.getExpenses().values().forEach(expense ->
                System.out.printf(rowFormat, expense.getId(),
                dateFormat.format(expense.getDate()),
                expense.getDescription(),
                expense.getCategory(),
                expense.getAmount()));
    }

    @Override
    public boolean validArguments(String[] args) {
        // No arguments are required for this command
        return true;
    }

    @Override
    public void setParser(String[] args) {
        // No parser is required for this command
    }
}
