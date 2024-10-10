package ru.ssau.tk.java_domination_339.java_labs_2024.io;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.LinkedListTabulatedFunctionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TabulatedFunctionFileReader {
    public static void main(String[] args) {
        try (BufferedReader reader1 = new BufferedReader(new FileReader("input/function.txt"));
             BufferedReader reader2 = new BufferedReader(new FileReader("input/function.txt"))) {

            TabulatedFunction arrayFunction = FunctionsIO.readTabulatedFunction(reader1, new ArrayTabulatedFunctionFactory());
            TabulatedFunction linkedFunction = FunctionsIO.readTabulatedFunction(reader2, new LinkedListTabulatedFunctionFactory());

            System.out.println(arrayFunction.toString());
            System.out.println(linkedFunction.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
