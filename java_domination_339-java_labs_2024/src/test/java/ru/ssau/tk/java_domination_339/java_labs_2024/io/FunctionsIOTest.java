package ru.ssau.tk.java_domination_339.java_labs_2024.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.LinkedListTabulatedFunctionFactory;

import java.io.*;
import java.util.Objects;


import static org.junit.Assert.assertEquals;

public class FunctionsIOTest {
    private static final String TEMP_DIR = "temp";
    private File tempDir;

    @Before
    public void setUp() throws IOException {
        tempDir = new File(TEMP_DIR);
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
    }

    @After
    public void tearDown() throws IOException {
        for (File file : Objects.requireNonNull(tempDir.listFiles())) {
            file.delete();
        }
    }

    @Test
    public void testWriteReadTabulatedFunction() throws IOException {
        LinkedListTabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        TabulatedFunction function = factory.create(new double[]{1.0, 2.0, 3.0}, new double[]{10.0, 20.0, 30.0});

        File file = new File(tempDir, "testFunction.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            FunctionsIO.writeTabulatedFunction(writer, function);
        }

        TabulatedFunction readFunction;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            readFunction = FunctionsIO.readTabulatedFunction(reader, factory);
        }

        assertEquals(function.getCount(), readFunction.getCount());
        for (int i = 0; i < function.getCount(); i++) {
            assertEquals(function.getX(i), readFunction.getX(i), 0.001);
            assertEquals(function.getY(i), readFunction.getY(i), 0.001);
        }
    }

    @Test
    public void testWriteReadTabulatedFunctionBinary() throws IOException {
        LinkedListTabulatedFunctionFactory factory = new LinkedListTabulatedFunctionFactory();
        TabulatedFunction function = factory.create(new double[]{1.0, 2.0, 3.0}, new double[]{10.0, 20.0, 30.0});

        File file = new File(tempDir, "testFunction.bin");
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            FunctionsIO.writeTabulatedFunction(outputStream, function);
        }

        TabulatedFunction readFunction;
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            readFunction = FunctionsIO.readTabulatedFunction(inputStream, factory);
        }

        assertEquals(function.getCount(), readFunction.getCount());
        for (int i = 0; i < function.getCount(); i++) {
            assertEquals(function.getX(i), readFunction.getX(i), 0.001);
            assertEquals(function.getY(i), readFunction.getY(i), 0.001);
        }
    }

    @Test
    public void testSerializeDeserializeArrayTabulatedFunction() throws IOException, ClassNotFoundException {
        double[] xValues = {1.0, 2.0, 3.0};
        double[] yValues = {1.0, 4.0, 9.0};

        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("output/serialized array functions.bin"));
        TabulatedFunction function = new ArrayTabulatedFunction(xValues, yValues);

        FunctionsIO.serialize(outputStream, function);

        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream("output/serialized array functions.bin"));
        TabulatedFunction deserializedFunction = FunctionsIO.deserialize(inputStream);


        assertEquals(function.getCount(), deserializedFunction.getCount());
        for (int i = 0; i < function.getCount(); i++) {
            assertEquals(function.getX(i), deserializedFunction.getX(i),0.001);
            assertEquals(function.getY(i), deserializedFunction.getY(i), 0.001);
        }
    }
}
