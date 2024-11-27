package ru.ssau.tk.java_domination_339.java_labs_2024.ui.api;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder.MathFunctionDtoBuilder;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.operations.TabulatedFunctionOperationService;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.MathFunctionRepository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/tabulated-function-operations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
@Validated
public class TabulatedFunctionOperationsController {
    private final TabulatedFunctionOperationService operationService = new TabulatedFunctionOperationService();
    private final MathFunctionRepository mathFunctionRepository;
    private final SettingsController settingsController;
    private final FunctionCreationController functionCreationController ;

    @PostMapping("/add")
    public ResponseEntity<MathFunctionDto> addFunctions(
            @RequestParam @NotNull  Long functionId1,
            @RequestParam @NotNull  Long functionId2
    ) {
        try {
            TabulatedFunction function1 = convertToTabulatedFunction(functionId1);
            TabulatedFunction function2 = convertToTabulatedFunction(functionId2);
            System.out.println(function1);
            System.out.println(function2);
            TabulatedFunction resultFunction = operationService.additionOperation(function1, function2);
            System.out.println(resultFunction);
            MathFunctionEntity entity = createAndSaveMathFunctionEntity(resultFunction);

            return new ResponseEntity<>(MathFunctionDtoBuilder.makeMathFunctionDto(entity), HttpStatus.CREATED);
        } catch (InconsistentFunctionsException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/subtract")
    public ResponseEntity<MathFunctionDto> subtractFunctions(
            @RequestParam @NotNull Long functionId1,
            @RequestParam @NotNull Long functionId2
    ) {
        try {
            TabulatedFunction function1 = convertToTabulatedFunction(functionId1);
            TabulatedFunction function2 = convertToTabulatedFunction(functionId2);

            TabulatedFunction resultFunction = operationService.substractionOperation(function1, function2);

            MathFunctionEntity entity = createAndSaveMathFunctionEntity(resultFunction);

            return new ResponseEntity<>(MathFunctionDtoBuilder.makeMathFunctionDto(entity), HttpStatus.CREATED);
        } catch (InconsistentFunctionsException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/multiply")
    public ResponseEntity<MathFunctionDto> multiplyFunctions(
            @RequestParam @NotNull Long functionId1,
            @RequestParam @NotNull Long functionId2
    ) {
        try {
            TabulatedFunction function1 = convertToTabulatedFunction(functionId1);
            TabulatedFunction function2 = convertToTabulatedFunction(functionId2);

            TabulatedFunction resultFunction = operationService.multiplicationOperation(function1, function2);

            MathFunctionEntity entity = createAndSaveMathFunctionEntity(resultFunction);

            return new ResponseEntity<>(MathFunctionDtoBuilder.makeMathFunctionDto(entity), HttpStatus.CREATED);
        } catch (InconsistentFunctionsException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/divide")
    public ResponseEntity<MathFunctionDto> divideFunctions(
            @RequestParam @NotNull Long functionId1,
            @RequestParam @NotNull Long functionId2
    ) {
        try {
            TabulatedFunction function1 = convertToTabulatedFunction(functionId1);
            TabulatedFunction function2 = convertToTabulatedFunction(functionId2);

            TabulatedFunction resultFunction = operationService.divisionOperation(function1, function2);

            MathFunctionEntity entity = createAndSaveMathFunctionEntity(resultFunction);

            return new ResponseEntity<>(MathFunctionDtoBuilder.makeMathFunctionDto(entity), HttpStatus.CREATED);
        } catch (InconsistentFunctionsException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

        return functionCreationController.createTabulatedFunction(xValues, yValues, factoryType);
    }

    private MathFunctionEntity createAndSaveMathFunctionEntity(TabulatedFunction function) {
        MathFunctionEntity entity = MathFunctionEntity.builder()
                .points(
                        IntStream.range(0, function.getCount())
                                .mapToObj(i -> new PointEntity(function.getX(i), function.getY(i)))
                                .collect(Collectors.toList())
                )
                .name(function.Name())
                .hash(function.HashName())
                .build();

        return mathFunctionRepository.save(entity);
    }
}