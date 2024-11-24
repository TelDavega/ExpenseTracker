package es.teldavega;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        String[] args = {"add", "--description", "food", "--amount", "10"};
        ExpenseTrackerCLI.main(args);
        String expected = "Expense added successfully (ID: 1)" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
        outContent.reset();
        args = new String[]{"add", "--description", "dinner", "--amount", "20.53"};
        ExpenseTrackerCLI.main(args);
        expected = "Expense added successfully (ID: 2)" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testUpdateExpense() throws IOException {
        String[] args = {"add", "--description", "food", "--amount", "10"};
        ExpenseTrackerCLI.main(args);
        outContent.reset();

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

    @Test
    void testDeleteExpense() throws IOException {
        String[] args = {"add", "--description", "food", "--amount", "10"};
        ExpenseTrackerCLI.main(args);
        outContent.reset();

        args = new String[]{"delete", "--id", "1"};
        ExpenseTrackerCLI.main(args);
        String expected = "Expense deleted successfully" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testListExpenses() throws IOException {
        String[] args = {"add", "--description", "food", "--amount", "10"};
        ExpenseTrackerCLI.main(args);
        Date date1 = new Date();
        args = new String[]{"add", "--description", "dinner", "--amount", "20.53"};
        ExpenseTrackerCLI.main(args);
        Date date2 = new Date();
        outContent.reset();

        args = new String[]{"list"};
        ExpenseTrackerCLI.main(args);
        String headerFormat = "%-5s %-27s %-20s %-10s%n";
        String rowFormat = "%-5d %-27s %-20s $%-9.2f%n";
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm z yyyy");

        String expectedHeader = String.format(headerFormat, "ID", "        Date        ", "Description  ", "Amount ");
        String expectedRow1 = String.format(rowFormat, 1, dateFormat.format(date1), "food", 10.0);
        String expectedRow2 = String.format(rowFormat, 2, dateFormat.format(date2), "dinner", 20.53);
        String expected = expectedHeader + expectedRow1 + expectedRow2;
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testSummary() throws IOException {
        String[] args = {"add", "--description", "food", "--amount", "10"};
        ExpenseTrackerCLI.main(args);
        args = new String[]{"add", "--description", "dinner", "--amount", "20.53"};
        ExpenseTrackerCLI.main(args);
        outContent.reset();

        args = new String[]{"summary"};
        ExpenseTrackerCLI.main(args);
        String expected = "Total expenses: $30.53" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testSummaryByMonth() throws IOException {
        Calendar calendar = Calendar.getInstance();
        int currentMonthNumber = calendar.get(Calendar.MONTH) + 1;
        String currentMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);

        String[] args = {"add", "--description", "food", "--amount", "10"};
        ExpenseTrackerCLI.main(args);
        args = new String[]{"add", "--description", "dinner", "--amount", "20.53"};
        ExpenseTrackerCLI.main(args);
        outContent.reset();

        args = new String[]{"summary", "--month", String.valueOf(currentMonthNumber)};
        ExpenseTrackerCLI.main(args);
        String expected = "Total expenses for month " + currentMonth + ": $30.53" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

}