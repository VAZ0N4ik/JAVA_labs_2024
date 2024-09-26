package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConstantFunctionTest {
    ConstantFunction CF;

    @Test
    void test(){
        CF = new ConstantFunction(0.00001);
        Assertions.assertEquals(0.00001, CF.apply(1000), 1e-9);
        Assertions.assertEquals(0.00001, CF.apply(68504.5664654), 1e-9);
        Assertions.assertEquals(0.00001, CF.apply(-0.0001), 1e-9);
    }
}