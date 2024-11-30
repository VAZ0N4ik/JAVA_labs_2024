package ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.exceptions.InconsistentFunctionsException;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.factory.LinkedListTabulatedFunctionFactory;
import ru.ssau.tk.java_domination_339.java_labs_2024.operations.TabulatedDifferentialOperator;
import ru.ssau.tk.java_domination_339.java_labs_2024.operations.TabulatedFunctionOperationService;
import ru.ssau.tk.java_domination_339.java_labs_2024.operations.TabulatedIntegrationOperator;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.MathFunctionRepository;

import jakarta.validation.constraints.NotNull;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.dto.ApplyResultDto;
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
    private final FunctionCreationController functionCreationController;
    private final MathFunctionService mathFunctionService;

    @PostMapping("/add")
    public ResponseEntity<MathFunctionDto> addFunctions(
            @RequestParam @NotNull Long functionId1,
            @RequestParam @NotNull Long functionId2
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
    public ResponseEntity<MathFunctionDto> deriveFunctions(
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

    @GetMapping("/integral")
    public ResponseEntity<Double> integralFunctions(
            @RequestParam @NotNull Long functionId, @RequestParam @NotNull Integer threads
    ) {

        TabulatedFunction function = mathFunctionService.convertToTabulatedFunction(functionId);
        Double result = new TabulatedIntegrationOperator(threads).integrate(function);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PostMapping("/apply")
    public ResponseEntity<ApplyResultDto> applyFunctions(
            @RequestParam @NotNull Long functionId, @RequestParam Double x
    ) {
        TabulatedFunction function = mathFunctionService.convertToTabulatedFunction(functionId);
        double ans;
        ApplyResultDto result;

        if (function.indexOfX(x) == -1) {
            ans = function.apply(x);
            if (function instanceof Insertable) {
                Insertable insertableObject = (Insertable) function;
                insertableObject.insert(x, ans);
                mathFunctionRepository.deleteById(functionId);
                MathFunctionDto savedFunction = mathFunctionService.createAndSaveMathFunctionEntity((TabulatedFunction) insertableObject).getBody();
                assert savedFunction != null;
                result = new ApplyResultDto(ans, savedFunction.getHashFunction());
            } else {
                result = new ApplyResultDto(ans);
            }
        } else {
            ans = function.getY(function.indexOfX(x));
            result = new ApplyResultDto(ans);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/is-insert")
    public ResponseEntity<Boolean> isInsertFunction(@RequestParam @NotNull Long functionId) {
        TabulatedFunction myObject = mathFunctionService.convertToTabulatedFunction(functionId);
        if (myObject instanceof Insertable) {
            // Класс реализует интерфейс Insertable
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    @PostMapping("/insert")
    public ResponseEntity<MathFunctionDto> insertFunction(@RequestParam @NotNull Long functionId, @RequestParam Double x, @RequestParam Double y) {
        TabulatedFunction myObject = mathFunctionService.convertToTabulatedFunction(functionId);
        if (myObject instanceof Insertable) {
            Insertable insertableObject = (Insertable) myObject;
            insertableObject.insert(x, y);
            mathFunctionRepository.deleteById(functionId);
            return new ResponseEntity<>(mathFunctionService.createAndSaveMathFunctionEntity((TabulatedFunction) insertableObject).getBody(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/is-remove")
    public ResponseEntity<Boolean> isRemoveFunction(@RequestParam @NotNull Long functionId) {
        TabulatedFunction myObject = mathFunctionService.convertToTabulatedFunction(functionId);
        if (myObject instanceof Removable) {
            // Класс реализует интерфейс Insertable
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }

    @PostMapping("/remove")
    public ResponseEntity<MathFunctionDto> removeFunction(@RequestParam @NotNull Long functionId, @RequestParam Double x) {
        TabulatedFunction myObject = mathFunctionService.convertToTabulatedFunction(functionId);
        int index = myObject.indexOfX(x);
        if (myObject instanceof Removable) {
            Removable removableObject = (Removable) myObject;
            removableObject.remove(index);
            mathFunctionRepository.deleteById(functionId);
            return new ResponseEntity<>(mathFunctionService.createAndSaveMathFunctionEntity((TabulatedFunction) removableObject).getBody(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/getX")
    public ResponseEntity<Double> getXFunction(@RequestParam Long functionId, @RequestParam int index) {
        TabulatedFunction myObject = mathFunctionService.convertToTabulatedFunction(functionId);
        return new ResponseEntity<>(myObject.getX(index), HttpStatus.OK);
    }

    @PostMapping("/getY")
    public ResponseEntity<Double> getYFunction(@RequestParam Long functionId, @RequestParam int index) {
        TabulatedFunction myObject = mathFunctionService.convertToTabulatedFunction(functionId);
        return new ResponseEntity<>(myObject.getY(index), HttpStatus.OK);
    }

    @PostMapping("/setY")
    public ResponseEntity<MathFunctionDto> setYFunction(@RequestParam Long functionId, @RequestParam int index, @RequestParam Double y) {
        TabulatedFunction myObject = mathFunctionService.convertToTabulatedFunction(functionId);
        myObject.setY(index, y);
        mathFunctionRepository.deleteById(functionId);
        return new ResponseEntity<>(mathFunctionService.createAndSaveMathFunctionEntity(myObject).getBody(), HttpStatus.OK);
    }
}