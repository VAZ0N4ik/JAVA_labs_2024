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
        double x_next = 0;
        double temp;
        do{
            temp = x_next;
            x_next = x1 - function.apply(x1) * (x0 - x1) / (function.apply(x0 - eps) - function.apply(x1 + eps));
            x0 = x1;
            x1 = temp;
        } while (Math.abs(x_next - x1) > eps);
        return x_next;
    }

}