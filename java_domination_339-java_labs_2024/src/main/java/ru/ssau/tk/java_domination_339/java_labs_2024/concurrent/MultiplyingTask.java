package ru.ssau.tk.java_domination_339.java_labs_2024.concurrent;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;

public class MultiplyingTask implements Runnable {
    private final TabulatedFunction function;

    public MultiplyingTask(TabulatedFunction function) {
        this.function = function;
    }

    public void run() {
        for (int i = 0; i < function.getCount(); ++i) {
            synchronized (function) {
                function.setY(i, function.getY(i) * 2);
            }
        }

        System.out.println("Thread \"" + Thread.currentThread().getName() + "\" finished the task");
    }
}
