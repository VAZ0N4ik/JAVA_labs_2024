package ru.ssau.tk.java_domination_339.java_labs_2024.exceptions;

public class ArrayIsNotSortedException extends RuntimeException {
    public ArrayIsNotSortedException() {}

    public ArrayIsNotSortedException(String msg) {
        super(msg);
    }
}