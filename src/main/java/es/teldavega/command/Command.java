package es.teldavega.command;

import es.teldavega.expense.ExpenseManager;

import java.io.IOException;

public abstract class Command {
    protected final ExpenseManager expenseManager;

    protected Command(ExpenseManager expenseManager) {
        this.expenseManager = expenseManager;
    }


    public abstract void execute(String[] args) throws IOException;
}
