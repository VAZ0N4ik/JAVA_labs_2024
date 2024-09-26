package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UnitFunctionTest {
    UnitFunction UF;

    @Test
    void test(){
        UF = new UnitFunction();
        Assertions.assertEquals(1, UF.apply(1000), 1e-9);
        Assertions.assertEquals(1, UF.apply(68504.5664654), 1e-9);
        Assertions.assertEquals(1, UF.apply(-0.0001), 1e-9);
    }
}