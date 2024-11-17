package ru.ssau.tk.java_domination_339.java_labs_2024.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointDto {
    private Long id;

    private Double x;

    private Double y;

}
