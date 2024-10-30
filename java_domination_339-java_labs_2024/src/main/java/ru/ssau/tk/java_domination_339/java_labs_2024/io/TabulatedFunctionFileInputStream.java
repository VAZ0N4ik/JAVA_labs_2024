package ru.ssau.tk.java_domination_339.java_labs_2024.io;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.operations.TabulatedDifferentialOperator;

import java.io.*;

public class TabulatedFunctionFileInputStream {
    public static void main(String[] args) {
        try (
                BufferedInputStream bufOutArr = new BufferedInputStream(new FileInputStream("input/binary function.bin"));

        ) {
            TabulatedFunction arrFunc = FunctionsIO.readTabulatedFunction(bufOutArr, new ArrayTabulatedFunctionFactory());
            System.out.println(arrFunc.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Введите размер и значения функции");
            TabulatedFunction ll = FunctionsIO.readTabulatedFunction(buffReader, new LinkedListTabulatedFunctionFactory());
            TabulatedDifferentialOperator diffOperator = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());
            TabulatedFunction llDerivative = diffOperator.derive(ll);
            System.out.println(llDerivative.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
