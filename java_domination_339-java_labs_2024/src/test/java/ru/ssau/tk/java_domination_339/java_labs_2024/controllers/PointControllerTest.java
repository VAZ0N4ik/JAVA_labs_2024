package ru.ssau.tk.java_domination_339.java_labs_2024.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.PointDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.PointRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PointControllerTest {

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private PointController pointController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPoint_Success() {
        Double x = 1.0;
        Double y = 2.0;
        PointEntity entity = new PointEntity(x, y);
        entity.setId(1L);

        when(pointRepository.saveAndFlush(any())).thenReturn(entity);

        ResponseEntity<PointDto> response = pointController.createOrUpdatePoint(x, y);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(x, response.getBody().getX());
        assertEquals(y, response.getBody().getY());
        verify(pointRepository).saveAndFlush(any());
    }

    @Test
    void deletePoint_Success() {
        Long id = 1L;
        PointEntity entity = new PointEntity(1.0, 1.0);
        when(pointRepository.findById(id)).thenReturn(Optional.of(entity));

        ResponseEntity<Void> response = pointController.delete(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(pointRepository).delete(entity);
    }

    @Test
    void deletePoint_NotFound() {
        Long id = 1L;
        when(pointRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = pointController.delete(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(pointRepository, never()).delete(any());
    }

    @Test
    void readPoint_Success() {
        Long id = 1L;
        PointEntity entity = new PointEntity(1.0, 2.0);
        entity.setId(id);
        when(pointRepository.findById(id)).thenReturn(Optional.of(entity));

        ResponseEntity<PointDto> response = pointController.read(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1.0, response.getBody().getX());
        assertEquals(2.0, response.getBody().getY());
    }

    @Test
    void readPoint_NotFound() {
        Long id = 1L;
        when(pointRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<PointDto> response = pointController.read(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}