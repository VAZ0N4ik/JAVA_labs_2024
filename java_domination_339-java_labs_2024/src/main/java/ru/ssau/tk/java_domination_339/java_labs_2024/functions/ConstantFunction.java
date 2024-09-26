package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

public class ConstantFunction implements MathFunction {
    private final double constant;

    public ConstantFunction(double cnst) {
        this.constant = cnst;
    }

    @Override
    public double apply(double x) {
        return constant;
    }

    // getter
    public double getConstant() {
        return constant;
    }
}