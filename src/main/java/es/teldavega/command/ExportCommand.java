package es.teldavega.command;

import es.teldavega.expense.Expense;
import es.teldavega.expense.ExpenseManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportCommand extends Command{
    public ExportCommand(ExpenseManager expenseManager) {
        super(expenseManager);
    }

    @Override
    public void performExecute(String[] args) {
        List<Expense> expenses = new ArrayList<>(expenseManager.getExpenses().values());
        String path = parser.getString("path");
        try {
            convertToCSV(expenses, path);
            System.out.println("Expenses exported successfully to " + path);
        } catch (IOException e) {
            System.out.println("Error exporting expenses to CSV file: " + e.getMessage());
        }

    }

    @Override
    public boolean validArguments(String[] args) {
        String path = parser.getString("path");
        if (path == null) {
            System.out.println("Missing path argument");
            return false;
        }
        return true;
    }

    private void convertToCSV(List<Expense> expenses, String path) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(("id,description,amount,date,category"));
            writer.newLine();

            for (Expense expense : expenses) {
                writer.write(expense.getId() + ","
                        + escapeSpecialCharacters(expense.getDescription()) + ","
                        + expense.getAmount() + ","
                        + expense.getDate() + ","
                        + escapeSpecialCharacters(expense.getCategory()));
                writer.newLine();
            }
        }
    }

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
