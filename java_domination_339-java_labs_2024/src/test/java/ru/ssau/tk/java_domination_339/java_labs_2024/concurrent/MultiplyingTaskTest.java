package ru.ssau.tk.java_domination_339.java_labs_2024.concurrent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;

import static org.junit.jupiter.api.Assertions.*;

class MultiplyingTaskTest {
    private TabulatedFunction function;
    private MultiplyingTask task;

    @BeforeEach
    void setUp() {
        function = new ArrayTabulatedFunction(new double[]{1, 2, 3}, new double[]{1, 2, 3});
        task = new MultiplyingTask(function);
    }

    @Test
    void testRun() {
        task.run();

        assertEquals(2.0, function.getY(0));
        assertEquals(4.0, function.getY(1));
        assertEquals(6.0, function.getY(2));
    }

    @Test
    void testIsFinished() {
        assertFalse(task.isFinished());

        task.run();

        assertTrue(task.isFinished());
    }

    @Test
    void testMultipleThreads() throws InterruptedException {

        TabulatedFunction function1 = new ArrayTabulatedFunction(new double[]{1, 2, 3}, new double[]{1, 2, 3});
        TabulatedFunction function2 = new ArrayTabulatedFunction(new double[]{1, 2, 3}, new double[]{1, 2, 3});

        MultiplyingTask task1 = new MultiplyingTask(function1);
        MultiplyingTask task2 = new MultiplyingTask(function2);

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertEquals(2.0, function1.getY(0));
        assertEquals(4.0, function1.getY(1));
        assertEquals(6.0, function1.getY(2));

        assertEquals(2.0, function2.getY(0));
        assertEquals(4.0, function2.getY(1));
        assertEquals(6.0, function2.getY(2));
    }
}
