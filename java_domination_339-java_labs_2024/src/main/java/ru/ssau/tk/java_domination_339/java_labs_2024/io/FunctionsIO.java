package ru.ssau.tk.java_domination_339.java_labs_2024.io;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.Point;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.TabulatedFunctionFactory;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public final class FunctionsIO {
    private FunctionsIO() {
        throw new UnsupportedOperationException();
    }

    public static void writeTabulatedFunction(BufferedWriter writer, TabulatedFunction function) {
        PrintWriter printWriter = new PrintWriter(writer);

        printWriter.println(function.getCount());

        for (Point point : function) {
            printWriter.printf("%f %f\n", point.x, point.y);
        }

        printWriter.flush();
    }

    public static void writeTabulatedFunction(BufferedOutputStream outputStream, TabulatedFunction function) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(outputStream);
        dataOut.writeInt(function.getCount());

        for (Point p : function) {
            dataOut.writeDouble(p.x);
            dataOut.writeDouble(p.y);
        }

        dataOut.flush();
    }
    public static TabulatedFunction readTabulatedFunction(BufferedReader reader, TabulatedFunctionFactory factory) throws IOException {
        int count = Integer.parseInt(reader.readLine());
        double[] xValues = new double[count];
        double[] yValues = new double[count];

        NumberFormat numberFormatter = NumberFormat.getInstance(Locale.forLanguageTag("ru"));

        for (int i = 0; i < count; ++i) {
            String line = reader.readLine();
            String[] values = line.split(" ");
            try {
                xValues[i] = numberFormatter.parse(values[0]).doubleValue();
                yValues[i] = numberFormatter.parse(values[1]).doubleValue();
            } catch (ParseException e) {
                throw new IOException(e);
            }
        }

        return factory.create(xValues, yValues);
    }

}
