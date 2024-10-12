package ru.ssau.tk.java_domination_339.java_labs_2024.concurrent;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;

public class ReadTask implements Runnable{
    private TabulatedFunction func;
    public ReadTask(TabulatedFunction tempFunc){
        func = tempFunc;
    }

    @Override
    public void run() {
        for (int i = 0; i<func.getCount();++i){
            System.out.printf("After read: i = %d, x = %f, y = %f\n",i,func.getX(i),func.getY(i));
        }
    }
}
