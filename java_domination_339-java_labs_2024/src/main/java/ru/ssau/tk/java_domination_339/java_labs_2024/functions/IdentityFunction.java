package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

@SimpleFunctionAnnotation(name = "Тождественная функция", priority = 3)
public class IdentityFunction implements MathFunction {
    @Override
    public double apply(double x) {
        return x;
    }
}
