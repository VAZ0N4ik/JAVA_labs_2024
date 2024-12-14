package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.TabulatedFunctionFactory;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class StrictTabulatedFunctionTest {
    double eps = 1e-8;
    StrictTabulatedFunction list = new StrictTabulatedFunction(new LinkedListTabulatedFunction(new double[]{-5, -3.6, 0.01, 1, 4}, new double[]{0, 3.6, 5, 100, 0.1}));
    StrictTabulatedFunction array = new StrictTabulatedFunction(new ArrayTabulatedFunction(new double[]{1, 2, 3}, new double[]{1, 2, 3}));

    @Test
    void iterator() {
        Iterator<Point> iterator1 = list.iterator();
        int i = 0;
        while (iterator1.hasNext()) {
            Point point = iterator1.next();
            assertEquals(list.getX(i), point.x, eps);
            assertEquals(list.getY(i), point.y, eps);
            ++i;
        }
        i = 0;
        for (Point point : list) {
            assertEquals(list.getX(i), point.x, eps);
            assertEquals(list.getY(i), point.y, eps);
            ++i;
        }

        Iterator<Point> iterator2 = array.iterator();
        i = 0;
        while (iterator2.hasNext()) {
            Point point = iterator2.next();
            assertEquals(array.getX(i), point.x, eps);
            assertEquals(array.getY(i), point.y, eps);
            ++i;
        }
        i = 0;
        for (Point point : array) {
            assertEquals(array.getX(i), point.x, eps);
            assertEquals(array.getY(i), point.y, eps);
            ++i;
        }
    }

    @Test
    void getCount() {
        assertEquals(3, array.getCount(), eps);
        assertEquals(5, list.getCount(), eps);
    }

    @Test
    void getX() {
        assertEquals(1, array.getX(0), eps);
        assertEquals(-5, list.getX(0), eps);
    }

    @Test
    void getY() {
        assertEquals(1, array.getY(0), eps);
        assertEquals(0, list.getY(0), eps);
    }

    @Test
    void setY() {
        list.setY(0, 5);
        array.setY(0, 5);
        assertEquals(5, array.getY(0), eps);
        assertEquals(5, list.getY(0), eps);
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
        assertEquals(0, list.apply(-5));
        assertEquals(3.6, list.apply(-3.6));
        assertEquals(1, array.apply(1));
        assertEquals(2, array.apply(2));
        assertThrows(UnsupportedOperationException.class, () -> list.apply(100));
        assertThrows(UnsupportedOperationException.class, () -> array.apply(4));
    }


    @Test
    void testUnmodifiable() {
        LinkedListTabulatedFunction ll = new LinkedListTabulatedFunction(new double[]{1.1, 2.2, 3.3}, new double[]{3.3, 4.4, 5.5});
        UnmodifiableTabulatedFunction unmod = new UnmodifiableTabulatedFunction(ll);
        StrictTabulatedFunction strict = new StrictTabulatedFunction(unmod);
        assertThrows(UnsupportedOperationException.class, () -> strict.apply(100));
        assertThrows(UnsupportedOperationException.class, () -> strict.apply(1.5));
        assertThrows(UnsupportedOperationException.class, () -> strict.setY(1, 1));
        assertThrows(UnsupportedOperationException.class, () -> strict.setY(3, 1));

        LinkedListTabulatedFunction arr = new LinkedListTabulatedFunction(new double[]{-1, 0, 1.1}, new double[]{3.3, 4.4, 5.5});
        StrictTabulatedFunction strict2 = new StrictTabulatedFunction(unmod);
        assertThrows(UnsupportedOperationException.class, () -> strict2.apply(100));
        assertThrows(UnsupportedOperationException.class, () -> strict2.apply(1.5));
        assertThrows(UnsupportedOperationException.class, () -> strict2.setY(1, 1));
        assertThrows(UnsupportedOperationException.class, () -> strict2.setY(3, 1));
    }

    private double[] xValues = new double[]{1.0, 2.0, 3.0};
    private double[] yValues = new double[]{2.0, 4.0, 6.0};

    private TabulatedFunctionFactory arrayFactory = new ArrayTabulatedFunctionFactory();
    private TabulatedFunctionFactory linkedListFactory = new LinkedListTabulatedFunctionFactory();

    @Test
    public void testCreateStrictArray() {
        TabulatedFunction strictArrayFunction = arrayFactory.createStrict(xValues, yValues);
        assertInstanceOf(StrictTabulatedFunction.class, strictArrayFunction);

        // Проверка работы метода apply
        assertEquals(2.0, strictArrayFunction.apply(1.0));
        assertEquals(4.0, strictArrayFunction.apply(2.0));
        assertThrows(UnsupportedOperationException.class, () -> strictArrayFunction.apply(2.5)); // Недопустимая интерполяция

        // Проверка работы setY
        strictArrayFunction.setY(1, 5.0);
        assertEquals(5.0, strictArrayFunction.getY(1));

        // Проверка на наличие точек
        assertEquals(3, strictArrayFunction.getCount());
        assertEquals(1.0, strictArrayFunction.getX(0));
        assertEquals(5.0, strictArrayFunction.getY(1));
    }

    @Test
    public void testCreateStrictLinkedList() {
        TabulatedFunction strictLinkedListFunction = linkedListFactory.createStrict(xValues, yValues);
        assertInstanceOf(StrictTabulatedFunction.class, strictLinkedListFunction);

        // Проверка работы метода apply
        assertEquals(2.0, strictLinkedListFunction.apply(1.0));
        assertEquals(4.0, strictLinkedListFunction.apply(2.0));
        assertThrows(UnsupportedOperationException.class, () -> strictLinkedListFunction.apply(2.5)); // Недопустимая интерполяция

        // Проверка работы setY
        strictLinkedListFunction.setY(1, 5.0);
        assertEquals(5.0, strictLinkedListFunction.getY(1));

        // Проверка на наличие точек
        assertEquals(3, strictLinkedListFunction.getCount());
        assertEquals(1.0, strictLinkedListFunction.getX(0));
        assertEquals(5.0, strictLinkedListFunction.getY(1));
    }

    @Test
    public void testCreateStrictUnmodifiableArray() {
        TabulatedFunction strictUnmodifiableArrayFunction = arrayFactory.createStrictUnmodifiable(xValues, yValues);
        assertInstanceOf(StrictTabulatedFunction.class, strictUnmodifiableArrayFunction);

        // Проверка работы метода apply
        assertEquals(2.0, strictUnmodifiableArrayFunction.apply(1.0));
        assertEquals(4.0, strictUnmodifiableArrayFunction.apply(2.0));
        assertThrows(UnsupportedOperationException.class, () -> strictUnmodifiableArrayFunction.apply(2.5)); // Недопустимая интерполяция

        // Проверка на недоступность модификации
        assertThrows(UnsupportedOperationException.class, () -> strictUnmodifiableArrayFunction.setY(1, 10.0));
    }

    @Test
    public void testCreateStrictUnmodifiableLinkedList() {
        TabulatedFunction strictUnmodifiableLinkedListFunction = linkedListFactory.createStrictUnmodifiable(xValues, yValues);
        assertInstanceOf(StrictTabulatedFunction.class, strictUnmodifiableLinkedListFunction);

        // Проверка работы метода apply
        assertEquals(2.0, strictUnmodifiableLinkedListFunction.apply(1.0));
        assertEquals(4.0, strictUnmodifiableLinkedListFunction.apply(2.0));
        assertThrows(UnsupportedOperationException.class, () -> strictUnmodifiableLinkedListFunction.apply(2.5)); // Недопустимая интерполяция

        // Проверка на недоступность модификации
        assertThrows(UnsupportedOperationException.class, () -> strictUnmodifiableLinkedListFunction.setY(1, 10.0));
    }

    @Test
    public void testUnmodifiableBehavior() {
        TabulatedFunction unmodifiableFunction = new UnmodifiableTabulatedFunction(arrayFactory.create(xValues, yValues));

        // Проверка работы методов get и apply
        assertEquals(3, unmodifiableFunction.getCount());
        assertEquals(2.0, unmodifiableFunction.getY(0));
        assertEquals(4.0, unmodifiableFunction.getY(1));
        assertEquals(6.0, unmodifiableFunction.getY(2));
        assertEquals(4.0, unmodifiableFunction.apply(2.0));

        // Проверка на недоступность модификации
        assertThrows(UnsupportedOperationException.class, () -> unmodifiableFunction.setY(1, 10.0));
    }

    @Test
    public void testStrictBehavior() {
        TabulatedFunction strictFunction = new StrictTabulatedFunction(arrayFactory.create(xValues, yValues));

        // Проверка работы метода apply
        assertEquals(2.0, strictFunction.apply(1.0));
        assertEquals(4.0, strictFunction.apply(2.0));
        assertThrows(UnsupportedOperationException.class, () -> strictFunction.apply(1.5)); // Недопустимая интерполяция
    }
}