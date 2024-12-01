package es.teldavega.command;

import es.teldavega.expense.ExpenseManager;

import java.io.IOException;

public abstract class Command {
    protected final ExpenseManager expenseManager;

    protected Command(ExpenseManager expenseManager) {
        this.expenseManager = expenseManager;
    }

    public final void execute(String[] args) throws IOException {
        if (validArguments(args)) {
            performExecute(args);
        }
    }


    public abstract void performExecute(String[] args) throws IOException;

    public abstract boolean validArguments(String[] args);


}
