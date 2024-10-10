package ru.ssau.tk.java_domination_339.java_labs_2024.io;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.ArrayTabulatedFunctionFactory;

import java.io.*;

public class ArrayTabulatedFunctionSerializationXml {
    public static void main(String[] args) {
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yValues = {2.0, 8.0, 18.0, 32.0, 50.0};

        ArrayTabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output/serialized array functions.xml"))) {
            FunctionsIO.serializeXml(writer, function);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader("output/serialized array functions.xml"))) {
            ArrayTabulatedFunction deserializedFunction = FunctionsIO.deserializeXml(reader);
            System.out.println(deserializedFunction.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
