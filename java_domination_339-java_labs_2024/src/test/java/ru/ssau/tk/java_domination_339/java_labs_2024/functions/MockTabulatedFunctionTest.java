package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MockTabulatedFunctionTest {
    double eps = 1e-8;
    private MockTabulatedFunction mock1 = new MockTabulatedFunction(1, 2, 3, 4);
    private MockTabulatedFunction mock2 = new MockTabulatedFunction(3.5, 0.5, 0, -5);

    @Test
    void testInterpolate() {
        assertEquals(3.5, mock1.interpolate(1.5, 1, 2, 3, 4), eps);
        assertEquals(-2.5, mock2.interpolate(2, 0.5, 3.5, -5, 0), eps);
    }

    @Test
    void testApply() {
        assertEquals(3.5, mock1.apply(1.5), eps);
        assertEquals(-2.5, mock2.apply(2), eps);
        assertEquals(-7.5, mock2.apply(-1), eps);
        assertEquals(7, mock1.apply(5), eps);
        mock1.setY(1, 6);
        assertEquals(6, mock1.apply(2), eps);
        mock2.setY(0, -4);
        assertEquals(-4, mock2.apply(0.5), eps);
    }
}