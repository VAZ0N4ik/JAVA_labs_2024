package ru.ssau.tk.java_domination_339.java_labs_2024.ui.api;


import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder.MathFunctionDtoBuilder;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.MathFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.TabulatedFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.io.FunctionsIO;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.MathFunctionRepository;

import java.io.*;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/api/function-io")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class FunctionIOController {

    private final FunctionsIO functionsIO = new FunctionsIO();
    private final MathFunctionRepository mathFunctionRepository;
    private final TabulatedFunctionOperationsController tabulatedFunctionOperationsController;

    @RequestMapping("/input")
    public ResponseEntity<MathFunctionDto> input(@RequestParam String path) throws IOException, ClassNotFoundException {
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream("output/serialized array functions.bin"));
        TabulatedFunction deserializedFunction = FunctionsIO.deserialize(inputStream);
        MathFunctionEntity entity = tabulatedFunctionOperationsController.createAndSaveMathFunctionEntity(deserializedFunction);
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

    @RequestMapping("/output")
    public ResponseEntity<MathFunctionDto> output(@RequestParam String path,@RequestParam @NotNull Long hash) throws IOException, ClassNotFoundException {
        TabulatedFunction function = tabulatedFunctionOperationsController.convertToTabulatedFunction(hash);
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path));
        FunctionsIO.serialize(outputStream, function);
        return new ResponseEntity<>(MathFunctionDtoBuilder.makeMathFunctionDto(tabulatedFunctionOperationsController.createAndSaveMathFunctionEntity(function)),HttpStatus.OK);

    }
    //TODO compose methods and craft safe method
}


