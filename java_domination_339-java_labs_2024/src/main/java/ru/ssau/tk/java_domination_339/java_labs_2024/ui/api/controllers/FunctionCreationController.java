package ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.controllers;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder.MathFunctionDtoBuilder;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.services.MathFunctionService;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.enums.MathFunctionType;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.enums.TabulatedFunctionFactoryType;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.MathFunctionRepository;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RestController
@RequestMapping("/api/function-creation")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class FunctionCreationController {

    private final MathFunctionRepository mathFunctionRepository;
    private final SettingsController settingsController;
    private final MathFunctionService mathFunctionService;


    @PostMapping("/create-from-points")
    public ResponseEntity<MathFunctionDto> createFromPoints(
            @RequestParam double[] x, @RequestParam double[] y
    ) {
        TabulatedFunctionFactoryType factoryType = settingsController.getCurrentFactoryType().getBody().getFactoryType();

        TabulatedFunction function = mathFunctionService.createTabulatedFunction(
                x,
                y,
                factoryType
        );

        // Создаем сущность с использованием точек из созданной функции
        List<PointEntity> points = IntStream.range(0, function.getCount())
                .mapToObj(i -> new PointEntity(function.getX(i), function.getY(i)))
                .collect(Collectors.toList());

        MathFunctionEntity entity = MathFunctionEntity.builder()
                .points(points)
                .name(function.getClass().getSimpleName())
                .hash(function.HashName())
                .build();


        Optional<MathFunctionEntity> entityFind = mathFunctionRepository.findByHash(entity.getHash());
        if (entityFind.isPresent()) {
            entity.setUpdateAt(Instant.now());
            entity.setCreatedAt(entityFind.get().getCreatedAt());
        }
        MathFunctionDto savedDto = MathFunctionDtoBuilder.makeMathFunctionDto(
                mathFunctionRepository.save(entity)
        );

        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @PostMapping("/create-from-math-function")
    public ResponseEntity<MathFunctionDto> createFromMathFunction(
            @RequestParam String name, @RequestParam Double from, @RequestParam Double to, @RequestParam int count
    ) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TabulatedFunctionFactoryType factoryType = settingsController.getCurrentFactoryType().getBody().getFactoryType();

        MathFunction mathFunction = MathFunctionType.getLocalizedFunctionMap()
                .get(name);
        System.out.println(mathFunction.apply(1));
        TabulatedFunction function = mathFunctionService.createTabulatedFunction(
                mathFunction,
                from,
                to,
                count,
                factoryType
        );

        // Создаем сущность с использованием точек из созданной функции
        List<PointEntity> points = IntStream.range(0, function.getCount())
                .mapToObj(i -> new PointEntity(function.getX(i), function.getY(i)))
                .collect(Collectors.toList());

        MathFunctionEntity entity = MathFunctionEntity.builder()
                .points(points)
                .name(function.getClass().getSimpleName())
                .hash(function.HashName())
                .build();

        // Сохранение функции в базу данных
        Optional<MathFunctionEntity> entityFind = mathFunctionRepository.findByHash(entity.getHash());
        if (entityFind.isPresent()) {
            entity.setUpdateAt(Instant.now());
            entity.setCreatedAt(entityFind.get().getCreatedAt());
        }
        MathFunctionDto savedDto = MathFunctionDtoBuilder.makeMathFunctionDto(
                mathFunctionRepository.save(entity)
        );


        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @GetMapping("/functions-to-create")
    public ResponseEntity<List<String>> getSimpleFunctions() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<String> ans = MathFunctionType.getFunctions();
        return new ResponseEntity<>(ans, HttpStatus.OK);
    }


    @PostMapping("/create-composite")
    public ResponseEntity<MathFunctionDto> createCompositeFunction(@RequestParam @NotNull Long hash1, @RequestParam @NotNull Long hash2, @RequestParam @NotNull String name) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TabulatedFunction function1 = mathFunctionService.convertToTabulatedFunction(hash1);
        TabulatedFunction function2 = mathFunctionService.convertToTabulatedFunction(hash2);
        CompositeFunction composite = new CompositeFunction(function1, function2);

        MathFunctionType.addFunctionMap(name, (MathFunction) composite);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}