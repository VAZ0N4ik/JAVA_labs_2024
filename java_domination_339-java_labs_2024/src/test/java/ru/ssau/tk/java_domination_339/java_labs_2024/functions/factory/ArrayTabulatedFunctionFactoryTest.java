package ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import static org.junit.jupiter.api.Assertions.*;

class ArrayTabulatedFunctionFactoryTest {
    ArrayTabulatedFunctionFactory fact = new ArrayTabulatedFunctionFactory();
    @Test
    void create() {
        assertInstanceOf(ArrayTabulatedFunction.class, fact.create(new double[]{1, 2, 3}, new double[]{1, 2,3}));
        assertInstanceOf(ArrayTabulatedFunction.class, fact.create(new double[]{0,1}, new double[]{-1,-2}));
    }
}