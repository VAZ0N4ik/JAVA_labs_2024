package ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.LinkedListTabulatedFunction;
public class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory{
    @Override
    public TabulatedFunction create(double[] xValues, double[] yValues){
        return new LinkedListTabulatedFunction(xValues,yValues);
    }
}
