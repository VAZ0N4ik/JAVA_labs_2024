package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

public class ChordMethodFunction implements MathFunction {
    private double x0;        // bottom border
    private double x1;        // top border
    private final MathFunction function;

    ChordMethodFunction(double x0, double x1, MathFunction func) {
        this.x0 = x0;
        this.x1 = x1;
        this.function = func;
    }

    @Override
    public double apply(double eps) {
        double temp;

        while (Math.abs(x0 - x1) > eps) {
            temp = x1;
            x1 = x0;
            x0 -= function.apply(x1) * (x1 - temp) / (function.apply(x1) - function.apply(temp));
        }

        return x0;
    }

}