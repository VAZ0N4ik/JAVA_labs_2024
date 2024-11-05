package ru.ssau.tk.java_domination_339.java_labs_2024.service;

import ru.ssau.tk.java_domination_339.java_labs_2024.DTOs.MathFunctionDTO;
import ru.ssau.tk.java_domination_339.java_labs_2024.DTOs.PointDTO;
import ru.ssau.tk.java_domination_339.java_labs_2024.Entities.MathFunctionEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.Entities.PointEntity;

public class PointMapper {
    public static PointDTO pointEntityToDTO(PointEntity entity) {
        if (entity == null) {
            return null;
        }
        PointDTO dto = new PointDTO();
        dto.setPointId(entity.getPointId());
        dto.setX(entity.getX());
        dto.setY(entity.getY());
        dto.setFunctionEntity(dto.getFunctionEntity());
        return dto;
    }

    public static PointEntity pointDTOToEntity(PointDTO dto) {
        if (dto == null) {
            return null;
        }
        PointEntity entity = new PointEntity();
        entity.setPointId(dto.getPointId());
        entity.setX(dto.getX());
        entity.setY(dto.getY());
        entity.setFunctionEntity(dto.getFunctionEntity());
        return entity;
    }
}

