package es.teldavega.expense;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import es.teldavega.command.*;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExpenseManager {
    public static final String BUDGET_LIMIT = "budgetLimit";
    public static final String CURRENT_MONTH_BUDGET = "currentMonthBudget";
    public static final String LAST_UPDATE = "lastUpdate";
    public static final String EXPENSES_JSON = "expenses.json";
    private Map<Integer, Expense> expenses = new HashMap<>();
    private final Map<String, Command> commands = new HashMap<>();
    private BigDecimal budgetLimit = BigDecimal.ZERO;
    private BigDecimal currentMonthBudget = BigDecimal.ZERO;
    private static final Gson gson = new Gson();

    public ExpenseManager() {
        this.expenses = readExpensesFile();
        Map<String, Object> budgetData = readBudgetFile();
        this.budgetLimit = convertToBigDecimal(budgetData.getOrDefault(BUDGET_LIMIT, BigDecimal.ZERO));
        this.currentMonthBudget = convertToBigDecimal(budgetData.getOrDefault(CURRENT_MONTH_BUDGET, BigDecimal.ZERO));
        registerCommands();
    }

    public Map<Integer, Expense> getExpenses() {
        return expenses;
    }

    private void registerCommands() {
        commands.put("add", new AddCommand(this));
        commands.put("update", new UpdateCommand(this));
        commands.put("delete", new DeleteCommand(this));
        commands.put("list", new ListCommand(this));
        commands.put("summary", new SummaryCommand(this));
        commands.put("budget", new BudgetCommand(this));
        commands.put("export", new ExportCommand(this));
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
        Path path = Paths.get(EXPENSES_JSON);
        Files.deleteIfExists(path);
        try (Writer writer = new FileWriter(EXPENSES_JSON)) {
            gson.toJson(expenses, writer);
        } catch (IOException e) {
            System.err.println("Error writing expenses.json: " + e.getMessage());
        }
    }

    private Map<Integer, Expense> readExpensesFile() {

        File file = new File(EXPENSES_JSON);

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

    public BigDecimal getBudgetLimit() {
        return budgetLimit;
    }

    public void setBudgetLimit(BigDecimal budgetLimit) {
        this.budgetLimit = budgetLimit;
        this.currentMonthBudget = budgetLimit;
        try {
            writeBudgetFile();
        } catch (IOException e) {
            System.out.println("Error saving budget: " + e.getMessage());
        }
    }

    public BigDecimal getCurrentBudget() {
        return currentMonthBudget;
    }

    public void setCurrentBudget(BigDecimal currentMonthBudget) {
        this.currentMonthBudget = currentMonthBudget;
        try {
            writeBudgetFile();
        } catch (IOException e) {
            System.out.println("Error saving budget: " + e.getMessage());
        }
    }

    private Map<String, Object> readBudgetFile() {
        File file = new File("budget.json");
        if (!file.exists() || file.length() == 0) {
            Map<String, Object> defaultBudget = new HashMap<>();
            defaultBudget.put(BUDGET_LIMIT, BigDecimal.ZERO);
            defaultBudget.put(CURRENT_MONTH_BUDGET, BigDecimal.ZERO);
            defaultBudget.put(LAST_UPDATE, LocalDate.now().toString());
            return defaultBudget;
        }

        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, new TypeToken<Map<String, Object>>() {}.getType());
        } catch (IOException e) {
            System.out.println("Error reading budget.json: " + e.getMessage());
            Map<String, Object> defaultBudget = new HashMap<>();
            defaultBudget.put(BUDGET_LIMIT, BigDecimal.ZERO);
            defaultBudget.put(CURRENT_MONTH_BUDGET, BigDecimal.ZERO);
            defaultBudget.put(LAST_UPDATE, LocalDate.now().toString());
            return defaultBudget;
        }
    }


    private void writeBudgetFile() throws IOException {
        Map<String, Object> budgetData = new HashMap<>();
        budgetData.put(BUDGET_LIMIT, budgetLimit);
        budgetData.put(CURRENT_MONTH_BUDGET, currentMonthBudget);
        budgetData.put(LAST_UPDATE, LocalDate.now().toString());

        try (Writer writer = new FileWriter("budget.json")) {
            gson.toJson(budgetData, writer);
        } catch (IOException e) {
            System.out.println("Error writing budget.json: " + e.getMessage());
        }
    }


    public void checkAndResetMonthlyBudget() {
        Map<String, Object> budgetData = readBudgetFile();
        String lastUpdateString = (String) budgetData.get(LAST_UPDATE);
        LocalDate lastUpdate = LocalDate.parse(lastUpdateString);

        LocalDate now = LocalDate.now();
        if (now.getMonthValue() != lastUpdate.getMonthValue() || now.getYear() != lastUpdate.getYear()) {
            this.currentMonthBudget = this.budgetLimit;
            try {
                writeBudgetFile();
            } catch (IOException e) {
                System.out.println("Error resetting monthly budget: " + e.getMessage());
            }
        }
    }

    private BigDecimal convertToBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Double) {
            return BigDecimal.valueOf((Double) value);
        } else if (value instanceof String) {
            return new BigDecimal((String) value);
        } else {
            return BigDecimal.ZERO;
        }
    }

}
