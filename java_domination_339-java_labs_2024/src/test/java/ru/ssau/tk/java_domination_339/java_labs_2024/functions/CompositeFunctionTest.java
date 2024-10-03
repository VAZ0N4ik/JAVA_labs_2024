package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CompositeFunctionTest {
    CompositeFunction cF, secondCF;
    IdentityFunction iF1,iF2,iF3;
    @Test
    void test(){
        iF1 = new IdentityFunction();
        iF2 = new IdentityFunction();
        iF3 = new IdentityFunction();
        secondCF = new CompositeFunction(iF1,iF2);
        cF = new CompositeFunction(iF3, secondCF);
        Assertions.assertEquals(10, secondCF.apply(10), 0.00000000001);
        Assertions.assertEquals(23434.00001, cF.apply(23434.00001), 0.00000000001);
    }

    @Test
    public void testCompositeOfArrayTabulatedFunctions() {
        double[] xValues1 = {1, 2, 3};
        double[] yValues1 = {2, 4, 6}; // f1(x) = 2x
        double[] xValues2 = {2, 4, 6};
        double[] yValues2 = {1, 2, 3}; // f2(x) = x/2

        ArrayTabulatedFunction f1 = new ArrayTabulatedFunction(xValues1, yValues1);
        ArrayTabulatedFunction f2 = new ArrayTabulatedFunction(xValues2, yValues2);

        CompositeFunction composite = new CompositeFunction(f1, f2);

        // f1(3) = 6, f2(6) = 3 => composite(3) should return 3
        assertEquals(3, composite.apply(3), 1e-9);
    }

    @Test
    public void testAndThenWithLinkedListTabulatedFunction() {
        double[] xValues = {1, 2, 3};
        double[] yValues = {1, 2, 3}; // f3(x) = x

        LinkedListTabulatedFunction f3 = new LinkedListTabulatedFunction(xValues, yValues);

        // f3(2) = 2, f4(x) = x^2
        MathFunction f4 = x ->  x * x;

        MathFunction andThenFunction = f3.andThen(f4);

        // f3(2) = 2 => f4(2) = 4
        assertEquals(4, andThenFunction.apply(2), 1e-9);
    }

    @Test
    public void testCompositeWithDifferentTabulatedFunctions() {
        double[] xValues1 = {0, 1, 2};
        double[] yValues1 = {1, 3, 5}; // f5(x) = 2x + 1
        double[] xValues2 = {1, 2, 3};
        double[] yValues2 = {1, 4, 9}; // f6(x) = x^2

        ArrayTabulatedFunction f5 = new ArrayTabulatedFunction(xValues1, yValues1);
        LinkedListTabulatedFunction f6 = new LinkedListTabulatedFunction(xValues2, yValues2);

        CompositeFunction composite = new CompositeFunction(f5, f6);

        // f5(1) = 3, f6(3) = 9 => composite(1) = f6(f5(1)) = f6(3) = 9
        assertEquals(9, composite.apply(1), 1e-9);
    }

    @Test
    public void testAndThenWithCompositeFunction() {
        double[] xValues1 = {0, 1, 2};
        double[] yValues1 = {1, 3, 5}; // f7(x) = 2x + 1
        double[] xValues2 = {-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        double[] yValues2 = {1, 0, 1, 4, 9, 16, 25, 36, 49, 64, 81, 100}; // f8(x) = x^2

        ArrayTabulatedFunction f7 = new ArrayTabulatedFunction(xValues1, yValues1);
        LinkedListTabulatedFunction f8 = new LinkedListTabulatedFunction(xValues2, yValues2);

        CompositeFunction composite1 = new CompositeFunction(f7, f8);
        CompositeFunction composite2 = new CompositeFunction(f8, f7);

        MathFunction andThenFunction = composite1.andThen(composite2);

        // Test the application
        // For x = 1, composite1(1) = f8(f7(1)) = f8(3) = 9
        // composite2(9) = f7(f8(9)) = f7(81) = 163
        assertEquals(163, andThenFunction.apply(1), 1e-9);
    }
}