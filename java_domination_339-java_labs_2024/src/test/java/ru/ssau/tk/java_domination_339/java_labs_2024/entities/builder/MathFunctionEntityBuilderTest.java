package ru.ssau.tk.java_domination_339.java_labs_2024.entities.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.PointDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;

import java.util.ArrayList;

class MathFunctionEntityBuilderTest {

    @Test
    void makeMathFunctionEntity_Success() {
        String name = "TestFunction";
        Long hash = 123L;

        PointDto point1 = new PointDto();
        point1.setX(1.0);
        point1.setY(2.0);

        PointDto point2 = new PointDto();
        point2.setX(3.0);
        point2.setY(4.0);

        List<PointDto> points = Arrays.asList(point1, point2);

        MathFunctionDto dto = new MathFunctionDto();
        dto.setName(name);
        dto.setHashFunction(hash);
        dto.setPoints(points);

        MathFunctionEntity result = MathFunctionEntityBuilder.makeMathFunctionEntity(dto);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(hash, result.getHash());
        assertEquals(2, result.getPoints().size());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdateAt());

        assertEquals(1.0, result.getPoints().get(0).getX());
        assertEquals(2.0, result.getPoints().get(0).getY());
        assertEquals(3.0, result.getPoints().get(1).getX());
        assertEquals(4.0, result.getPoints().get(1).getY());
    }

    @Test
    void makeMathFunctionEntity_WithEmptyPoints() {
        MathFunctionDto dto = new MathFunctionDto();
        dto.setName("EmptyFunction");
        dto.setHashFunction(456L);
        dto.setPoints(new ArrayList<>());

        MathFunctionEntity result = MathFunctionEntityBuilder.makeMathFunctionEntity(dto);

        assertNotNull(result);
        assertTrue(result.getPoints().isEmpty());
    }

    @Test
    void makeMathFunctionEntity_WithNullValues() {
        MathFunctionDto dto = new MathFunctionDto();
        dto.setPoints(new ArrayList<>());

        MathFunctionEntity result = MathFunctionEntityBuilder.makeMathFunctionEntity(dto);

        assertNotNull(result);
        assertNull(result.getName());
        assertNull(result.getHash());
        assertTrue(result.getPoints().isEmpty());
    }
}