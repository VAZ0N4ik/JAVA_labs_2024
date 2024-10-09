package ru.ssau.tk.java_domination_339.java_labs_2024.operations;

import ru.ssau.tk.java_domination_339.java_labs_2024.functions.MathFunction;

public abstract class SteppingDifferentialOperator implements DifferentialOperator<MathFunction> {
    protected double step;

    private void checkStep(double step) {
        if (step <= 0 || step == Double.POSITIVE_INFINITY || Double.isNaN(step))
            throw new IllegalArgumentException();
    }

    public SteppingDifferentialOperator(double step) {
        this.step = step;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        checkStep(step);
        this.step = step;
    }

}