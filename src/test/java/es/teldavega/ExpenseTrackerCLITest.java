package es.teldavega;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTrackerCLITest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private static final Logger log = LogManager.getLogger(ExpenseTrackerCLITest.class);

    @BeforeEach
    void setUp() {
        log.info("Clearing output content");
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        log.info("Resetting output content");
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void testAddExpense() {
        log.info("Testing add expense");
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
    void testUpdateExpense() {
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
    void testDeleteExpense() {
        String[] args = {"add", "--description", "food", "--amount", "10"};
        ExpenseTrackerCLI.main(args);
        outContent.reset();

        args = new String[]{"delete", "--id", "1"};
        ExpenseTrackerCLI.main(args);
        String expected = "Expense deleted successfully" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testListExpenses() {
        String[] args = {"add", "--description", "food", "--amount", "10"};
        ExpenseTrackerCLI.main(args);
        args = new String[]{"add", "--description", "dinner", "--amount", "20.53"};
        ExpenseTrackerCLI.main(args);
        outContent.reset();

        args = new String[]{"list"};
        ExpenseTrackerCLI.main(args);
        String expected = "ID  Description  Amount" + System.lineSeparator() +
                "1  food  $10.0" + System.lineSeparator() +
                "2  dinner  $20.53" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testSummary() {
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
    void testSummaryByMonth() {
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