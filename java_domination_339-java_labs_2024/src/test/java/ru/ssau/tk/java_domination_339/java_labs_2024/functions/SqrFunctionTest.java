package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SqrFunctionTest {
    SqrFunction SF;

    @Test
    void test() {
        SF = new SqrFunction();
        Assertions.assertEquals(10000, SF.apply(100), 0.00000000001);
        Assertions.assertEquals(0, SF.apply(0), 0.00000000001);
        Assertions.assertEquals(0.0000000001, SF.apply(0.00001), 0.00000000001);
    }
}