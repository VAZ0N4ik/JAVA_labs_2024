package ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.PointDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;

import static org.junit.jupiter.api.Assertions.*;

class PointDtoBuilderTest {

    @Test
    void makePointDto() {
        PointEntity entity = new PointEntity(1L,1.0,2.0);
        PointDto dto = PointDtoBuilder.makePointDto(entity);
        assertEquals(1.0, dto.getX());
        assertEquals(2.0, dto.getY());
        dto.setX(2.0);
        dto.setY(1.0);

        assertEquals(2.0, dto.getX());
        assertEquals(1.0, dto.getY());


        entity.setId(2L);
        entity.setX(2.0);
        entity.setY(1.0);

        assertEquals(2.0, entity.getX());
        assertEquals(1.0, entity.getY());
        assertEquals(2L, entity.getId());
    }
}