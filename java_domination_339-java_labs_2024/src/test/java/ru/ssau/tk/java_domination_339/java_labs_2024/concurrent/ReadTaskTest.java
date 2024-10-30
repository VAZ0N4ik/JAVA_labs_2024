package ru.ssau.tk.java_domination_339.java_labs_2024.concurrent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ReadTaskTest {
    private TabulatedFunction function;
    private ReadTask readTask;

    @BeforeEach
    void setUp() {
        function = new ArrayTabulatedFunction(new double[]{1, 2, 3}, new double[]{2, 4, 6});
        readTask = new ReadTask(function);
    }

    @Test
    void testRun() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;
        System.setOut(printStream);

        readTask.run();

        System.setOut(originalOut);

        String output = outputStream.toString();

        // debug
        System.out.println("Captured output:");
        System.out.println(output);

        assertTrue(output.contains("After read: i = 0, x = 1,000000, y = 2,000000"), "First entry not found in output");
        assertTrue(output.contains("After read: i = 1, x = 2,000000, y = 4,000000"), "Second entry not found in output");
        assertTrue(output.contains("After read: i = 2, x = 3,000000, y = 6,000000"), "Third entry not found in output");
    }
}
