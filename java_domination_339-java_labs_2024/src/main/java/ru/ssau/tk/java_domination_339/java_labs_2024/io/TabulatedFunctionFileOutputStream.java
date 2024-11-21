package ru.ssau.tk.java_domination_339.java_labs_2024.io;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.LinkedListTabulatedFunction;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TabulatedFunctionFileOutputStream {
    public static void main(String[] args) {
        try (
                BufferedOutputStream bufOutArr = new BufferedOutputStream(new FileOutputStream("output/array function.bin"));
                BufferedOutputStream bufOutLinkedList = new BufferedOutputStream(new FileOutputStream("output/linked list function.bin"))
        ) {
            ArrayTabulatedFunction arrFunc = new ArrayTabulatedFunction(new double[]{1, 2, 3}, new double[]{1, 4, 9});
            LinkedListTabulatedFunction linkedListFunc = new LinkedListTabulatedFunction(new double[]{1, 2, 3}, new double[]{1, 4, 9});

            FunctionsIO.writeTabulatedFunction(bufOutArr, arrFunc);
            FunctionsIO.writeTabulatedFunction(bufOutLinkedList, linkedListFunc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
