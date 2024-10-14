package ru.ssau.tk.java_domination_339.java_labs_2024.concurrent;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.LinkedListTabulatedFunction;

import static org.junit.jupiter.api.Assertions.*;

class SynchronizedTabulatedFunctionTest {
    double eps = 1e-8;
    ArrayTabulatedFunction arr0 = new ArrayTabulatedFunction(new double[] {1,2,3},new double[] {1,2,3});
    LinkedListTabulatedFunction ll0 = new LinkedListTabulatedFunction(new double[] {1,2,3},new double[] {1,2,3});
    SynchronizedTabulatedFunction arr = new SynchronizedTabulatedFunction(arr0);
    SynchronizedTabulatedFunction ll = new SynchronizedTabulatedFunction(ll0);

    @Test
    void getCount() {
        assertEquals(3,arr.getCount(),eps);
        assertEquals(3,ll.getCount(),eps);
    }

    @Test
    void getX() {
        assertEquals(1,arr.getX(0),eps);
        assertEquals(1,ll.getX(0),eps);
    }

    @Test
    void getY() {
        assertEquals(1,arr.getY(0),eps);
        assertEquals(1,ll.getY(0),eps);
    }

    @Test
    void setY() {
        arr.setY(0,0);
        ll.setY(0,0);
        assertEquals(0,arr.getY(0),eps);
        assertEquals(0,ll.getY(0),eps);
    }

    @Test
    void indexOfX() {
        assertEquals(1,arr.indexOfX(2),eps);
        assertEquals(1,ll.indexOfX(2),eps);
    }

    @Test
    void indexOfY() {
        assertEquals(1,arr.indexOfY(2),eps);
        assertEquals(1,ll.indexOfY(2),eps);
    }

    @Test
    void leftBound() {
        assertEquals(1,arr.leftBound(),eps);
        assertEquals(1,ll.leftBound(),eps);
    }

    @Test
    void rightBound() {
        assertEquals(3,arr.rightBound(),eps);
        assertEquals(3,ll.rightBound(),eps);
    }

    @Test
    void apply() {
        assertEquals(3,arr.apply(3),eps);
        assertEquals(3,ll.apply(3),eps);
    }

    @Test
    void iterator() {

        assertInstanceOf(arr0.iterator().getClass(),arr.iterator());
        assertInstanceOf(ll0.iterator().getClass(),ll.iterator());
    }
}