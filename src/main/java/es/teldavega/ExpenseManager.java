package es.teldavega;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExpenseManager {
    private Map<Integer, Expense> expenses = new HashMap<>();

    private static final Gson gson = new Gson();

    public ExpenseManager() {
        this.expenses = readExpensesFile();
    }

    private Map<Integer, Expense> readExpensesFile() {

        File file = new File("expenses.json");

        if (!file.exists() || file.length() == 0) {
            return new HashMap<>();
        }

        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, new TypeToken<Map<Integer, Expense>>() {
            }.getType());
        } catch (IOException e) {
            System.err.println("Error reading expenses.json: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    public void addExpense(String[] args) {
        int id = expenses.size() + 1;
        String description = null;
        double amount = 0;
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--description")) {
                description = args[i + 1];
            } else if (args[i].equals("--amount")) {
                amount = Double.parseDouble(args[i + 1]);
            }
        }

        if (description == null || amount == 0) {
            System.err.println("Description and amount are required");
            return;
        }

        Expense expense = new Expense(id, description, amount, new Date());
        expenses.put(id, expense);
        System.out.println("Expense added successfully (ID: " + id + ")");
        writeExpensesFile();
    }

    private void writeExpensesFile() {
        try (Writer writer = new FileWriter("expenses.json")) {
            gson.toJson(expenses, writer);
        } catch (IOException e) {
            System.err.println("Error writing expenses.json: " + e.getMessage());
        }
    }
}
