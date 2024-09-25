package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NewtonMethodFunctionTest {
    NewtonMethodFunction nF1, nF2;
    MathFunction testFunc1, testFunc2;
    @Test
    void test(){
        testFunc1 = new MathFunction(){
            public double apply(double x) {
                return x * x * x - 1;
            }
        };
        testFunc2 = new MathFunction(){
            public double apply(double x) {
                return x * x * x + 2*x*x -x  - 1;
            }
        };
        nF1 = new NewtonMethodFunction(testFunc1);
        nF2 = new NewtonMethodFunction(testFunc2);

        Assertions.assertEquals(1, nF1.apply(10), 0.01);
        Assertions.assertEquals(0.8, nF2.apply(20), 0.01);
    }
}