package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConstantFunctionTest {
    ConstantFunction CF;

    @Test
    void test1() {
        CF = new ConstantFunction(0.00001);
        Assertions.assertEquals(0.00001, CF.apply(1000), 1e-9);
        Assertions.assertEquals(0.00001, CF.apply(68504.5664654), 1e-9);
        Assertions.assertEquals(0.00001, CF.apply(-0.0001), 1e-9);
    }

    @Test
    void test2() {
        CF = new ConstantFunction(100);
        Assertions.assertEquals(100, CF.apply(1000), 1e-9);
        Assertions.assertEquals(100, CF.apply(68504.5664654), 1e-9);
        Assertions.assertEquals(100, CF.apply(-0.0001), 1e-9);
    }

    @Test
    void test3() {
        CF = new ConstantFunction(1101);
        Assertions.assertEquals(1101, CF.getConstant(), 1e-9);
    }
}