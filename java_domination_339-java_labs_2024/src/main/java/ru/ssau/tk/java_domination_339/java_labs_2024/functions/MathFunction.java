package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

public interface MathFunction {
    double apply(double x);
    default String Name(){
        return this.getClass().getSimpleName();
    }
    default int HashName() {
        return this.Name().hashCode();
    }
    default MathFunction andThen(MathFunction afterFunction) {
        return new CompositeFunction(this, afterFunction);
    }
}