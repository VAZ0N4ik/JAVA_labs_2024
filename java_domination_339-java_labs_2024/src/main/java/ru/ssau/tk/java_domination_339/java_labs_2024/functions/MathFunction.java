package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

public interface MathFunction {
    double apply(double x);

    default MathFunction andThen(MathFunction afterFunction) {
        return new CompositeFunction(this, afterFunction);
    }
}