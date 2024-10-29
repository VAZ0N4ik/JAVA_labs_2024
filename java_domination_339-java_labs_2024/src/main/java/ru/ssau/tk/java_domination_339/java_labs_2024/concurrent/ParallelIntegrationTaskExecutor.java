package ru.ssau.tk.java_domination_339.java_labs_2024.concurrent;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.operations.TabulatedIntegrationOperator;

public class ParallelIntegrationTaskExecutor {
    public static void main(String[] args) {
        TabulatedFunctionFactory factory = new ArrayTabulatedFunctionFactory();
        double[] xValues = {0.0, 1.0, 2.0, 3.0};
        double[] yValues = {0.0, 1.0, 4.0, 9.0};
        TabulatedFunction function = factory.create(xValues, yValues);

        TabulatedIntegrationOperator integrationOperator = new TabulatedIntegrationOperator();
        double result = integrationOperator.integrate(function);

        System.out.println("Интеграл от функции: " + result);
    }

}
