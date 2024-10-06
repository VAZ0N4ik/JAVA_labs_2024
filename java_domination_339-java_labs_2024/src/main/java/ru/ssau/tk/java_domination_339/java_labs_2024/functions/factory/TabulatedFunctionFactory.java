package ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;

public interface TabulatedFunctionFactory {
    TabulatedFunction create (double[] xValues, double[] yValues);

}
