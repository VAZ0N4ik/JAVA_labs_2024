package ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder;

import org.junit.jupiter.api.Test;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.PointDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;

import java.time.Instant;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

class MathFunctionDtoBuilderTest {

    @Test
    void makeMathFunctionDto() {
        Instant instantFromSeconds = Instant.ofEpochSecond(1622520000);
        Instant instantToSeconds = Instant.ofEpochSecond(1622520001);
        MathFunctionEntity entity = new MathFunctionEntity(123L,"f", new ArrayList<PointEntity> (), instantFromSeconds ,instantToSeconds);
        MathFunctionDto dto = MathFunctionDtoBuilder.makeMathFunctionDto(entity);
        assertEquals(instantToSeconds, dto.getUpdateAt());
        assertEquals(instantFromSeconds, dto.getCreatedAt());
        assertEquals(0, dto.getCountPoint());
        assertEquals(new ArrayList<PointDto> (),dto.getPoints());
        assertEquals(123L,dto.getHashFunction());

        entity.setHash(12L);
        entity.setName("a");

        assertEquals(instantToSeconds, entity.getUpdateAt());
        assertEquals(instantFromSeconds, entity.getCreatedAt());
        assertEquals("a", entity.getName());
        assertEquals(new ArrayList<PointEntity> (),entity.getPoints());
        assertEquals(12L,entity.getHash());


    }
}