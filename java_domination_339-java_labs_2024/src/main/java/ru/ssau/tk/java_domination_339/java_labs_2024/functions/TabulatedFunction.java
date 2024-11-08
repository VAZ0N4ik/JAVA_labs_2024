package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

public interface TabulatedFunction extends MathFunction, Iterable<Point> {


    int getCount();

    double getX(int index);

    double getY(int index);

    void setY(int index, double value);

    int indexOfX(double x);

    int indexOfY(double x);

    double leftBound();

    double rightBound();
}
