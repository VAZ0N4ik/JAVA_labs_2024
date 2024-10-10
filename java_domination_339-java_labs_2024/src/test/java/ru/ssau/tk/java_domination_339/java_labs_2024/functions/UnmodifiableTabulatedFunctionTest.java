package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class UnmodifiableTabulatedFunctionTest {

    @Test
    public void testArrayTabulatedFunction() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {2.0, 4.0, 6.0};
        TabulatedFunction originalFunction = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableFunction = new UnmodifiableTabulatedFunction(originalFunction);

        assertEquals(3, unmodifiableFunction.getCount());
        assertEquals(1.0, unmodifiableFunction.getX(0));
        assertEquals(2.0, unmodifiableFunction.getY(0));
        assertEquals(3.0, unmodifiableFunction.getX(2));
        assertEquals(6.0, unmodifiableFunction.getY(2));

        assertEquals(0, unmodifiableFunction.indexOfX(1.0));
        assertEquals(1, unmodifiableFunction.indexOfY(4.0));
        assertEquals(-1, unmodifiableFunction.indexOfX(4.0));
        assertEquals(-1, unmodifiableFunction.indexOfY(5.0));

        assertEquals(1.0, unmodifiableFunction.leftBound());
        assertEquals(3.0, unmodifiableFunction.rightBound());

        assertThrows(UnsupportedOperationException.class, () -> unmodifiableFunction.setY(0, 5.0));
    }

    @Test
    public void testLinkedListTabulatedFunction() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {2.0, 4.0, 6.0};
        TabulatedFunction originalFunction = new LinkedListTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableFunction = new UnmodifiableTabulatedFunction(originalFunction);

        assertEquals(3, unmodifiableFunction.getCount());
        assertEquals(1.0, unmodifiableFunction.getX(0));
        assertEquals(2.0, unmodifiableFunction.getY(0));
        assertEquals(3.0, unmodifiableFunction.getX(2));
        assertEquals(6.0, unmodifiableFunction.getY(2));

        assertEquals(0, unmodifiableFunction.indexOfX(1.0));
        assertEquals(1, unmodifiableFunction.indexOfY(4.0));
        assertEquals(-1, unmodifiableFunction.indexOfX(4.0));
        assertEquals(-1, unmodifiableFunction.indexOfY(5.0));

        assertEquals(1.0, unmodifiableFunction.leftBound());
        assertEquals(3.0, unmodifiableFunction.rightBound());

        assertThrows(UnsupportedOperationException.class, () -> unmodifiableFunction.setY(0, 5.0));
    }

    @Test
    public void testIterator() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {2.0, 4.0, 6.0};
        TabulatedFunction originalFunction = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableFunction = new UnmodifiableTabulatedFunction(originalFunction);

        int index = 0;
        Iterator<Point> iterator = unmodifiableFunction.iterator();
        assertTrue(iterator.hasNext());
        Point point = iterator.next();
        assertEquals(xValues[index], point.x, 1e-9);
        assertEquals(yValues[index], point.y, 1e-9);
        ++index;
        assertTrue(iterator.hasNext());
        point = iterator.next();
        assertEquals(xValues[index], point.x, 1e-9);
        assertEquals(yValues[index], point.y, 1e-9);
        ++index;
        assertTrue(iterator.hasNext());
        point = iterator.next();
        assertEquals(xValues[index], point.x, 1e-9);
        assertEquals(yValues[index], point.y, 1e-9);
        assertFalse(iterator.hasNext());
    }


    @Test
    public void testApply() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {2.0, 4.0, 6.0};
        TabulatedFunction originalFunction = new ArrayTabulatedFunction(xValues, yValues);
        TabulatedFunction unmodifiableFunction = new UnmodifiableTabulatedFunction(originalFunction);

        assertEquals(2.0, unmodifiableFunction.apply(1.0), 1e-9);
        assertEquals(4.0, unmodifiableFunction.apply(2.0), 1e-9);
        assertEquals(6.0, unmodifiableFunction.apply(3.0), 1e-9);

        assertEquals(0.0, unmodifiableFunction.apply(0.0), 1e-9);
        assertEquals(8.0, unmodifiableFunction.apply(4.0), 1e-9);

        assertEquals(3.0, unmodifiableFunction.apply(1.5), 1e-9);
    }

    @Test
    void testUnmodifiableTabulatedFunctionWrappingStrict() {
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(new double[]{0, 1, 2}, new double[]{0, 1, 4});
        TabulatedFunction strictFunction = new StrictTabulatedFunction(baseFunction);
        TabulatedFunction unmodifiableFunction = new UnmodifiableTabulatedFunction(strictFunction);

        assertThrows(UnsupportedOperationException.class, () -> {
            unmodifiableFunction.setY(1, 5);
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            unmodifiableFunction.apply(0.5);
        });
    }

    @Test
    void testChainingWraps() {
        TabulatedFunction baseFunction = new ArrayTabulatedFunction(new double[]{0, 1, 2}, new double[]{0, 1, 4});
        TabulatedFunction unmodifiable = new UnmodifiableTabulatedFunction(baseFunction);
        TabulatedFunction strict = new StrictTabulatedFunction(unmodifiable);

        assertEquals(0, strict.getY(0));
        assertEquals(4, strict.getY(2));

        assertThrows(UnsupportedOperationException.class, () -> {
            strict.setY(0, 10);
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            strict.apply(1.5);
        });
    }

}
