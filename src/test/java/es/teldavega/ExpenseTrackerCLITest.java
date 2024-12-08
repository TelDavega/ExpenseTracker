package es.teldavega;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTrackerCLITest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() throws IOException {
        Path path = Paths.get("expenses.json");
        Files.deleteIfExists(path);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void testAddExpense() throws IOException {
        String[] args = {"add", "--description", "food", "--amount", "10", "--category", "Food"};
        ExpenseTrackerCLI.main(args);
        String expected = "Expense added successfully (ID: 1)" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
        outContent.reset();
        args = new String[]{"add", "--description", "dinner", "--amount", "20.53", "--category", "Food"};
        ExpenseTrackerCLI.main(args);
        expected = "Expense added successfully (ID: 2)" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @ParameterizedTest
    @CsvSource({
            "'add --description  --amount 10 --category Food', 'Invalid description'",
            "'add --description dinner --amount -10 --category Food', 'Invalid amount'",
            "'add --description dinner --amount abc --category Food', 'Invalid amount'",
            "'add --description dinner --amount 10.123 --category Food', 'Invalid amount'",
            "'add', 'Description, amount and category are required'"
    })
    void testAddExpenseWithInvalidInputs(String inputArgs, String expectedOutput) throws IOException {
        String[] args = inputArgs.split(" ");
        ExpenseTrackerCLI.main(args);

        String expected = expectedOutput + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testUpdateExpense() throws IOException {
        addDefaultExpense();
        String[] args;

        args = new String[]{"update", "--id", "1", "--description", "dinner", "--amount", "20.53"};
        ExpenseTrackerCLI.main(args);
        String expected = "Expense updated successfully (ID: 1)" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
        outContent.reset();

        args = new String[]{"update","--id", "1", "--amount", "10.53"};
        ExpenseTrackerCLI.main(args);
        assertEquals(expected, outContent.toString());
        outContent.reset();

        args = new String[]{"update","--id", "1", "--description", "lunch"};
        ExpenseTrackerCLI.main(args);
        assertEquals(expected, outContent.toString());
    }

    private void addDefaultExpense() throws IOException {
        String[] args = {"add", "--description", "food", "--amount", "10", "--category", "Food"};
        ExpenseTrackerCLI.main(args);
        outContent.reset();
    }

    @ParameterizedTest
    @CsvSource({
            "'update --id 1 --description dinner --amount -10', 'Invalid amount'",
            "'update --id 1 --description dinner --amount abc', 'Invalid amount'",
            "'update --id 1 --description dinner --amount 10.123', 'Invalid amount'",
            "'update --id 1 --description dinner --amount 10.53 --id 2', 'Too many arguments provided. Please provide an ID, description, and/or amount.'",
            "'update --id 4 --description dinner --amount 10.53', 'Expense not found (ID: 4)'",
            "'update', 'No arguments provided. Please provide an ID.'",
            "'update --id 1', 'Description or amount are required'",
            "'delete', 'No arguments provided. Please provide an ID.'",
            "'delete --id 4', 'Expense not found (ID: 4)'",
    })
    void testUpdateAndDeleteExpenseWithInvalidInputs(String inputArgs, String expectedOutput) throws IOException {
        addDefaultExpense();
        String[] args;
        args = inputArgs.split(" ");
        ExpenseTrackerCLI.main(args);

        String expected = expectedOutput + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testDeleteExpense() throws IOException {
        addDefaultExpense();
        String [] args = new String[]{"delete", "--id", "1"};
        ExpenseTrackerCLI.main(args);
        String expected = "Expense deleted successfully" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }


    @Test
    void testListExpenses() throws IOException {
        String[] args = {"add", "--description", "food", "--amount", "10", "--category", "Food"};
        ExpenseTrackerCLI.main(args);
        Date date1 = new Date();
        args = new String[]{"add", "--description", "dinner", "--amount", "20.53", "--category", "FOOD"};
        ExpenseTrackerCLI.main(args);
        Date date2 = new Date();
        outContent.reset();

        args = new String[]{"list"};
        ExpenseTrackerCLI.main(args);
        String headerFormat = "%-5s %-27s %-20s %-20s %-10s%n";
        String rowFormat = "%-5d %-27s %-20s %-20s $%-9.2f%n";
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm z yyyy");

        String expectedHeader = String.format(headerFormat, "ID", "        Date        ", "Description  ","Category  ",
                "Amount ");
        String expectedRow1 = String.format(rowFormat, 1, dateFormat.format(date1), "food", "Food",  10.0);
        String expectedRow2 = String.format(rowFormat, 2, dateFormat.format(date2), "dinner", "Food", 20.53);
        String expected = expectedHeader + expectedRow1 + expectedRow2;
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testListExpensesEmpty() throws IOException {
        String[] args = {"list"};
        ExpenseTrackerCLI.main(args);
        String expected = "No expenses found" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testSummary() throws IOException {
        String[] args = {"add", "--description", "food", "--amount", "10", "--category", "Food"};
        ExpenseTrackerCLI.main(args);
        args = new String[]{"add", "--description", "dinner", "--amount", "20.5", "--category", "Food"};
        ExpenseTrackerCLI.main(args);
        outContent.reset();

        args = new String[]{"summary"};
        ExpenseTrackerCLI.main(args);
        String expected = "Total expenses: $30,50" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testSummaryByMonth() throws IOException {
        Calendar calendar = Calendar.getInstance();
        int currentMonthNumber = calendar.get(Calendar.MONTH) + 1;
        String currentMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);

        String[] args = {"add", "--description", "food", "--amount", "10", "--category", "Food"};
        ExpenseTrackerCLI.main(args);
        args = new String[]{"add", "--description", "dinner", "--amount", "20.53", "--category", "Food"};
        ExpenseTrackerCLI.main(args);
        outContent.reset();

        args = new String[]{"summary", "--month", String.valueOf(currentMonthNumber)};
        ExpenseTrackerCLI.main(args);
        String expected = "Total expenses for month " + currentMonth + ": $30,53" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testSummaryByMonthEmpty() throws IOException {
        String[] args = {"summary", "--month", "1"};
        ExpenseTrackerCLI.main(args);
        String expected = "No expenses found" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @ParameterizedTest
    @CsvSource({
            "'summary --month 13', 'Invalid month'",
            "'summary --month abc', 'Invalid month'",
            "'summary --month 0', 'Invalid month'",
    })
    void testSummaryByMonthInvalid() throws IOException {
        String[] args = {"add", "--description", "food", "--amount", "10", "--category", "Food"};
        ExpenseTrackerCLI.main(args);
        args = new String[]{"add", "--description", "dinner", "--amount", "20.53", "--category", "Food"};
        ExpenseTrackerCLI.main(args);
        outContent.reset();

        args = new String[]{"summary", "--month", "13"};
        ExpenseTrackerCLI.main(args);
        String expected = "Invalid month" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testSummaryByMonthNoExpensesForMonth() throws IOException {
        Calendar calendar = Calendar.getInstance();
        int currentMonthNumber = calendar.get(Calendar.MONTH) + 1;
        calendar.add(Calendar.MONTH, 1);
        String nextMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        String[] args = {"add", "--description", "food", "--amount", "10", "--category", "Food"};
        ExpenseTrackerCLI.main(args);
        args = new String[]{"add", "--description", "dinner", "--amount", "20.53", "--category", "Food"};
        ExpenseTrackerCLI.main(args);
        outContent.reset();
        int nextMonthNumber;
        if (currentMonthNumber == 12) {
            nextMonthNumber = 1;
        } else {
            nextMonthNumber = currentMonthNumber + 1;
        }
        args = new String[]{"summary", "--month", String.valueOf(nextMonthNumber)};
        ExpenseTrackerCLI.main(args);
        String expected = "No expenses found for month " + nextMonth + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

}