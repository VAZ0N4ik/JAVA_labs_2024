package ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.controllers;


import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.io.FunctionsIO;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.MathFunctionRepository;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.services.MathFunctionService;

import java.io.*;


@RestController
@RequestMapping("/api/function-io")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class FunctionIOController {


    private final MathFunctionRepository mathFunctionRepository;
    private final FunctionOperationsController tabulatedFunctionOperationsController;
    private final MathFunctionService mathFunctionService;

    @PostMapping("/input")
    public ResponseEntity<MathFunctionDto> input(@RequestParam String path) throws IOException, ClassNotFoundException {
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(path));
        TabulatedFunction deserializedFunction = FunctionsIO.deserialize(inputStream);
        MathFunctionDto savedDto = mathFunctionService.createAndSaveMathFunctionEntity(deserializedFunction).getBody();


        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @GetMapping("/output")
    public ResponseEntity<MathFunctionDto> output(@RequestParam String path,@RequestParam @NotNull Long hash) throws IOException, ClassNotFoundException {
        TabulatedFunction function = mathFunctionService.convertToTabulatedFunction(hash);
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
        FunctionsIO.serialize(outputStream, function);
        return new ResponseEntity<>(mathFunctionService.createAndSaveMathFunctionEntity(function).getBody(),HttpStatus.OK);

    }
    //TODO compose methods and craft safe method
}


