package ru.ssau.tk.java_domination_339.java_labs_2024.operations;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.MathFunction;

public class MiddleSteppingDifferentialOperator extends SteppingDifferentialOperator {
    public MiddleSteppingDifferentialOperator(double step) {
        super(step);
    }

    @Override
    public MathFunction derive(MathFunction function) {
        return x -> (function.apply(x + step) - function.apply(x - step)) / (2 * step);
    }

    @Override
    public double apply(double x) {
        throw new UnsupportedOperationException();
    }
}