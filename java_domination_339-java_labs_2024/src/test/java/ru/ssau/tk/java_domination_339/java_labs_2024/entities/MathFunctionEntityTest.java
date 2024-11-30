package ru.ssau.tk.java_domination_339.java_labs_2024.entities;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MathFunctionEntityTest {

    @Test
    void defaultConstructor_CreatesEmptyFunction() {
        MathFunctionEntity function = new MathFunctionEntity();

        assertNull(function.getHash());
        assertNull(function.getName());
        assertNull(function.getPoints());
        assertNull(function.getCreatedAt());
        assertNull(function.getUpdateAt());
    }

    @Test
    void builder_CreatesFunctionWithAllFields() {
        Long hash = 1L;
        String name = "Test Function";
        List<PointEntity> points = List.of(new PointEntity(1.0, 1.0));
        Instant created = Instant.now();
        Instant updated = Instant.now();

        MathFunctionEntity function = MathFunctionEntity.builder()
                .hash(hash)
                .name(name)
                .points(points)
                .createdAt(created)
                .updateAt(updated)
                .build();

        assertEquals(hash, function.getHash());
        assertEquals(name, function.getName());
        assertEquals(points, function.getPoints());
        assertEquals(created, function.getCreatedAt());
        assertEquals(updated, function.getUpdateAt());
    }

    @Test
    void prePersist_SetsCreatedAt() {
        MathFunctionEntity function = new MathFunctionEntity();
        assertNull(function.getCreatedAt());

        function.prePersist();

        assertNotNull(function.getCreatedAt());
    }

    @Test
    void preUpdate_SetsUpdateAt() {
        MathFunctionEntity function = new MathFunctionEntity();
        assertNull(function.getUpdateAt());

        function.preUpdate();

        assertNotNull(function.getUpdateAt());
    }

    @Test
    void setters_UpdateFields() {
        MathFunctionEntity function = new MathFunctionEntity();
        Long hash = 2L;
        String name = "Updated Function";
        List<PointEntity> points = new ArrayList<>();
        Instant created = Instant.now();
        Instant updated = Instant.now();

        function.setHash(hash);
        function.setName(name);
        function.setPoints(points);
        function.setCreatedAt(created);
        function.setUpdateAt(updated);

        assertEquals(hash, function.getHash());
        assertEquals(name, function.getName());
        assertEquals(points, function.getPoints());
        assertEquals(created, function.getCreatedAt());
        assertEquals(updated, function.getUpdateAt());
    }
}