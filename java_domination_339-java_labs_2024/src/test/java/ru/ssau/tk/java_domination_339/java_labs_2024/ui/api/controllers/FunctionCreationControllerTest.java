package ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder.MathFunctionDtoBuilder;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.MathFunctionRepository;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.controllers.SettingsController;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.controllers.FunctionCreationController;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.enums.MathFunctionType;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.enums.TabulatedFunctionFactoryType;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.services.MathFunctionService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FunctionCreationControllerTest {

    @Mock
    private MathFunctionRepository mathFunctionRepository;

    @Mock
    private SettingsController settingsController;

    @Mock
    private MathFunctionService mathFunctionService;

    @InjectMocks
    private FunctionCreationController functionCreationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFromPoints() {
        // Arrange
        double[] x = {0.0, 1.0, 2.0};
        double[] y = {0.0, 1.0, 4.0};
        TabulatedFunctionFactoryType factoryType = TabulatedFunctionFactoryType.ARRAY_FACTORY;

        TabulatedFunction mockFunction = mock(TabulatedFunction.class);
        when(mockFunction.getCount()).thenReturn(3);
        when(mockFunction.getX(anyInt())).thenReturn(x[0], x[1], x[2]);
        when(mockFunction.getY(anyInt())).thenReturn(y[0], y[1], y[2]);
        when(mockFunction.Name()).thenReturn("TestFunction");
        when(mockFunction.HashName()).thenReturn(123L);



        when(mathFunctionService.createTabulatedFunction(x, y, factoryType))
                .thenReturn(mockFunction);

        MathFunctionEntity savedEntity = MathFunctionEntity.builder()
                .points(anyList())
                .name("TestFunction")
                .hash(123L)
                .build();

        when(mathFunctionRepository.findByHash(anyLong()))
                .thenReturn(Optional.empty());

        when(mathFunctionRepository.save(any(MathFunctionEntity.class)))
                .thenReturn(savedEntity);

        // Act
        ResponseEntity<MathFunctionDto> response = functionCreationController.createFromPoints(x, y);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(mathFunctionService).createTabulatedFunction(x, y, factoryType);
        verify(mathFunctionRepository).save(any(MathFunctionEntity.class));
    }

    @Test
    void testCreateFromMathFunction() throws Exception {
        // Arrange
        String functionName = "Square";
        double from = 0.0;
        double to = 10.0;
        int count = 5;
        TabulatedFunctionFactoryType factoryType = TabulatedFunctionFactoryType.ARRAY_FACTORY;

        TabulatedFunction mockFunction = mock(TabulatedFunction.class);
        when(mockFunction.getCount()).thenReturn(count);
        when(mockFunction.getX(anyInt())).thenReturn(0.0, 2.0, 4.0, 6.0, 8.0);
        when(mockFunction.getY(anyInt())).thenReturn(0.0, 4.0, 16.0, 36.0, 64.0);
        when(mockFunction.Name()).thenReturn("SquareFunction");
        when(mockFunction.HashName()).thenReturn(456L);


        when(mathFunctionService.createTabulatedFunction(
                any(), eq(from), eq(to), eq(count), eq(factoryType)))
                .thenReturn(mockFunction);

        MathFunctionEntity savedEntity = MathFunctionEntity.builder()
                .points(anyList())
                .name("SquareFunction")
                .hash(456L)
                .build();

        when(mathFunctionRepository.findByHash(anyLong()))
                .thenReturn(Optional.empty());

        when(mathFunctionRepository.save(any(MathFunctionEntity.class)))
                .thenReturn(savedEntity);

        // Act
        ResponseEntity<MathFunctionDto> response = functionCreationController.createFromMathFunction(
                functionName, from, to, count);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(mathFunctionService).createTabulatedFunction(
                any(), eq(from), eq(to), eq(count), eq(factoryType));
        verify(mathFunctionRepository).save(any(MathFunctionEntity.class));
    }

    @Test
    void testGetSimpleFunctions() throws Exception {
        // Act
        ResponseEntity<List<String>> response = functionCreationController.getSimpleFunctions();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertTrue(response.getBody().stream().allMatch(f -> !f.isEmpty()));
    }
}