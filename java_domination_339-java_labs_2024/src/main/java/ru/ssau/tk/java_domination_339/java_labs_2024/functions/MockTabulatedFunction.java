package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import java.util.Iterator;

import static java.lang.Math.abs;

public class MockTabulatedFunction extends AbstractTabulatedFunction {
    private final double x0;
    private final double x1;
    private double y0;
    private double y1;

    public MockTabulatedFunction(double x0, double x1, double y0, double y1) {
        if (x0 <= x1) {
            this.x0 = x0;
            this.x1 = x1;
            this.y0 = y0;
            this.y1 = y1;
        } else {
            this.x0 = x1;
            this.x1 = x0;
            this.y0 = y1;
            this.y1 = y0;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public double leftBound() {
        return x0;
    }

    @Override
    public double rightBound() {
        return x1;
    }


    @Override
    public double getX(int index) {
        if (index == 0) {
            return x0;
        }
        if (index == 1) {
            return x1;
        }
        return -1;
    }

    @Override
    public double getY(int index) {
        if (index == 0) {
            return y0;
        }
        if (index == 1) {
            return y1;
        }
        return -1;
    }

    @Override
    public void setY(int index, double value) {
        if (index == 0) {
            y0 = value;
        }
        if (index == 1) {
            y1 = value;
        }
    }

    @Override
    public int indexOfX(double x) {
        if (x == x0) {
            return 0;
        }
        if (x == x1) {
            return 1;
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        if (y == y0) {
            return 0;
        }
        if (y == y1) {
            return 1;
        }
        return -1;
    }

    @Override
    protected int floorIndexOfX(double x) {
        if (x < x1)
            return 0;
        if (x == x1)
            return 1;
        return 2;
    }

    @Override
    protected double extrapolateLeft(double x) {

        return interpolate(x, leftBound(), rightBound(), getY(0), getY(1));
    }

    @Override
    protected double interpolate(double x, int floorIndex) {

        return interpolate(x, getX(floorIndexOfX(floorIndex)), getX(floorIndexOfX(floorIndex) + 1), getY(floorIndexOfX(floorIndex)), getY(floorIndexOfX(floorIndex) + 1));
    }

    @Override
    protected double extrapolateRight(double x) {

        return interpolate(x, leftBound(), rightBound(), getY(0), getY(1));
    }


    @Override
    public Iterator<Point> iterator() {
        return null;
    }
}
