package ru.ssau.tk.java_domination_339.java_labs_2024.concurrent;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ConstantFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;

public class ReadWriteTaskExecutor {
    public static void main(String[] args) {
        TabulatedFunction func = new LinkedListTabulatedFunction(new ConstantFunction(-1), 1, 1000, 1000);
        Thread firstThread = new Thread(new ReadTask(func));
        Thread secondThread = new Thread(new WriteTask(func, 0.5));

        firstThread.start();
        secondThread.start();

    }
}
