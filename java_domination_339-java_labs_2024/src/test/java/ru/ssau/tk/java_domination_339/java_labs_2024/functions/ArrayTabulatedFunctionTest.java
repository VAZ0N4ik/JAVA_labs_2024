package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArrayTabulatedFunctionTest {
    ArrayTabulatedFunction func;

    @Test
    void test1() {
        double[] xValues = {0, 1, 2, 3, 4};
        double[] yValues = {0, 1, 2, 3, 4};
        func = new ArrayTabulatedFunction(xValues, yValues);
        Assertions.assertEquals(5, func.getCount());
        for (int i = 0; i < func.getCount(); ++i) {
            Assertions.assertEquals(xValues[i], func.getX(i), 1e-9);
            Assertions.assertEquals(yValues[i], func.getY(i), 1e-9);
        }
        Assertions.assertEquals(0, func.leftBound(), 1e-9);
        Assertions.assertEquals(4, func.rightBound(), 1e-9);
    }

    @Test
    void test2() {
        MathFunction linFunc = x -> 2 * x;
        double xFrom = 4;
        double xTo = -1;
        int count = 6;
        func = new ArrayTabulatedFunction(linFunc, xFrom, xTo, count);
        double[] expectedY = {-2, 0, 2, 4, 6, 8};
        for (int i = 0; i < func.getCount(); ++i) {
            Assertions.assertEquals(expectedY[i], func.getY(i), 1e-9);
        }
    }

    @Test
    void test3() {
        MathFunction constFunc = x -> 98;
        double xFrom = 0;
        double xTo = 0;
        int count = 21;
        func = new ArrayTabulatedFunction(constFunc, xFrom, xTo, count);
        for (int i = 0; i < func.getCount(); ++i) {
            Assertions.assertEquals(0, func.getX(i), 1e-9);
            Assertions.assertEquals(98, func.getY(i), 1e-9);
        }
    }

    double[] xValues = {-2, -1, 0.5, 0, 0.5, 1, 2, 3, 3.5, 4, 5};
    double[] yValues = {-8, -1, -0.125, 0, 0.125, 1, 8, 27, 42.875, 64, 125};

    @Test
    void test4() {
        func = new ArrayTabulatedFunction(xValues, yValues);
        Assertions.assertEquals(44.361864, func.apply(3.54), 0.5);
    }

    @Test
    void test5() {
        func = new ArrayTabulatedFunction(xValues, yValues);
        Assertions.assertEquals(-36, func.extrapolateLeft(-6), 1e-9);
        Assertions.assertEquals(-36, func.apply(-6), 1e-9);
    }

    @Test
    void test6() {
        func = new ArrayTabulatedFunction(xValues, yValues);
        Assertions.assertEquals(186, func.extrapolateRight(6), 1e-9);
        Assertions.assertEquals(186, func.apply(6), 1e-9);
    }

    @Test
    void test7() {
        func = new ArrayTabulatedFunction(xValues, yValues);
        Assertions.assertEquals(-0.125, func.getY(2), 1e-9);
        func.setY(2, 99.9);
        Assertions.assertEquals(99.9, func.getY(2), 1e-9);
    }

    @Test
    void test8() {
        func = new ArrayTabulatedFunction(xValues, yValues);
        Assertions.assertEquals(17.5, func.apply(2.5), 1e-9);
    }

    @Test
    void test9() {
        double[] xValues = {1};
        double[] yValues = {1};
        func = new ArrayTabulatedFunction(xValues, yValues);
        Assertions.assertEquals(1, func.extrapolateRight(1), 1e-9);
        Assertions.assertEquals(1, func.extrapolateLeft(1), 1e-9);
        Assertions.assertEquals(1, func.interpolate(1,1), 1e-9);
    }

    @Test
    void test10() {
        func = new ArrayTabulatedFunction(xValues, yValues);
        Assertions.assertEquals(0, func.indexOfX(-2), 1e-9);
        Assertions.assertEquals(0, func.indexOfY(-8), 1e-9);
        Assertions.assertEquals(-1, func.indexOfY(-100), 1e-9);
        Assertions.assertEquals(0, func.floorIndexOfX(-3), 1e-9);
        Assertions.assertEquals(10, func.floorIndexOfX(1000), 1e-9);
    }

    final ArrayTabulatedFunction func1 = new ArrayTabulatedFunction(new double[]{1.0, 2.0, 3.0}, new double[]{1.0, 2.0, 3.0});

    @Test
    public void testRemoveByIndex() {
        func1.remove(1); // Remove element at index 1 (x = 2.0)
        Assertions.assertEquals(2, func1.getCount());
        Assertions.assertEquals(1.0, func1.getX(0));
        Assertions.assertEquals(3.0, func1.getX(1));
    }

    @Test
    public void testRemoveFirstElementByIndex() {
        func1.remove(0); // Remove the first element (x = 1.0)
        Assertions.assertEquals(2, func1.getCount());
        Assertions.assertEquals(2.0, func1.getX(0));
    }

    @Test
    public void testRemoveLastElementByIndex() {
        func1.remove(2); // Remove the last element (x = 3.0)
        Assertions.assertEquals(2, func1.getCount());
        Assertions.assertEquals(1.0, func1.getX(0));
        Assertions.assertEquals(2.0, func1.getX(1));
    }

    @Test
    public void testRemoveAllElements() {
        func1.remove(0); // Remove x = 1.0
        func1.remove(0); // Remove x = 2.0
        func1.remove(0); // Remove x = 3.0
        Assertions.assertEquals(0, func1.getCount()); // Count should be zero
    }
    ArrayTabulatedFunction arrayForInsert =new ArrayTabulatedFunction(new double[]{}, new double[]{});
    @Test
    public void testInsert(){
        arrayForInsert.insert(1,1);
        Assertions.assertEquals(1, arrayForInsert.getCount(),1e-9);
        Assertions.assertEquals(1, arrayForInsert.getX(0),1e-9);
        Assertions.assertEquals(1, arrayForInsert.getY(0),1e-9);
        arrayForInsert.insert(0,0);
        Assertions.assertEquals(2, arrayForInsert.getCount(),1e-9);
        Assertions.assertEquals(0, arrayForInsert.getX(0),1e-9);
        Assertions.assertEquals(0, arrayForInsert.getY(0),1e-9);
        arrayForInsert.insert(2,2);
        Assertions.assertEquals(3, arrayForInsert.getCount(),1e-9);
        Assertions.assertEquals(2, arrayForInsert.getX(2),1e-9);
        Assertions.assertEquals(2, arrayForInsert.getY(2),1e-9);
        arrayForInsert.insert(1.5,1.5);
        Assertions.assertEquals(4, arrayForInsert.getCount(),1e-9);
        Assertions.assertEquals(1.5, arrayForInsert.getX(2),1e-9);
        Assertions.assertEquals(1.5, arrayForInsert.getY(2),1e-9);
        arrayForInsert.insert(0,5);
        Assertions.assertEquals(4, arrayForInsert.getCount(),1e-9);
        Assertions.assertEquals(0, arrayForInsert.getX(0),1e-9);
        Assertions.assertEquals(5, arrayForInsert.getY(0),1e-9);
    }
}
