package ru.ssau.tk.java_domination_339.java_labs_2024.service;

import ru.ssau.tk.java_domination_339.java_labs_2024.DTOs.MathFunctionDTO;
import ru.ssau.tk.java_domination_339.java_labs_2024.Entities.MathFunctionEntity;

public class MathFunctionMapper {
    public static MathFunctionDTO functionEntityToDTO(MathFunctionEntity entity) {
        if (entity == null) {
            return null;
        }

        MathFunctionDTO dto = new MathFunctionDTO();
        dto.setCompMethod(entity.getCompMethod());
        dto.setFunctionId(entity.getFunctionId());
        dto.setType(entity.getType());
        dto.setCount(entity.getCount());
        dto.setStart(entity.getStart());
        dto.setEnd(entity.getEnd());

        return dto;
    }

    public static MathFunctionEntity functionDTOToEntity(MathFunctionDTO dto) {
        if (dto == null) {
            return null;
        }

        MathFunctionEntity entity = new MathFunctionEntity();
        entity.setCompMethod(dto.getCompMethod());
        entity.setFunctionId(dto.getFunctionId());
        entity.setType(dto.getType());
        entity.setCount(dto.getCount());
        entity.setStart(dto.getStart());
        entity.setEnd(dto.getEnd());

        return entity;
    }
}
