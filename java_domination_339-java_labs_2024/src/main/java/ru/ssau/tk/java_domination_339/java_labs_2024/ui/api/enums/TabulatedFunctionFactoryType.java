package ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.enums;

import lombok.Getter;

@Getter
public enum TabulatedFunctionFactoryType {
    ARRAY_FACTORY("Массив"),
    LINKED_LIST_FACTORY("Связный список");

    private final String localizedName;

    TabulatedFunctionFactoryType(String localizedName) {
        this.localizedName = localizedName;
    }

}