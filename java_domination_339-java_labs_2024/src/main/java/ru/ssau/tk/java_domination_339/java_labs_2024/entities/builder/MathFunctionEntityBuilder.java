package ru.ssau.tk.java_domination_339.java_labs_2024.entities.builder;

import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder.PointDtoBuilder;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;

public class MathFunctionEntityBuilder {

    static public MathFunctionEntity makeMathFunctionEntity(MathFunctionDto dto) {
        return MathFunctionEntity.builder()
                .hash(dto.getHashFunction())
                .points(dto.getPoints()
                        .stream()
                        .map(PointEntityBuilder::makePointEntity)
                        .toList())
                .createdAt(dto.getCreatedAt())
                .updateAt(dto.getUpdateAt())
                .build();
    }
}
