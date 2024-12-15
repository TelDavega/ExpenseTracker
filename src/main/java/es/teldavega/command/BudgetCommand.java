package es.teldavega.command;

import es.teldavega.expense.ExpenseManager;

import java.math.BigDecimal;

public class BudgetCommand extends Command {

    public BudgetCommand(ExpenseManager expenseManager) {
        super(expenseManager);
    }

    @Override
    public void performExecute(String[] args) {
        BigDecimal setBudget = parser.getBigDecimal("set");
        BigDecimal updateBudget = parser.getBigDecimal("update");
        String delete = parser.getStringWithoutValue(args,"delete");
        if (delete != null) {
            expenseManager.setBudgetLimit(null);
            System.out.println("Budget deleted successfully");
            return;
        }
        if (validArguments(args)) {
            updateBudget(setBudget, updateBudget);
        }
    }

    private void updateBudget(BigDecimal setBudget, BigDecimal updateBudget) {
        BigDecimal budget = setBudget != null ? setBudget : updateBudget;
        expenseManager.setBudgetLimit(budget);
        System.out.println("Budget updated successfully");
    }

    @Override
    public boolean validArguments(String[] args) {
        BigDecimal setBudget = null;
        BigDecimal updateBudget = null;
        try {
            setBudget = parser.getBigDecimal("set");
            updateBudget = parser.getBigDecimal("update");
        } catch (Exception e) {
            System.out.println("Invalid budget");
            return false;
        }
        String delete = parser.getStringWithoutValue(args,"delete");
        if (setBudget == null && updateBudget == null && delete == null) {
            System.out.println("No arguments provided. Please provide a budget.");
            return false;
        }
        return true;
    }
}
