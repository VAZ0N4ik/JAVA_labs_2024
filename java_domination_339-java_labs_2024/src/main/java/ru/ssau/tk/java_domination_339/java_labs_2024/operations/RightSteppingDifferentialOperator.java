package ru.ssau.tk.java_domination_339.java_labs_2024.operations;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.MathFunction;

public class RightSteppingDifferentialOperator extends SteppingDifferentialOperator {
    public RightSteppingDifferentialOperator(double step) {
        super(step);
    }

    @Override
    public MathFunction derive(MathFunction function) {
        return x -> (function.apply(x + step) - function.apply(x)) / step;
    }

    @Override
    public double apply(double x) {
        throw new UnsupportedOperationException();
    }
}