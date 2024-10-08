package ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;

import static org.junit.jupiter.api.Assertions.*;

class ArrayTabulatedFunctionFactoryTest {
    ArrayTabulatedFunctionFactory fact = new ArrayTabulatedFunctionFactory();
    @Test
    void create() {
        assertInstanceOf(ArrayTabulatedFunction.class, fact.create(new double[]{1, 2, 3}, new double[]{1, 2,3}));
        assertInstanceOf(ArrayTabulatedFunction.class, fact.create(new double[]{0,1}, new double[]{-1,-2}));
    }

    @Test
    public void testCreateUnmodifiableWithArrayFactory() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        TabulatedFunction function = factory.createUnmodifiable(new double[]{1, 2, 3}, new double[]{4, 5, 6});

        assertThrows(UnsupportedOperationException.class, () -> function.setY(1, 10));
    }
}