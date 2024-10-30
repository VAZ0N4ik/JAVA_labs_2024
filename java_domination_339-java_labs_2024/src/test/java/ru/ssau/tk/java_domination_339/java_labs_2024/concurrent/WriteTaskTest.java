package ru.ssau.tk.java_domination_339.java_labs_2024.concurrent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class WriteTaskTest {
    private TabulatedFunction function;
    private WriteTask writeTask;
    private final double newValue = 10.0;

    @BeforeEach
    void setUp() {
        function = new LinkedListTabulatedFunction(new double[]{1, 2, 3}, new double[]{2, 4, 6});
        writeTask = new WriteTask(function, newValue);
    }

    @Test
    void testRun() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);

        writeTask.run();

        System.setOut(originalOut);

        for (int i = 0; i < function.getCount(); i++) {
            assertEquals(newValue, function.getY(i), 1e-9);
        }

        String output = outputStream.toString();

        assertTrue(output.contains("Writing for index 0 complete"));
        assertTrue(output.contains("Writing for index 1 complete"));
        assertTrue(output.contains("Writing for index 2 complete"));
    }
}
