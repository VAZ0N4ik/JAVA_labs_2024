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
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;


@RestController
@RequestMapping("/api/function-io")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class FunctionIOController {
    private final MathFunctionRepository mathFunctionRepository;
    private final MathFunctionService mathFunctionService;

    @PostMapping("/input")
    public ResponseEntity<MathFunctionDto> input(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("function", ".txt");
            file.transferTo(tempFile);

            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(tempFile));
            TabulatedFunction deserializedFunction = FunctionsIO.deserialize(inputStream);
            inputStream.close();

            tempFile.delete();

            MathFunctionDto savedDto = mathFunctionService.createAndSaveMathFunctionEntity(deserializedFunction).getBody();
            return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/input-json")
    public ResponseEntity<MathFunctionDto> inputJson(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("function", ".json");
            file.transferTo(tempFile);

            BufferedReader reader = new BufferedReader(new FileReader(tempFile));
            TabulatedFunction deserializedFunction = FunctionsIO.deserializeJson(reader);
            reader.close();

            tempFile.delete();

            MathFunctionDto savedDto = mathFunctionService.createAndSaveMathFunctionEntity(deserializedFunction).getBody();
            return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/input-xml")
    public ResponseEntity<MathFunctionDto> inputXml(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("function", ".xml");
            file.transferTo(tempFile);

            BufferedReader reader = new BufferedReader(new FileReader(tempFile));
            TabulatedFunction deserializedFunction = FunctionsIO.deserializeXml(reader);
            reader.close();

            tempFile.delete();

            MathFunctionDto savedDto = mathFunctionService.createAndSaveMathFunctionEntity(deserializedFunction).getBody();
            return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/output")
    public ResponseEntity<Resource> output(@RequestParam @NotNull Long hash) {
        try {
            TabulatedFunction function = mathFunctionService.convertToTabulatedFunction(hash);

            File tempFile = File.createTempFile("function", ".txt");
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempFile));

            FunctionsIO.serialize(outputStream, function);
            outputStream.close();

            InputStreamResource resource = new InputStreamResource(new FileInputStream(tempFile));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=function_" + hash + ".txt");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(tempFile.length()));

            tempFile.deleteOnExit();

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/output-json")
    public ResponseEntity<Resource> outputJson(@RequestParam @NotNull Long hash) {
        try {
            TabulatedFunction function = mathFunctionService.convertToTabulatedFunction(hash);

            File tempFile = File.createTempFile("function", ".json");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            FunctionsIO.serializeJson(writer, function);
            writer.close();

            InputStreamResource resource = new InputStreamResource(new FileInputStream(tempFile));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=function_" + hash + ".json");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(tempFile.length()));

            tempFile.deleteOnExit();

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/output-xml")
    public ResponseEntity<Resource> outputXml(@RequestParam @NotNull Long hash) {
        try {
            TabulatedFunction function = mathFunctionService.convertToTabulatedFunction(hash);

            File tempFile = File.createTempFile("function", ".xml");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            FunctionsIO.serializeXml(writer, function);
            writer.close();

            InputStreamResource resource = new InputStreamResource(new FileInputStream(tempFile));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=function_" + hash + ".xml");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/xml");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(tempFile.length()));

            tempFile.deleteOnExit();

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}