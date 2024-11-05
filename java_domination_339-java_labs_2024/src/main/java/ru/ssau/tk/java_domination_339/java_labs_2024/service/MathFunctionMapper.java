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
}
