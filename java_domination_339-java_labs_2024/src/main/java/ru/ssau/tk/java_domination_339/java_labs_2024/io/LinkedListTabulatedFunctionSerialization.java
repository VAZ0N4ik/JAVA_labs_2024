package ru.ssau.tk.java_domination_339.java_labs_2024.io;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.SqrFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.operations.TabulatedDifferentialOperator;

import java.io.*;

public class LinkedListTabulatedFunctionSerialization {
    public static void main(String[] args) {
        try (
                BufferedOutputStream bufOut = new BufferedOutputStream(new FileOutputStream("output/serialized linked list functions.bin"));
        ) {
            TabulatedFunction ll = new LinkedListTabulatedFunction(new SqrFunction(), 1, 3, 3);
            TabulatedDifferentialOperator der = new TabulatedDifferentialOperator(new LinkedListTabulatedFunctionFactory());
            TabulatedFunction firstDer = der.derive(ll);
            TabulatedFunction secondDer = der.derive(firstDer);
            FunctionsIO.serialize(bufOut, ll);
            FunctionsIO.serialize(bufOut, firstDer);
            FunctionsIO.serialize(bufOut, secondDer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedInputStream bufIN = new BufferedInputStream(new FileInputStream("output/serialized linked list functions.bin"))) {

            TabulatedFunction ll = FunctionsIO.deserialize(bufIN);
            ;
            TabulatedFunction firstDer = FunctionsIO.deserialize(bufIN);
            ;
            TabulatedFunction secondDer = FunctionsIO.deserialize(bufIN);

            System.out.println(ll.toString());
            System.out.println(firstDer.toString());
            System.out.println(secondDer.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
