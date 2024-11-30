package ru.ssau.tk.java_domination_339.java_labs_2024.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointEntityTest {

    @Test
    void defaultConstructor_CreatesEmptyPoint() {
        PointEntity point = new PointEntity();

        assertNull(point.getId());
        assertNull(point.getX());
        assertNull(point.getY());
    }

    @Test
    void coordinatesConstructor_SetsXAndY() {
        Double x = 1.0;
        Double y = 2.0;
        PointEntity point = new PointEntity(x, y);

        assertNull(point.getId());
        assertEquals(x, point.getX());
        assertEquals(y, point.getY());
    }

    @Test
    void builder_CreatesPointWithAllFields() {
        Long id = 1L;
        Double x = 3.0;
        Double y = 4.0;

        PointEntity point = PointEntity.builder()
                .id(id)
                .x(x)
                .y(y)
                .build();

        assertEquals(id, point.getId());
        assertEquals(x, point.getX());
        assertEquals(y, point.getY());
    }

    @Test
    void setters_UpdateFields() {
        PointEntity point = new PointEntity();
        Long id = 2L;
        Double x = 5.0;
        Double y = 6.0;

        point.setId(id);
        point.setX(x);
        point.setY(y);

        assertEquals(id, point.getId());
        assertEquals(x, point.getX());
        assertEquals(y, point.getY());
    }
}