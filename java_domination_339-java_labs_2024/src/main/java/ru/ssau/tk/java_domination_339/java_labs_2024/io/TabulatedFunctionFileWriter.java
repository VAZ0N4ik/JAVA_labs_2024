package ru.ssau.tk.java_domination_339.java_labs_2024.io;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.MathFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TabulatedFunctionFileWriter {
    public static void main(String[] args) {
        String arrayFilePath = "output/array function.txt";
        String linkedListFilePath = "output/linked list function.txt";

        MathFunction function = x -> x * x + 2 * x + 1;
        TabulatedFunction arrayFunction = new ArrayTabulatedFunction(function, -10, 10, 20);
        TabulatedFunction linkedListFunction = new LinkedListTabulatedFunction(function, -10, 10, 20);

        try (
                BufferedWriter arrayWriter = new BufferedWriter(new FileWriter(arrayFilePath));
                BufferedWriter linkedListWriter = new BufferedWriter(new FileWriter(linkedListFilePath))
        ) {
            FunctionsIO.writeTabulatedFunction(arrayWriter, arrayFunction);
            FunctionsIO.writeTabulatedFunction(linkedListWriter, linkedListFunction);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
