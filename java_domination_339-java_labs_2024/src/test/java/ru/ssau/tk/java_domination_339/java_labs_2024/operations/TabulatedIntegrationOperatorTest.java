package ru.ssau.tk.java_domination_339.java_labs_2024.operations;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.operations.TabulatedIntegrationOperator;

import static org.junit.jupiter.api.Assertions.*;

class TabulatedIntegrationOperatorTest {

    @Test
    void testConstructorWithNumThreads() {
        TabulatedIntegrationOperator integrationOperator = new TabulatedIntegrationOperator(4);
        assertTrue(integrationOperator.numThreads > 0);
    }

    @Test
    void testDefaultConstructor() {
        TabulatedIntegrationOperator integrationOperator = new TabulatedIntegrationOperator();
        assertTrue(integrationOperator.numThreads > 0);
    }

    @Test
    void testIntegrateArrayFunction() {
        TabulatedFunction function = new ArrayTabulatedFunction(new double[]{0, 1, 2}, new double[]{0, 1, 2});
        TabulatedIntegrationOperator integrationOperator = new TabulatedIntegrationOperator();
        double result = integrationOperator.integrate(function);
        assertEquals(2.0, result, 0.1);
    }

    @Test
    void testIntegrateLinkedListFunction() {
        TabulatedFunction function = new LinkedListTabulatedFunction(new double[]{0, 1, 2}, new double[]{0, 1, 2});
        TabulatedIntegrationOperator integrationOperator = new TabulatedIntegrationOperator();
        double result = integrationOperator.integrate(function);
        assertEquals(2.0, result, 0.1);
    }

    @Test
    void testApplyMethod() {
        TabulatedIntegrationOperator integrationOperator = new TabulatedIntegrationOperator();
        assertThrows(UnsupportedOperationException.class, () -> integrationOperator.apply(0));
    }
}
