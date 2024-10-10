package ru.ssau.tk.java_domination_339.java_labs_2024.operations;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.Point;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.TabulatedFunctionFactory;

import static org.junit.jupiter.api.Assertions.*;

public class TabulatedFunctionOperationServiceTest {

    @Test
    public void testAsPoints() {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {2.0, 3.0, 5.0};
        TabulatedFunction tabulatedFunction = new ArrayTabulatedFunction(xValues, yValues);

        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

        Point[] points = service.asPoints(tabulatedFunction);

        assertEquals(3, points.length);
        assertEquals(1.0, points[0].x);
        assertEquals(2.0, points[0].y);
        assertEquals(2.0, points[1].x);
        assertEquals(3.0, points[1].y);
        assertEquals(3.0, points[2].x);
        assertEquals(5.0, points[2].y);
    }

    @Test
    public void testAsPointsEmptyFunction() {
        double[] xValues = {};
        double[] yValues = {};

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ArrayTabulatedFunction(xValues, yValues);
        });
        assertEquals("Length must be >=2", exception.getMessage());
    }

    @Test
    public void testAsPointsSinglePoint() {
        double[] xValues = {1.0, 2.0};
        double[] yValues = {2.0, 3.0};
        TabulatedFunction tabulatedFunction = new ArrayTabulatedFunction(xValues, yValues);

        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService();

        Point[] points = service.asPoints(tabulatedFunction);

        assertEquals(2, points.length);
        assertEquals(1.0, points[0].x);
        assertEquals(2.0, points[0].y);
        assertEquals(2.0, points[1].x);
        assertEquals(3.0, points[1].y);
    }

    double[] xValues = {1.0, 2.0, 3.0};
    double[] yValuesA = {2.0, 4.0, 6.0}; // Function: y = 2x
    double[] yValuesB = {1.0, 2.0, 3.0}; // Function: y = x

    private final TabulatedFunction arrayFunction = new ArrayTabulatedFunction(xValues, yValuesA);
    private final TabulatedFunction linkedListFunction = new LinkedListTabulatedFunction(xValues, yValuesB);

    private final TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(new ArrayTabulatedFunctionFactory());


    @Test
    public void testAdditionOperationWithArrayAndLinkedList() {
        TabulatedFunction result = service.additionOperation(arrayFunction, linkedListFunction);

        assertEquals(3.0, result.getY(0));
        assertEquals(6.0, result.getY(1));
        assertEquals(9.0, result.getY(2));
    }

    @Test
    public void testSubstractionOperationWithArrayAndLinkedList() {
        TabulatedFunction result = service.substractionOperation(arrayFunction, linkedListFunction);

        assertEquals(1.0, result.getY(0));
        assertEquals(2.0, result.getY(1));
        assertEquals(3.0, result.getY(2));
    }

    @Test
    public void testAdditionOperationWithLinkedListAndArray() {
        service.setFactory(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction result = service.additionOperation(linkedListFunction, arrayFunction);

        assertEquals(3.0, result.getY(0));
        assertEquals(6.0, result.getY(1));
        assertEquals(9.0, result.getY(2));
    }

    @Test
    public void testSubstractionOperationWithLinkedListAndArray() {
        service.setFactory(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction result = service.substractionOperation(linkedListFunction, arrayFunction);

        assertEquals(-1.0, result.getY(0));
        assertEquals(-2.0, result.getY(1));
        assertEquals(-3.0, result.getY(2));
    }

    @Test
    public void testAdditionOperationWithInconsistentFunctions() {
        double[] xValues = {1.0, 2.0, 4.0}; // Different xValues
        TabulatedFunction inconsistentFunction = new ArrayTabulatedFunction(xValues, yValuesA);

        assertThrows(InconsistentFunctionsException.class, () -> {
            service.additionOperation(arrayFunction, inconsistentFunction);
        });
    }

    @Test
    public void testSubstractionOperationWithInconsistentFunctions() {
        double[] xValues = {1.0, 2.0, 4.0}; // Different xValues
        TabulatedFunction inconsistentFunction = new ArrayTabulatedFunction(xValues, yValuesA);

        assertThrows(InconsistentFunctionsException.class, () -> {
            service.substractionOperation(arrayFunction, inconsistentFunction);
        });
    }

    @Test
    public void testGetFactory() {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        TabulatedFunctionOperationService service = new TabulatedFunctionOperationService(factory);

        assertEquals(factory, service.getFactory());
    }

    @Test
    public void testAdditionOperationWithDifferentCounts() {
        double[] xValuesA = {1.0, 2.0, 3.0};
        double[] yValuesA = {2.0, 4.0, 6.0}; // Function: y = 2x
        double[] xValuesB = {1.0, 2.0}; // Different count
        double[] yValuesB = {1.0, 2.0}; // Function: y = x

        TabulatedFunction functionA = new ArrayTabulatedFunction(xValuesA, yValuesA);
        TabulatedFunction functionB = new ArrayTabulatedFunction(xValuesB, yValuesB);

        assertThrows(InconsistentFunctionsException.class, () -> {
            service.additionOperation(functionA, functionB);
        });
    }

    @Test
    public void testSubstractionOperationWithDifferentCounts() {
        double[] xValuesA = {1.0, 2.0, 3.0};
        double[] yValuesA = {2.0, 4.0, 6.0}; // Function: y = 2x
        double[] xValuesB = {1.0, 2.0}; // Different count
        double[] yValuesB = {1.0, 2.0}; // Function: y = x

        TabulatedFunction functionA = new ArrayTabulatedFunction(xValuesA, yValuesA);
        TabulatedFunction functionB = new ArrayTabulatedFunction(xValuesB, yValuesB);

        assertThrows(InconsistentFunctionsException.class, () -> {
            service.substractionOperation(functionA, functionB);
        });

    }
    @Test
    public void testMultiplicationOperationWithLinkedListAndArray() {
        service.setFactory(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction result = service.multiplicationOperation(linkedListFunction, arrayFunction);

        assertEquals(2.0, result.getY(0));
        assertEquals(8.0, result.getY(1));
        assertEquals(18.0, result.getY(2));
    }

    @Test
    public void testDivisionOperationWithLinkedListAndArray() {
        service.setFactory(new LinkedListTabulatedFunctionFactory());
        TabulatedFunction result = service.divisionOperation(linkedListFunction, arrayFunction);

        assertEquals(0.5, result.getY(0));
        assertEquals(0.5, result.getY(1));
        assertEquals(0.5, result.getY(2));
    }

}
