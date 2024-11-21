package ru.ssau.tk.java_domination_339.java_labs_2024.entities.builder;

import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder.PointDtoBuilder;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;

import java.time.Instant;

public class MathFunctionEntityBuilder {

    static public MathFunctionEntity makeMathFunctionEntity(MathFunctionDto dto) {
        return MathFunctionEntity.builder().name(dto.getName())
                .hash(dto.getHashFunction())
                .points(dto.getPoints()
                        .stream()
                        .map(PointEntityBuilder::makePointEntity)
                        .toList())
                .createdAt(Instant.now())
                .updateAt(Instant.now())
                .build();
    }
}
