package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.java_domination_339.java_labs_2024.exceptions.DifferentLengthOfArraysException;
import ru.ssau.tk.java_domination_339.java_labs_2024.exceptions.InterpolationException;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListTabulatedFunctionTest {
    double eps = 0.01;
    LinkedListTabulatedFunction clList1 = new LinkedListTabulatedFunction(new double[]{-5, -3.6, 0.01, 1, 4}, new double[]{0, 3.6, 5, 100, 0.1});
    LinkedListTabulatedFunction clList2 = new LinkedListTabulatedFunction(new double[]{1, 2, 3}, new double[]{9, 10, 15});
    //LinkedListTabulatedFunction clList3 = new LinkedListTabulatedFunction(new double[]{1}, new double[]{1});
    LinkedListTabulatedFunction dList1 = new LinkedListTabulatedFunction(new SqrFunction(), -10, 20, 30);
    LinkedListTabulatedFunction dList2 = new LinkedListTabulatedFunction(new SqrFunction(), 1, 1, 5);
    LinkedListTabulatedFunction dList3 = new LinkedListTabulatedFunction(new ConstantFunction(3), 5, -5, 5);

    @Test
    void testGetCount() {
        assertEquals(5, clList1.getCount(), eps);
        assertEquals(3, clList2.getCount(), eps);
        assertEquals(30, dList1.getCount(), eps);
        assertEquals(5, dList2.getCount(), eps);
        assertEquals(5, dList3.getCount(), eps);
    }

    @Test
    void testRightBound() {
        assertEquals(4, clList1.rightBound(), eps);
        assertEquals(3, clList2.rightBound(), eps);
        assertEquals(20, dList1.rightBound(), eps);
        assertEquals(1, dList2.rightBound(), eps);
    }

    @Test
    void testLeftBound() {
        assertEquals(-5, clList1.leftBound(), eps);
        assertEquals(1, clList2.leftBound(), eps);
        assertEquals(-10, dList1.leftBound(), eps);
        assertEquals(1, dList2.leftBound(), eps);
    }

    @Test
    void testGetX() {
        assertEquals(-3.6, clList1.getX(1), eps);
        assertEquals(3, clList2.getX(2), eps);
        assertEquals(-10, dList1.getX(0), eps);
        assertEquals(1, dList2.getX(3), eps);
        assertThrows(IllegalArgumentException.class, () -> clList1.getX(-1));
        assertThrows(IllegalArgumentException.class, () -> clList1.getX(7));
        assertThrows(IllegalArgumentException.class, () -> dList1.getX(-1));
        assertThrows(IllegalArgumentException.class, () -> dList1.getX(30));
    }

    @Test
    void testGetY() {
        assertEquals(3.6, clList1.getY(1), eps);
        assertEquals(15, clList2.getY(2), eps);
        assertEquals(100, dList1.getY(0), eps);
        assertEquals(1, dList2.getY(3), eps);
        assertThrows(IllegalArgumentException.class, () -> clList1.getY(-1));
        assertThrows(IllegalArgumentException.class, () -> clList1.getY(7));
        assertThrows(IllegalArgumentException.class, () -> dList1.getY(-1));
        assertThrows(IllegalArgumentException.class, () -> dList1.getY(30));
    }

    @Test
    void testSetY() {
        clList1.setY(0, -100);
        clList2.setY(2, 100);
        dList1.setY(1, -9.5);
        assertEquals(-100, clList1.getY(0), eps);
        assertEquals(100, clList2.getY(2), eps);
        assertEquals(-9.5, dList1.getY(1), eps);
        assertThrows(IllegalArgumentException.class, () -> clList1.setY(-1,0));
        assertThrows(IllegalArgumentException.class, () -> clList1.setY(7,0));
        assertThrows(IllegalArgumentException.class, () -> dList1.setY(-1,0));
        assertThrows(IllegalArgumentException.class, () -> dList1.setY(30,0));
    }

    @Test
    void testIndexOfX() {
        assertEquals(-1, clList1.indexOfX(100), eps);
        assertEquals(2, clList1.indexOfX(0.01), eps);
        assertEquals(29, dList1.indexOfX(20), eps);
    }

    @Test
    void testIndexOfY() {
        assertEquals(-1, clList1.indexOfY(1000), eps);
        assertEquals(2, clList1.indexOfY(5), eps);
        assertEquals(29, dList1.indexOfY(400), eps);
    }

    @Test
    void testFloorIndexOfX() {
        assertEquals(3, clList1.floorIndexOfX(1), eps);
        assertEquals(1, clList2.floorIndexOfX(2.5), eps);
        assertEquals(30, dList1.floorIndexOfX(25), eps);
        assertEquals(5, dList2.floorIndexOfX(2), eps);
        assertThrows(IllegalArgumentException.class, () -> clList1.floorIndexOfX(clList1.leftBound() - 1));
        assertThrows(IllegalArgumentException.class, () -> dList1.floorIndexOfX(dList1.leftBound() - 1));

    }
    @Test
    void testExtrapolateLeft() {
        assertEquals(-12.85, clList1.extrapolateLeft(-10), eps);
        assertEquals(8, clList2.extrapolateLeft(0), eps);
        assertEquals(118.96, dList1.extrapolateLeft(-11), eps);
        //assertEquals(NaN, dList2.extrapolateLeft(0), eps);
    }

    @Test
    void testInterpolate() {
        assertEquals(33.39, clList1.interpolate(3, 3), eps);
        assertEquals(12.5, clList2.interpolate(2.5, 1), eps);
        assertEquals(361.03, dList1.interpolate(19, 28), eps);
    }

    @Test
    void testExtrapolateRight() {
        assertEquals(-199.7, clList1.extrapolateRight(10), eps);
        assertEquals(50, clList2.extrapolateRight(10), eps);
        assertEquals(477.93, dList1.extrapolateRight(22), eps);
    }

    @Test
    void testApply() {
        assertEquals(-199.7, clList1.apply(10), eps);
        //assertEquals(1, clList3.apply(-1), eps);
        //assertEquals(1, clList3.apply(1), eps);
        //assertEquals(1, clList3.apply(2), eps);
        assertEquals(9.1, clList2.apply(1.1), eps);
        assertEquals(0.23, dList1.apply(0.01), eps);
        assertEquals(-12.85, clList1.apply(-10), eps);
        assertEquals(118.96, dList1.apply(-11), eps);
        assertEquals(-199.7, clList1.apply(10), eps);
        assertEquals(0, clList1.apply(-5), eps);
        assertEquals(0.1, clList1.apply(4), eps);
        assertEquals(9, clList2.apply(1), eps);
        assertEquals(15, clList2.apply(3), eps);
        assertEquals(3.6, clList1.apply(-3.6), eps);
        assertEquals(100, dList1.apply(-10), eps);
        assertEquals(400, dList1.apply(20), eps);
        assertEquals(1, dList2.apply(1), eps);
    }

    final private LinkedListTabulatedFunction func = new LinkedListTabulatedFunction(new double[]{1.0, 2.0}, new double[]{1.0, 2.0});

    /*@Test
    public void testInsertIntoEmptyList() {
        LinkedListTabulatedFunction emptyFunction = new LinkedListTabulatedFunction(new double[]{}, new double[]{});
        emptyFunction.insert(1.0, 2.0);
        assertEquals(1, emptyFunction.getCount());
        assertEquals(1.0, emptyFunction.getX(0));
        assertEquals(2.0, emptyFunction.getY(0));
    }*/

    @Test
    public void testInsertUniqueXGreater() {
        func.insert(3.0, 3.0);
        assertEquals(3, func.getCount());
        assertEquals(3.0, func.getX(2));
        assertEquals(3.0, func.getY(2));
    }

    @Test
    public void testInsertUniqueXLess() {
        func.insert(0.5, 0.5);
        assertEquals(3, func.getCount());
        assertEquals(0.5, func.getX(0));
        assertEquals(1.0, func.getX(1));
    }

    @Test
    public void testInsertUniqueXBetween() {
        func.insert(1.5, 1.5);
        assertEquals(3, func.getCount());
        assertEquals(1.5, func.getX(1));
    }

    @Test
    public void testInsertDuplicateX() {
        func.insert(1.0, 10.0);
        assertEquals(2, func.getCount());
        assertEquals(10.0, func.getY(0));
    }

    final private LinkedListTabulatedFunction list_for_remove = new LinkedListTabulatedFunction(new double[]{1.0, 2.0, 3.5}, new double[]{3.5, 0.1, -1});

    @Test
    public void testRemove() {
        list_for_remove.remove(1);
        assertEquals(2, list_for_remove.getCount(), eps);
        assertEquals(-1, list_for_remove.getY(1), eps);
        list_for_remove.remove(1);
        assertEquals(1, list_for_remove.getCount(), eps);
        assertEquals(3.5, list_for_remove.getY(0), eps);
        list_for_remove.remove(0);
        assertEquals(0, list_for_remove.getCount(), eps);
        assertThrows(IllegalArgumentException.class, () -> clList1.remove(-1));
        assertThrows(IllegalArgumentException.class, () -> clList1.remove(7));
        assertThrows(IllegalArgumentException.class, () -> dList1.remove(-1));
        assertThrows(IllegalArgumentException.class, () -> dList1.remove(30));
    }

    @Test
    void testCheckLength() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 2.0}; // Different lengths

        assertThrows(DifferentLengthOfArraysException.class, () -> {
            new LinkedListTabulatedFunction(xValues, yValues);
        });
    }

    @Test
    void testCheckSorted() {
        double[] unsortedXValues = {1.0, 2.0, 3.0, 2.0};
        double[] sortedXValues = {1.0, 2.0, 3.0};

        // Should not throw exception for sorted array
        assertDoesNotThrow(() -> {
            new LinkedListTabulatedFunction(sortedXValues, new double[]{1, 2, 3});
        });

        // Should throw exception for unsorted array
        assertThrows(ArrayIsNotSortedException.class, () -> {
            new LinkedListTabulatedFunction(unsortedXValues, new double[]{1, 2, 3, 4});
        });
    }

    @Test
    void testInterpolationException() {
        LinkedListTabulatedFunction function = new LinkedListTabulatedFunction(new double[]{1, 2, 3}, new double[]{1, 4, 9});
        assertThrows(InterpolationException.class, () -> function.interpolate(0, 0));
        assertThrows(InterpolationException.class, () -> function.interpolate(4, 1));
    }

}