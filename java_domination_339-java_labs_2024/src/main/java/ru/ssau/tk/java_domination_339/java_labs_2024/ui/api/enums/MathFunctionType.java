package ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.enums;

import lombok.Getter;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.*;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum MathFunctionType {
    IDENTITY_FUNCTION("Тождественная функция", new IdentityFunction()),
    SQR_FUNCTION("Квадратичная функция", new SqrFunction()),
    UNIT_FUNCTION("Единичная функция", new UnitFunction()),
    ZERO_FUNCTION("Нулевая функция", new ZeroFunction()),
    ;

    private final String localizedName;
    private final MathFunction function;

    MathFunctionType(String localizedName, MathFunction function) {
        this.localizedName = localizedName;
        this.function = function;
    }

    public static Map<String, MathFunction> getLocalizedFunctionMap() {
        Map<String, MathFunction> map = new HashMap<>();
        for (MathFunctionType type : values()) {
           map.put(type.getLocalizedName(), type.getFunction());
        }

        return map;
    }
}