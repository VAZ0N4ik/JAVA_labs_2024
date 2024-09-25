package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

public class CompositeFunction implements MathFunction{
    private MathFunction firstFunction;
    private MathFunction secondFunction;

    CompositeFunction(MathFunction first, MathFunction second){
        firstFunction =first;
        secondFunction =second;
    }

    public double apply(double x){
        return (secondFunction.apply(firstFunction.apply(x)));
    }
}
