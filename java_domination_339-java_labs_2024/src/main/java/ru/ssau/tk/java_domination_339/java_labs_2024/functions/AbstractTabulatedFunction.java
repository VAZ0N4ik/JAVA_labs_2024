package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import ru.ssau.tk.java_domination_339.java_labs_2024.exceptions.ArrayIsNotSortedException;
import ru.ssau.tk.java_domination_339.java_labs_2024.exceptions.DifferentLengthOfArraysException;

public abstract class AbstractTabulatedFunction implements TabulatedFunction {
    protected int count;

    protected abstract int floorIndexOfX(double x);

    protected abstract double extrapolateLeft(double x);

    protected abstract double extrapolateRight(double x);

    protected abstract double interpolate(double x, int floorIndex);

    protected double interpolate(double x, double leftX, double rightX, double leftY, double rightY) {
        return leftY + ((x - leftX) * (rightY - leftY) / (rightX - leftX));
    }

    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        } else if (x > rightBound()) {
            return extrapolateRight(x);
        } else {
            if (indexOfX(x) != -1) {
                return getY(indexOfX(x));
            } else {
                return interpolate(x, floorIndexOfX(x));
            }
        }
    }

    void checkLengthIsTheSame(double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length)
            throw new DifferentLengthOfArraysException();
    }

    void checkSorted(double[] xValues) {
        if (xValues.length > 1)
            for (int i = 1; i < xValues.length; ++i)
                if (xValues[i - 1] >= xValues[i])           // why does task require i > i-1 ??? for ex.: [1,1,1,1] is sorted; [1,2,3,3,3,4] is sorted too
                    throw new ArrayIsNotSortedException();
    }

    @Override
    public String toString() {
        StringBuilder strBuild = new StringBuilder(this.getClass().getSimpleName());
        strBuild.append(" size = ");
        strBuild.append(this.count);
        strBuild.append('\n');
        int c = 0;
        for (Point p: this){
            strBuild.append("[");
            strBuild.append(p.x);
            strBuild.append("; ");
            strBuild.append(p.y);
            strBuild.append("]");
            if (c < count - 1)
                strBuild.append("\n");
            c++;
        }
        return strBuild.toString();
    }
}
