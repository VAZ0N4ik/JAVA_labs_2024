package ru.ssau.tk.java_domination_339.java_labs_2024.functions;

import java.util.Arrays;

public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable{
    protected double[] xValues;
    protected double[] yValues;

    public ArrayTabulatedFunction(double[] xValues, double[] yValues) {
        this.xValues = Arrays.copyOf(xValues, xValues.length);
        this.yValues = Arrays.copyOf(yValues, yValues.length);
        this.count = xValues.length;
    }

    public ArrayTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {

        xValues = new double[count];
        yValues = new double[count];
        this.count = count;

        if (xFrom > xTo) {
            double temp = xFrom;
            xFrom = xTo;
            xTo = temp;
        }

        if (xTo - xFrom < 1e-9) {
            double yValue = source.apply(xFrom);
            for (int i = 0; i < count; ++i) {
                xValues[i] = xFrom;
                yValues[i] = yValue;
            }
        }

        else {
            double step = (xTo - xFrom) / (count - 1);
            for (int i = 0; i < count; ++i) {
                xValues[i] = xFrom + i * step;
                yValues[i] = source.apply(xValues[i]);
            }
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public double getX(int index) {
        return xValues[index];
    }

    @Override
    public double getY(int index) {
        return yValues[index];
    }

    @Override
    public void setY(int index, double value) {
        yValues[index] = value;
    }

    @Override
    public double leftBound() {
        return xValues[0];
    }

    @Override
    public double rightBound() {
        return xValues[count - 1];
    }

    @Override
    public int indexOfX(double x) {
        for (int i = 0; i < count; i++) {
            if (Math.abs(xValues[i] - x) < 1e-9) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        for (int i = 0; i < count; i++) {
            if (Math.abs(yValues[i] - y) < 1e-9) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected int floorIndexOfX(double x) {
        if (x < xValues[0]) {
            return 0;
        }
        for (int i = 1; i < count; i++) {
            if (x < xValues[i]) {
                return i - 1;
            }
        }
        return count - 1;
    }

    @Override
    protected double extrapolateLeft(double x) {
        if (count == 1) {
            return yValues[0];
        }
        return interpolate(x, xValues[0], xValues[1], yValues[0], yValues[1]);
    }

    @Override
    protected double extrapolateRight(double x) {
        if (count == 1) {
            return yValues[0];
        }
        return interpolate(x, xValues[count - 2], xValues[count - 1], yValues[count - 2], yValues[count - 1]);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        if (count == 1) {
            return yValues[0];
        }
        return interpolate(x, xValues[floorIndex], xValues[floorIndex + 1], yValues[floorIndex], yValues[floorIndex + 1]);
    }

    @Override
    public void insert(double x, double y){
        if (indexOfX(x) != -1){
            setY(indexOfX(x),y);
        }
        else{

            double[] xBuffer = new double[count+1];
            double[] yBuffer = new double[count+1];
            if (count == 0){
                xBuffer[0] = x;
                yBuffer[0] = y;
            }
            else if (x < leftBound()){
                xBuffer[0] = x;
                yBuffer[0] = y;
                System.arraycopy(xValues,0,xBuffer,1, count);
                System.arraycopy(yValues,0,yBuffer,1, count);
            }
            else if (x > rightBound()){
                xBuffer[xBuffer.length - 1] = x;
                yBuffer[yBuffer.length - 1] = y;
                System.arraycopy(xValues,0,xBuffer,0, count);
                System.arraycopy(yValues,0,yBuffer,0, count);
            }
            else {
                int index = floorIndexOfX(x);
                System.arraycopy(xValues,0,xBuffer, 0, index +1);
                System.arraycopy(yValues,0,yBuffer, 0, index +1);
                System.arraycopy(xValues,index+1,xBuffer, index+2, count - index - 1);
                System.arraycopy(yValues,index+1,yBuffer, index+2, count - index - 1);
                xBuffer[index+1] = x;
                yBuffer[index+1] = y;
            }
            xValues = xBuffer;
            yValues = yBuffer;
            count++;
        }
    }

    @Override
    public void remove(int index) {
        if (index >= 0 && index < count){
            double[] newXValues = new double[count - 1];
            double[] newYValues = new double[count - 1];

            System.arraycopy(xValues, 0, newXValues, 0, index);
            System.arraycopy(yValues, 0, newYValues, 0, index);
            System.arraycopy(xValues, index + 1, newXValues, index, count - index - 1);
            System.arraycopy(yValues, index + 1, newYValues, index, count - index - 1);

            xValues = newXValues;
            yValues = newYValues;
            count--;
        }
    }
}