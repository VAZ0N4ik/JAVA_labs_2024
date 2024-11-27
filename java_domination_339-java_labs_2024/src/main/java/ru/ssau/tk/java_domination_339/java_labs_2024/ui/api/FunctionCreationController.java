package ru.ssau.tk.java_domination_339.java_labs_2024.ui.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.PointDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder.MathFunctionDtoBuilder;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.builder.MathFunctionEntityBuilder;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.MathFunctionType;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.SettingsController;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.TabulatedFunctionFactoryType;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.MathFunctionRepository;

import java.time.Instant;
import java.util.List;
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

    @GetMapping("/math-functions")
    public ResponseEntity<List<String>> getMathFunctions() {
        return ResponseEntity.ok(
                List.of(MathFunctionType.values()).stream()
                        .map(MathFunctionType::getLocalizedName)
                        .sorted()
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/create-from-points")
    public ResponseEntity<MathFunctionDto> createFromPoints(
            @RequestParam double[] x, @RequestParam double[] y
    ) {
        TabulatedFunctionFactoryType factoryType = settingsController.getCurrentFactoryType().getBody().getFactoryType();

        TabulatedFunction function = createTabulatedFunction(
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
                .name(function.Name())
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
    ) {
        TabulatedFunctionFactoryType factoryType = settingsController.getCurrentFactoryType().getBody().getFactoryType();

        MathFunction mathFunction =  MathFunctionType.getLocalizedFunctionMap()
                .get(name);
        System.out.println(mathFunction.apply(1));
        TabulatedFunction function = createTabulatedFunction(
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
                .name(function.Name())
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

    TabulatedFunction createTabulatedFunction(
            double[] xValues,
            double[] yValues,
            TabulatedFunctionFactoryType factoryType
    ) {
        return switch (factoryType) {
            case ARRAY_FACTORY -> new ArrayTabulatedFunction(xValues, yValues);
            case LINKED_LIST_FACTORY -> new LinkedListTabulatedFunction(xValues, yValues);
        };
    }

    private TabulatedFunction createTabulatedFunction(
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
}