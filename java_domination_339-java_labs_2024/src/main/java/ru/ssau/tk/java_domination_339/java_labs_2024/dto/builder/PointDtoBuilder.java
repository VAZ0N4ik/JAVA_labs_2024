package ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder;

import ru.ssau.tk.java_domination_339.java_labs_2024.dto.PointDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;

public class PointDtoBuilder {

    public static PointDto makePointDto(PointEntity pointEntity) {
        return PointDto.builder()
                .x(pointEntity.getX())
                .y(pointEntity.getY())
                .build();
    }

}