package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import java.util.Iterator;
public class StrictTabulatedFunction implements TabulatedFunction{
    TabulatedFunction func;

    public StrictTabulatedFunction(TabulatedFunction function) {
        this.func = function;
    }

    @Override
    public Iterator<Point> iterator() {
        return func.iterator();
    }

    @Override
    public int getCount() {
        return func.getCount();
    }

    @Override
    public double getX(int index) {
        return func.getX(index);
    }

    @Override
    public double getY(int index) {
        return func.getY(index);
    }

    @Override
    public void setY(int index, double value) {
        func.setY(index, value);
    }

    @Override
    public int indexOfX(double x) {
        return func.indexOfX(x);
    }

    @Override
    public int indexOfY(double y) {
        return func.indexOfY(y);
    }

    @Override
    public double leftBound() {
        return func.leftBound();
    }

    @Override
    public double rightBound() {
        return func.rightBound();
    }

    @Override
    public double apply(double x) throws UnsupportedOperationException {
        int index = indexOfX(x);

        if (index == -1) {
            throw new UnsupportedOperationException("Not allowed to interpolate y for passed x");
        } else {
            return getY(index);
        }
    }
}
