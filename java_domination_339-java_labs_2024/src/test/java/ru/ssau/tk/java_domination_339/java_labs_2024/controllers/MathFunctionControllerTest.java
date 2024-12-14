package ru.ssau.tk.java_domination_339.java_labs_2024.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.PointDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.MathFunctionRepository;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class MathFunctionControllerTest {

    @Mock
    private MathFunctionRepository mathFunctionRepository;

    @InjectMocks
    private MathFunctionController mathFunctionController;

    private MathFunctionDto testDto;
    private MathFunctionEntity testEntity;
    private final Long TEST_HASH = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        List<PointDto> pointDtos = new ArrayList<>();
        pointDtos.add(new PointDto(1L, 1.0, 1.0));

        testDto = new MathFunctionDto();
        testDto.setHashFunction(TEST_HASH);
        testDto.setName("Test Function");
        testDto.setPoints(pointDtos);

        List<PointEntity> pointEntities = new ArrayList<>();
        pointEntities.add(new PointEntity(1.0, 1.0));

        testEntity = new MathFunctionEntity();
        testEntity.setHash(TEST_HASH);
        testEntity.setName("Test Function");
        testEntity.setPoints(pointEntities);
        testEntity.setCreatedAt(Instant.now());
        testEntity.setUpdateAt(Instant.now());
    }

    @Test
    void createMathFunction_NewFunction_Success() {
        when(mathFunctionRepository.findByHash(TEST_HASH)).thenReturn(Optional.empty());
        when(mathFunctionRepository.save(any())).thenReturn(testEntity);

        ResponseEntity<MathFunctionDto> response = mathFunctionController.createOrUpdateMathFunction(testDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_HASH, response.getBody().getHashFunction());
        verify(mathFunctionRepository).findByHash(TEST_HASH);
        verify(mathFunctionRepository).save(any());
    }

    @Test
    void createMathFunction_UpdateExisting_Success() {
        when(mathFunctionRepository.findByHash(TEST_HASH)).thenReturn(Optional.of(testEntity));
        when(mathFunctionRepository.save(any())).thenReturn(testEntity);

        ResponseEntity<MathFunctionDto> response = mathFunctionController.createOrUpdateMathFunction(testDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_HASH, response.getBody().getHashFunction());
        verify(mathFunctionRepository).findByHash(TEST_HASH);
        verify(mathFunctionRepository).save(any());
    }

    @Test
    void deleteMathFunction_Success() {
        when(mathFunctionRepository.findByHash(TEST_HASH)).thenReturn(Optional.of(testEntity));
        doNothing().when(mathFunctionRepository).delete(any());

        ResponseEntity<Void> response = mathFunctionController.delete(TEST_HASH);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(mathFunctionRepository).findByHash(TEST_HASH);
        verify(mathFunctionRepository).delete(testEntity);
    }

    @Test
    void deleteMathFunction_NotFound() {
        when(mathFunctionRepository.findByHash(TEST_HASH)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = mathFunctionController.delete(TEST_HASH);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(mathFunctionRepository).findByHash(TEST_HASH);
        verify(mathFunctionRepository, never()).delete(any());
    }

    @Test
    void readMathFunction_Success() {
        when(mathFunctionRepository.findByHash(TEST_HASH)).thenReturn(Optional.of(testEntity));

        ResponseEntity<MathFunctionDto> response = mathFunctionController.read(TEST_HASH);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_HASH, response.getBody().getHashFunction());
        verify(mathFunctionRepository).findByHash(TEST_HASH);
    }

    @Test
    void readMathFunction_NotFound() {
        when(mathFunctionRepository.findByHash(TEST_HASH)).thenReturn(Optional.empty());

        ResponseEntity<MathFunctionDto> response = mathFunctionController.read(TEST_HASH);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(mathFunctionRepository).findByHash(TEST_HASH);
    }
}