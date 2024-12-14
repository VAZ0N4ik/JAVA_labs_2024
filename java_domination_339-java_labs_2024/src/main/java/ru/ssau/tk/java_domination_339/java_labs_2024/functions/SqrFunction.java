package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

@SimpleFunctionAnnotation(name = "Квадратичная функция", priority = 4)
public class SqrFunction implements MathFunction {
    @Override
    public double apply(double x) {
        return Math.pow(x, 2);
    }
}