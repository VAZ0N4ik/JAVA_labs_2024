package ru.ssau.tk.java_domination_339.java_labs_2024.operations;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.Point;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;

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
}
