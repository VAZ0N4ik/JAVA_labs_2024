package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ChordMethodFunctionTest {
    ChordMethodFunction cF1, cF2, cF3;
    MathFunction testF1, testF2, testF3;
    double testX1_1, testX2_1, testX1_2, testX2_2;

    @Test
    void test() {
        testX1_1 = 2;
        testX2_1 = 10;
        testX1_2 = -10;
        testX2_2 = 0;
        testF1 = x -> x * x * x - 18 * x - 83;

        testF2 = x -> -(x * x * x) - (x * x) + 10 * x - 83;

        testF3 = x -> x;

        cF1 = new ChordMethodFunction(testX1_1, testX2_1, testF1);
        cF2 = new ChordMethodFunction(testX1_2, testX2_2, testF2);
        cF3 = new ChordMethodFunction(testX1_2, testX2_2, testF3);

        Assertions.assertEquals(5.7051, cF1.apply(0.0001), 0.0001);
        Assertions.assertEquals(-5.5267, cF2.apply(0.0001), 0.0001);
        Assertions.assertEquals(0, cF3.apply(0.0001), 0.0001);
    }
}