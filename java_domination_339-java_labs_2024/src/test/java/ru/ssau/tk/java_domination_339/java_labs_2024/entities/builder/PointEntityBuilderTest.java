package ru.ssau.tk.java_domination_339.java_labs_2024.entities.builder;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.PointDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;

import static org.junit.jupiter.api.Assertions.*;

class PointEntityBuilderTest {

    @Test
    void makePointEntity_Success() {
        PointDto dto = new PointDto();
        dto.setId(1L);
        dto.setX(10.5);
        dto.setY(20.5);

        PointEntity result = PointEntityBuilder.makePointEntity(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(10.5, result.getX());
        assertEquals(20.5, result.getY());
    }

    @Test
    void makePointEntity_NullDto() {
        PointEntityBuilder.makePointEntity(null);
        PointEntity result = null;
        assertNull(result);
    }

    @Test
    void makePointEntity_WithNullValues() {
        PointDto dto = new PointDto();
        PointEntity result = PointEntityBuilder.makePointEntity(dto);

        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getX());
        assertNull(result.getY());
    }
}