package ru.ssau.tk.java_domination_339.java_labs_2024.operations;

import ru.ssau.tk.java_domination_339.java_labs_2024.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.Point;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.TabulatedFunctionFactory;

public class TabulatedFunctionOperationService {
    private TabulatedFunctionFactory factory;

    public TabulatedFunctionOperationService(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public TabulatedFunctionOperationService() {
        this.factory = new ArrayTabulatedFunctionFactory();
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public static Point[] asPoints(TabulatedFunction tabulatedFunction) {
        Point[] points = new Point[tabulatedFunction.getCount()];
        int i = 0;

        for (Point point : tabulatedFunction) {
            points[i] = point;
            ++i;
        }

        return points;
    }

    private interface BiOperation {
        double apply(double u, double v);
    }

    private TabulatedFunction doOperation(TabulatedFunction a, TabulatedFunction b, BiOperation operation) {
        if (a.getCount() != b.getCount())
            throw new InconsistentFunctionsException();

        Point[] pointsA = TabulatedFunctionOperationService.asPoints(a);
        Point[] pointsB = TabulatedFunctionOperationService.asPoints(b);

        int size = a.getCount();
        double[] xValues = new double[size];
        double[] yValues = new double[size];

        for (int i = 0; i < size; ++i) {
            if (pointsA[i].x != pointsB[i].x)
                throw new InconsistentFunctionsException();

            xValues[i] = pointsA[i].x;
            yValues[i] = operation.apply(pointsA[i].y, pointsB[i].y);
        }

        return factory.create(xValues, yValues);
    }

    public TabulatedFunction additionOperation(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (u, v) -> u + v);
    }

    public TabulatedFunction substractionOperation(TabulatedFunction a, TabulatedFunction b) {
        return doOperation(a, b, (u, v) -> u - v);
    }

}
