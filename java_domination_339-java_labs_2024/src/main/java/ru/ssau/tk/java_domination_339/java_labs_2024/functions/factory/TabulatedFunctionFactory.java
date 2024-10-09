package ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.StrictTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.UnmodifiableTabulatedFunction;

public interface TabulatedFunctionFactory {
    TabulatedFunction create (double[] xValues, double[] yValues);

    default TabulatedFunction createUnmodifiable(double[] xValues, double[] yValues) {
        return new UnmodifiableTabulatedFunction(create(xValues, yValues));
  
    default TabulatedFunction createStrict (double[] xValues, double[] yValues){
        return new StrictTabulatedFunction(create(xValues,yValues));
    }

    default TabulatedFunction createStrictUnmodifiable (double[] xValues, double[] yValues){
        return new StrictTabulatedFunction(new UnmodifiableTabulatedFunction(create(xValues,yValues)));
    }
}
