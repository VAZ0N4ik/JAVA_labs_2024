package ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder;

import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;

public class MathFunctionDtoBuilder {

    static public MathFunctionDto makeMathFunctionDto(MathFunctionEntity entity) {
        if (entity == null)
            return null;
        return MathFunctionDto.builder()
                .hashFunction(entity.getHash())
                .name(entity.getName())
                .countPoint(entity.getPoints().size())
                .points(entity.getPoints()
                        .stream()
                        .map(PointDtoBuilder::makePointDto)
                        .toList())
                .createdAt(entity.getCreatedAt())
                .updateAt(entity.getUpdateAt())
                .build();
    }

}
