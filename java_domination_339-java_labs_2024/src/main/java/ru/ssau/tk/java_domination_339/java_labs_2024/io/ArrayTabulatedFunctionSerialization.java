package ru.ssau.tk.java_domination_339.java_labs_2024.io;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.operations.TabulatedDifferentialOperator;

import java.io.*;

public class ArrayTabulatedFunctionSerialization {
    public static void main(String[] args) {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("output/serialized array functions.bin"))) {
            double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0};
            double[] yValues = {2.0, 8.0, 18.0, 32.0, 50.0};

            TabulatedFunction function1 = new ArrayTabulatedFunction(xValues, yValues);
            TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator(new ArrayTabulatedFunctionFactory());
            TabulatedFunction function2 = operator.derive(function1);
            TabulatedFunction function3 = operator.derive(function2);

            FunctionsIO.serialize(outputStream, function1);
            FunctionsIO.serialize(outputStream, function2);
            FunctionsIO.serialize(outputStream, function3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream("output/serialized array functions.bin"))) {
            TabulatedFunction deserializedFunction1 = FunctionsIO.deserialize(inputStream);
            TabulatedFunction deserializedFunction2 = FunctionsIO.deserialize(inputStream);
            TabulatedFunction deserializedFunction3 = FunctionsIO.deserialize(inputStream);

            System.out.println(deserializedFunction1.toString());
            System.out.println(deserializedFunction2.toString());
            System.out.println(deserializedFunction3.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
