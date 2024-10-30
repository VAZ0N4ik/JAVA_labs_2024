package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

public class NewtonMethodFunction implements MathFunction {
    private MathFunction func;
    private int x0;

    NewtonMethodFunction(MathFunction new_func, int x0) {
        func = new_func;
        this.x0 = x0;
    }

    private double EPSILON = 1e-6;

    @Override
    public double apply(double iterations) {
        double x = x0;
        for (int i = 0; i < iterations; ++i) {
            double der = (func.apply(x + EPSILON) - func.apply(x));
            x = x - func.apply(x) * EPSILON / der;
        }
        return x;
    }
}
