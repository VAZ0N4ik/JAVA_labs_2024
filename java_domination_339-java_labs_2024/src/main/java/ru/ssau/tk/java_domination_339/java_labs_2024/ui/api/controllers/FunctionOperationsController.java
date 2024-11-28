package ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.ArrayTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.LinkedListTabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.operations.TabulatedDifferentialOperator;
import ru.ssau.tk.java_domination_339.java_labs_2024.operations.TabulatedFunctionOperationService;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.MathFunctionRepository;

import jakarta.validation.constraints.NotNull;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.enums.TabulatedFunctionFactoryType;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.services.MathFunctionService;


@RestController
@RequestMapping("/api/tabulated-function-operations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
@Validated
public class FunctionOperationsController {
    private final TabulatedFunctionOperationService operationService = new TabulatedFunctionOperationService();
    private final MathFunctionRepository mathFunctionRepository;
    private final SettingsController settingsController;
    private final FunctionCreationController functionCreationController ;
    private final MathFunctionService mathFunctionService;
    @PostMapping("/add")
    public ResponseEntity<MathFunctionDto> addFunctions(
            @RequestParam @NotNull  Long functionId1,
            @RequestParam @NotNull  Long functionId2
    ) {
        try {
            TabulatedFunction function1 = mathFunctionService.convertToTabulatedFunction(functionId1);
            TabulatedFunction function2 = mathFunctionService.convertToTabulatedFunction(functionId2);

            TabulatedFunction resultFunction = operationService.additionOperation(function1, function2);



            return new ResponseEntity<>(mathFunctionService.createAndSaveMathFunctionEntity(resultFunction).getBody(), HttpStatus.CREATED);
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
            TabulatedFunction function1 = mathFunctionService.convertToTabulatedFunction(functionId1);
            TabulatedFunction function2 = mathFunctionService.convertToTabulatedFunction(functionId2);

            TabulatedFunction resultFunction = operationService.substractionOperation(function1, function2);


            return new ResponseEntity<>(mathFunctionService.createAndSaveMathFunctionEntity(resultFunction).getBody(), HttpStatus.CREATED);
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
            TabulatedFunction function1 = mathFunctionService.convertToTabulatedFunction(functionId1);
            TabulatedFunction function2 = mathFunctionService.convertToTabulatedFunction(functionId2);

            TabulatedFunction resultFunction = operationService.multiplicationOperation(function1, function2);

            return new ResponseEntity<>(mathFunctionService.createAndSaveMathFunctionEntity(resultFunction).getBody(), HttpStatus.CREATED);
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
            TabulatedFunction function1 = mathFunctionService.convertToTabulatedFunction(functionId1);
            TabulatedFunction function2 = mathFunctionService.convertToTabulatedFunction(functionId2);

            TabulatedFunction resultFunction = operationService.divisionOperation(function1, function2);


            return new ResponseEntity<>(mathFunctionService.createAndSaveMathFunctionEntity(resultFunction).getBody(), HttpStatus.CREATED);
        } catch (InconsistentFunctionsException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/derive")
    public ResponseEntity<MathFunctionDto> subtractFunctions(
            @RequestParam @NotNull Long functionId
    ) {
        try {
            TabulatedFunction function = mathFunctionService.convertToTabulatedFunction(functionId);
            TabulatedFunctionFactoryType type = settingsController.getCurrentFactoryType().getBody().getFactoryType();

            TabulatedFunction resultFunction = new TabulatedDifferentialOperator(switch (type) {
                case ARRAY_FACTORY -> new ArrayTabulatedFunctionFactory();
                case LINKED_LIST_FACTORY -> new LinkedListTabulatedFunctionFactory();
            }).derive(function);


            return new ResponseEntity<>(mathFunctionService.createAndSaveMathFunctionEntity(resultFunction).getBody(), HttpStatus.CREATED);
        } catch (InconsistentFunctionsException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}