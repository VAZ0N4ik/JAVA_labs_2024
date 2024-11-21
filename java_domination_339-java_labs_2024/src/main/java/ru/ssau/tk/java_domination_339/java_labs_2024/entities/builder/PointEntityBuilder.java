package ru.ssau.tk.java_domination_339.java_labs_2024.entities.builder;

import ru.ssau.tk.java_domination_339.java_labs_2024.dto.PointDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;

public class PointEntityBuilder {


        public static PointEntity makePointEntity(PointDto pointDto) {
            if(pointDto == null) {
                return null;
            }
            return PointEntity.builder().id(pointDto.getId())
                    .x(pointDto.getX())
                    .y(pointDto.getY())
                    .build();
        }

}

