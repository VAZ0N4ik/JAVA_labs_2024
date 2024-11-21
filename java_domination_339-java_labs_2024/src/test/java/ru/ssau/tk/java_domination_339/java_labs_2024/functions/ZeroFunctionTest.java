package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ZeroFunctionTest {
    ZeroFunction ZF;

    @Test
    void test() {
        ZF = new ZeroFunction();
        Assertions.assertEquals(0, ZF.apply(1000), 1e-9);
        Assertions.assertEquals(0, ZF.apply(68504.5664654), 1e-9);
        Assertions.assertEquals(0, ZF.apply(-0.0001), 1e-9);
    }
}