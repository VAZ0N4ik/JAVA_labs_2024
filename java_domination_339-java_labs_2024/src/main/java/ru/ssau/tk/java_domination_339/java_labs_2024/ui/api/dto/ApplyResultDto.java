package ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApplyResultDto {
    private Double value;
    private Long newFunctionId;

    public ApplyResultDto(Double value, Long newFunctionId) {
        this.value = value;
        this.newFunctionId = newFunctionId;
    }

    public ApplyResultDto(Double value) {
        this.value = value;
        this.newFunctionId = null;
    }

}