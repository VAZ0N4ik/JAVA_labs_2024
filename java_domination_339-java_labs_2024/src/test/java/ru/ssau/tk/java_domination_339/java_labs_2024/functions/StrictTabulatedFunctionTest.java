package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class StrictTabulatedFunctionTest {
    double eps = 1e-8;
    StrictTabulatedFunction list = new StrictTabulatedFunction(new LinkedListTabulatedFunction(new double[]{-5, -3.6, 0.01, 1, 4}, new double[]{0, 3.6, 5, 100, 0.1}));
    StrictTabulatedFunction array = new StrictTabulatedFunction(new ArrayTabulatedFunction(new double[]{1,2,3},new double[]{1,2,3}));
    @Test
    void iterator() {
        Iterator<Point> iterator1 = list.iterator();
        int i = 0;
        while (iterator1.hasNext()) {
            Point point = iterator1.next();
            assertEquals(list.getX(i), point.x,eps);
            assertEquals(list.getY(i), point.y,eps);
            ++i;
        }
        i = 0;
        for (Point point : list){
            assertEquals(list.getX(i), point.x,eps);
            assertEquals(list.getY(i), point.y,eps);
            ++i;
        }

        Iterator<Point> iterator2 = array.iterator();
        i = 0;
        while (iterator2.hasNext()) {
            Point point = iterator2.next();
            assertEquals(array.getX(i), point.x,eps);
            assertEquals(array.getY(i), point.y,eps);
            ++i;
        }
        i = 0;
        for (Point point : array){
            assertEquals(array.getX(i), point.x,eps);
            assertEquals(array.getY(i), point.y,eps);
            ++i;
        }
    }

    @Test
    void getCount() {
        assertEquals(3,array.getCount(), eps);
        assertEquals(5,list.getCount(), eps);
    }

    @Test
    void getX() {
        assertEquals(1,array.getX(0), eps);
        assertEquals(-5,list.getX(0), eps);
    }

    @Test
    void getY() {
        assertEquals(1,array.getY(0), eps);
        assertEquals(0,list.getY(0), eps);
    }

    @Test
    void setY() {
        list.setY(0,5);
        array.setY(0,5);
        assertEquals(5,array.getY(0), eps);
        assertEquals(5,list.getY(0), eps);
    }

    @Test
    void indexOfX() {
        assertEquals(2, list.indexOfX(0.01), eps);
        assertEquals(-1, array.indexOfX(4), eps);
    }

    @Test
    void indexOfY() {
        assertEquals(3, list.indexOfY(100), eps);
        assertEquals(-1, array.indexOfY(4), eps);
    }

    @Test
    void leftBound() {
        assertEquals(-5, list.leftBound(), eps);
        assertEquals(1, array.leftBound(), eps);
    }

    @Test
    void rightBound() {
        assertEquals(4, list.rightBound(), eps);
        assertEquals(3, array.rightBound(), eps);
    }

    @Test
    void apply() {
        assertEquals(0,list.apply(-5));
        assertEquals(3.6,list.apply(-3.6));
        assertEquals(1,array.apply(1));
        assertEquals(2,array.apply(2));
        assertThrows(UnsupportedOperationException.class,()->list.apply(100));
        assertThrows(UnsupportedOperationException.class,()->array.apply(4));
    }
}