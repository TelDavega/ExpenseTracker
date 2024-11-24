package es.teldavega.expense;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.teldavega.command.AddCommand;
import es.teldavega.command.Command;
import es.teldavega.command.DeleteCommand;
import es.teldavega.command.UpdateCommand;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExpenseManager {
    private Map<Integer, Expense> expenses = new HashMap<>();
    private Map<String, Command> commands = new HashMap<>();
    private static final Gson gson = new Gson();

    public ExpenseManager() {
        this.expenses = readExpensesFile();
        registerCommands();
    }

    public Map<Integer, Expense> getExpenses() {
        return expenses;
    }

    private void registerCommands() {
        commands.put("add", new AddCommand(this));
        commands.put("update", new UpdateCommand(this));
        commands.put("delete", new DeleteCommand(this));
    }

    public void executeCommand(String commandName, String[] args) throws IOException {
        Command command = commands.get(commandName);
        if (command == null) {
            System.err.println("Unknown command: " + commandName);
            return;
        }
        command.execute(args);
    }

    public void writeExpensesFile() throws IOException {
        Path path = Paths.get("expenses.json");
        Files.deleteIfExists(path);
        try (Writer writer = new FileWriter("expenses.json")) {
            gson.toJson(expenses, writer);
        } catch (IOException e) {
            System.err.println("Error writing expenses.json: " + e.getMessage());
        }
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
}
