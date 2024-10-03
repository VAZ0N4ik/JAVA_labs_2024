package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IdentityFunctionTest {
    IdentityFunction IF;

    @Test
    void test(){
        IF = new IdentityFunction();
        Assertions.assertEquals(10, IF.apply(10), 0.00000000001);
        Assertions.assertEquals(23434.00001, IF.apply(23434.00001), 0.00000000001);
        Assertions.assertEquals(0.00001, IF.apply(0.00001), 0.00000000001);
    }
}