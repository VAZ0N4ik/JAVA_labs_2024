package ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder.MathFunctionDtoBuilder;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.MathFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.MathFunctionRepository;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.controllers.SettingsController;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.enums.TabulatedFunctionFactoryType;


import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class MathFunctionService {
    @Autowired
    private final MathFunctionRepository mathFunctionRepository;
    private static final SettingsController settingsController = new SettingsController();

    public ResponseEntity<MathFunctionDto> saveAndUpdateMathFunction(MathFunctionEntity entity) {
        Optional<MathFunctionEntity> entityFind = mathFunctionRepository.findByHash(entity.getHash());
        if (entityFind.isPresent()) {
            entity.setUpdateAt(Instant.now());
            entity.setCreatedAt(entityFind.get().getCreatedAt());
        }
        MathFunctionDto savedDto = MathFunctionDtoBuilder.makeMathFunctionDto(
                mathFunctionRepository.save(entity)
        );
        return new ResponseEntity<>(savedDto, HttpStatus.OK);
    }

    public TabulatedFunction createTabulatedFunction(
            double[] xValues,
            double[] yValues,
            TabulatedFunctionFactoryType factoryType
    ) {
        return switch (factoryType) {
            case ARRAY_FACTORY -> new ArrayTabulatedFunction(xValues, yValues);
            case LINKED_LIST_FACTORY -> new LinkedListTabulatedFunction(xValues, yValues);
        };
    }

    public TabulatedFunction createTabulatedFunction(
            MathFunction source,
            double xFrom,
            double xTo,
            int count,
            TabulatedFunctionFactoryType factoryType
    ) {
        return switch (factoryType) {
            case ARRAY_FACTORY -> new ArrayTabulatedFunction(source, xFrom, xTo, count);
            case LINKED_LIST_FACTORY -> new LinkedListTabulatedFunction(source, xFrom, xTo, count);
        };
    }

    public TabulatedFunction convertToTabulatedFunction(Long functionId) {

        MathFunctionEntity functionEntity = mathFunctionRepository.findById(functionId)
                .orElseThrow(() -> new RuntimeException("Function not found"));

        TabulatedFunctionFactoryType factoryType = settingsController.getCurrentFactoryType().getBody().getFactoryType();

        double[] xValues = functionEntity.getPoints().stream()
                .mapToDouble(PointEntity::getX)
                .toArray();

        double[] yValues = functionEntity.getPoints().stream()
                .mapToDouble(PointEntity::getY)
                .toArray();

        return createTabulatedFunction(xValues, yValues, factoryType);
    }

    public ResponseEntity<MathFunctionDto> createAndSaveMathFunctionEntity(TabulatedFunction function) {
        MathFunctionEntity entity = MathFunctionEntity.builder()
                .points(
                        IntStream.range(0, function.getCount())
                                .mapToObj(i -> new PointEntity(function.getX(i), function.getY(i)))
                                .collect(Collectors.toList())
                )
                .name(function.getClass().getSimpleName())
                .hash(function.HashName())
                .build();

        return saveAndUpdateMathFunction(entity);
    }
}
