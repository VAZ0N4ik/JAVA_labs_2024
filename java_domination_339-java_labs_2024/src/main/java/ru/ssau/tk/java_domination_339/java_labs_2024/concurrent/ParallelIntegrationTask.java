package ru.ssau.tk.java_domination_339.java_labs_2024.concurrent;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;

import java.util.concurrent.Callable;

public class ParallelIntegrationTask implements Callable<Double> {
    private final TabulatedFunction function;
    private final double start;
    private final double end;
    private final int intervals;

    public ParallelIntegrationTask(TabulatedFunction function, double start, double end, int intervals) {
        this.function = function;
        this.start = start;
        this.end = end;
        this.intervals = intervals;
    }

    @Override
    public Double call() {
        if (intervals % 2 != 0) 
            throw new IllegalArgumentException("Number of intervals must be even.");

        double totalArea = 0.0;
        double h = (end - start) / intervals;
        double x = start;

        totalArea += function.apply(x); // f(x0)

        for (int i = 1; i < intervals; i += 2) {
            x += h;
            totalArea += 4 * function.apply(x); // f(xi) * 4
        }

        for (int i = 2; i < intervals; i += 2) {
            x += h;
            totalArea += 2 * function.apply(x); // f(xi) * 2
        }

        totalArea += function.apply(end); // f(xn)

        totalArea *= (h / 3);

        return totalArea;
    }
}