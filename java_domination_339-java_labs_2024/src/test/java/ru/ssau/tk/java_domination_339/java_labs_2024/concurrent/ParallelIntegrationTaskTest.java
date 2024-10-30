package ru.ssau.tk.java_domination_339.java_labs_2024.concurrent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class ParallelIntegrationTaskTest {
    private TabulatedFunction function;

    @BeforeEach
    void setUp() {
        function = new ArrayTabulatedFunction(new double[]{0, 1, 2, 3}, new double[]{0, 1, 4, 9}); // f(x) = x^2
    }

    @Test
    void testCall() throws ExecutionException, InterruptedException {
        ParallelIntegrationTask task = new ParallelIntegrationTask(function, 1, 3, 10);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Double> result = executor.submit(task);

        assertEquals(8.0, result.get(), 0.5);

        executor.shutdown();
    }

    @Test
    void testOddIntervals() {
        ParallelIntegrationTask task = new ParallelIntegrationTask(function, 0, 3, 3);
        assertThrows(IllegalArgumentException.class, task::call);
    }

}
