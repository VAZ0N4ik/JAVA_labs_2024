package ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.enums.TabulatedFunctionFactoryType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDto {
    private TabulatedFunctionFactoryType factoryType;
}