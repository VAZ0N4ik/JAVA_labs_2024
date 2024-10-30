package ru.ssau.tk.java_domination_339.java_labs_2024.concurrent;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.UnitFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MultiplyingTaskExecutor {
    public static void main(String[] args) {
        TabulatedFunction function = new LinkedListTabulatedFunction(new UnitFunction(), 1, 1000, 1000);

        List<Thread> threads = new ArrayList<>();
        ConcurrentHashMap<Integer, MultiplyingTask> taskMap = new ConcurrentHashMap<>();

        for (int i = 0; i < 10; ++i) {
            MultiplyingTask task = new MultiplyingTask(function);
            threads.add(new Thread(task));
            taskMap.put(i, task);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        while (!taskMap.isEmpty()) {
            taskMap.values().removeIf(MultiplyingTask::isFinished); // removeIf() is safe unlike iterator
        }

        for (int i = 0; i < function.getCount(); i++) {
            System.out.println("x: " + function.getX(i) + ", y: " + function.getY(i));
        }
    }
}
