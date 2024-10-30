package ru.ssau.tk.java_domination_339.java_labs_2024.operations;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.MathFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.concurrent.ParallelIntegrationTask;

import java.util.concurrent.*;

public class TabulatedIntegrationOperator implements MathFunction {

    final int numThreads;

    public TabulatedIntegrationOperator(int numThreads) {
        this.numThreads = numThreads;
    }

    public TabulatedIntegrationOperator() {
        this.numThreads = Runtime.getRuntime().availableProcessors() - 1;

    }

    public double integrate(TabulatedFunction function) {
        double start = function.leftBound();
        double end = function.rightBound();
        int intervals = 10000;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        double totalArea = 0.0;
        double segmentLength = (end - start) / numThreads;
        Future<Double>[] futures = new Future[numThreads];

        double segmentStart = start;
        for (int i = 0; i < numThreads; i++) {
            segmentStart += segmentLength;
            double segmentEnd = (i == numThreads - 1) ? end : segmentStart + segmentLength;
            futures[i] = executor.submit(new ParallelIntegrationTask(function, segmentStart, segmentEnd, intervals / numThreads));
        }

        for (Future<Double> future : futures) {
            try {
                totalArea += future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return totalArea;
    }

    @Override
    public double apply(double x) {
        throw new UnsupportedOperationException();
    }
}
