package ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.LinkedListTabulatedFunction;
import static org.junit.jupiter.api.Assertions.*;

class LinkedListTabulatedFunctionFactoryTest {
    LinkedListTabulatedFunctionFactory fact = new LinkedListTabulatedFunctionFactory();
    @Test
    void create() {
        assertInstanceOf(LinkedListTabulatedFunction.class, fact.create(new double[]{1, 2, 3}, new double[]{1, 2,3}));
        assertInstanceOf(LinkedListTabulatedFunction.class, fact.create(new double[]{0,1}, new double[]{-1,-2}));
    }
}