package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AndThenTest {
    MathFunction MF;

    @Test
    void test1() {
        MF = new ConstantFunction(16)
                .andThen(new SqrFunction());

        Assertions.assertEquals(256, MF.apply(999), 1e-9);
        Assertions.assertEquals(256, MF.apply(1e-7), 1e-9);
    }

    @Test
    void test2() {
        MF = new ZeroFunction()
                .andThen(new SqrFunction())
                .andThen(new ZeroFunction())
                .andThen(new ConstantFunction(739));

        Assertions.assertEquals(739, MF.apply(15398.998), 1e-9);
        Assertions.assertEquals(739, MF.apply(750*1e-7), 1e-9);
    }

    @Test
    void test3() {
        MF = new ConstantFunction(0.0001)
                .andThen(new ChordMethodFunction(0, 10, new MathFunction() {
                    @Override
                    public double apply(double x) {
                        return x * x * x - 18 * x - 83;
                    }
                }));

        Assertions.assertEquals(5.7051, MF.apply(0.1), 0.001);
        Assertions.assertEquals(5.7051, MF.apply(1e-9), 0.001);
    }
}
