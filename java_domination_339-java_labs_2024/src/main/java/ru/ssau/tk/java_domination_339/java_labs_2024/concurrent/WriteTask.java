package ru.ssau.tk.java_domination_339.java_labs_2024.concurrent;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ConstantFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;

public class WriteTask implements Runnable{
    private final TabulatedFunction func;
    private final double value;
    public WriteTask(TabulatedFunction f, double v){
        func = f;
        value = v;
    }

    @Override
    public void run() {
        for (int i = 0; i < func.getCount(); i++) {
            synchronized (func) {
                func.setY(i, value);
                System.out.printf("Writing for index %d complete\n", i);
            }
        }
    }


}
