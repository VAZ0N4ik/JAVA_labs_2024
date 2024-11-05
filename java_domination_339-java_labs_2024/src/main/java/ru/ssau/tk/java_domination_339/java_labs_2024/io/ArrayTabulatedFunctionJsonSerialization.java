package ru.ssau.tk.java_domination_339.java_labs_2024.io;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.io.FunctionsIO;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.operations.TabulatedDifferentialOperator;

import java.io.*;

public class ArrayTabulatedFunctionJsonSerialization {
    public static void main(String[] args) throws FileNotFoundException {
        try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter("output/serialized array functions.json"))) {
            double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0};
            double[] yValues = {2.0, 8.0, 18.0, 32.0, 50.0};
            ArrayTabulatedFunction function1 = new ArrayTabulatedFunction(xValues, yValues);

            FunctionsIO.serializeJson(bufWriter, function1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader bufReader = new BufferedReader(new FileReader("output/serialized array functions.json"))) {
            TabulatedFunction deserializedFunction1 = FunctionsIO.deserializeJson(bufReader);
            System.out.println(deserializedFunction1.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
