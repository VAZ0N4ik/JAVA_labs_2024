package ru.ssau.tk.java_domination_339.java_labs_2024.operations;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.MathFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.SqrFunction;

public class SteppingDifferentialOperatorTest {

    private static final double EPSILON = 0.02;

    @Test
    public void testLeftSteppingDifferentialOperator() {
        double step = 0.01;
        LeftSteppingDifferentialOperator leftOperator = new LeftSteppingDifferentialOperator(step);
        SqrFunction function = new SqrFunction();

        MathFunction derivative = leftOperator.derive(function);

        assertEquals(2.0, derivative.apply(1.0), EPSILON);
        assertEquals(4.0, derivative.apply(2.0), EPSILON);
        assertEquals(6.0, derivative.apply(3.0), EPSILON);
    }

    @Test
    public void testRightSteppingDifferentialOperator() {
        double step = 0.01;
        RightSteppingDifferentialOperator rightOperator = new RightSteppingDifferentialOperator(step);
        SqrFunction function = new SqrFunction();

        MathFunction derivative = rightOperator.derive(function);

        assertEquals(2.0, derivative.apply(1.0), EPSILON);
        assertEquals(4.0, derivative.apply(2.0), EPSILON);
        assertEquals(6.0, derivative.apply(3.0), EPSILON);
    }

    @Test
    public void testMiddleSteppingDifferentialOperator() {
        double step = 0.01;
        MiddleSteppingDifferentialOperator middleOperator = new MiddleSteppingDifferentialOperator(step);
        SqrFunction function = new SqrFunction();

        MathFunction derivative = middleOperator.derive(function);

        assertEquals(2.0, derivative.apply(1.0), EPSILON);
        assertEquals(4.0, derivative.apply(2.0), EPSILON);
        assertEquals(6.0, derivative.apply(3.0), EPSILON);
    }

    @Test
    public void testLeftSteppingDifferentialOperatorWithSin() {
        double step = 0.01;
        MathFunction sinFunction = Math::sin;
        LeftSteppingDifferentialOperator operator = new LeftSteppingDifferentialOperator(step);
        MathFunction derivative = operator.derive(sinFunction);

        double x = Math.PI / 4;
        double expected = Math.cos(x);
        assertEquals(expected, derivative.apply(x), EPSILON);
    }

    @Test
    public void testRightSteppingDifferentialOperatorWithExp() {
        double step = 0.01;
        MathFunction expFunction = Math::exp;
        RightSteppingDifferentialOperator operator = new RightSteppingDifferentialOperator(step);
        MathFunction derivative = operator.derive(expFunction);

        double x = 1.0;
        double expected = Math.exp(x);
        assertEquals(expected, derivative.apply(x), EPSILON);
    }

    @Test
    public void testMiddleSteppingDifferentialOperatorWithLog() {
        double step = 0.01;
        MathFunction logFunction = Math::log;
        MiddleSteppingDifferentialOperator operator = new MiddleSteppingDifferentialOperator(step);
        MathFunction derivative = operator.derive(logFunction);

        double x = Math.E;
        double expected = 1 / x;
        assertEquals(expected, derivative.apply(x), EPSILON);
    }

    @Test
    public void testLeftSteppingDifferentialOperatorApplyThrowsException() {
        double step = 0.01;
        LeftSteppingDifferentialOperator operator = new LeftSteppingDifferentialOperator(step);
        assertThrows(UnsupportedOperationException.class, () -> operator.apply(1.0));
    }

    @Test
    public void testRightSteppingDifferentialOperatorApplyThrowsException() {
        double step = 0.01;
        RightSteppingDifferentialOperator operator = new RightSteppingDifferentialOperator(step);
        assertThrows(UnsupportedOperationException.class, () -> operator.apply(1.0));
    }

    @Test
    public void testMiddleSteppingDifferentialOperatorApplyThrowsException() {
        double step = 0.01;
        MiddleSteppingDifferentialOperator operator = new MiddleSteppingDifferentialOperator(step);
        assertThrows(UnsupportedOperationException.class, () -> operator.apply(1.0));
    }
}
