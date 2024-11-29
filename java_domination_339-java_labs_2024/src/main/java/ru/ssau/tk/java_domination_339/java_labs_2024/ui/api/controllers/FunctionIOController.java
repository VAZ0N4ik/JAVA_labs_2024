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
@CrossOrigin(origins = "http://localhost:3000")  // Изменили порт на 3000
public class FunctionIOController {

    private final MathFunctionRepository mathFunctionRepository;
    private final MathFunctionService mathFunctionService;

    @PostMapping("/input")
    public ResponseEntity<MathFunctionDto> input(@RequestParam("file") MultipartFile file) {
        try {
            // Создаем временный файл
            File tempFile = File.createTempFile("function", ".txt");
            // Копируем данные из загруженного файла во временный
            file.transferTo(tempFile);

            // Читаем функцию из временного файла
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(tempFile));
            TabulatedFunction deserializedFunction = FunctionsIO.deserialize(inputStream);
            inputStream.close();

            // Удаляем временный файл
            tempFile.delete();

            // Сохраняем функцию в базу данных
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

            // Создаем временный файл
            File tempFile = File.createTempFile("function", ".txt");
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempFile));

            // Сериализуем функцию во временный файл
            FunctionsIO.serialize(outputStream, function);
            outputStream.close();

            // Создаем ресурс из файла
            InputStreamResource resource = new InputStreamResource(new FileInputStream(tempFile));

            // Настраиваем заголовки ответа
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=function_" + hash + ".txt");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(tempFile.length()));

            // Удаляем временный файл после отправки (можно реализовать через ServletContext.cleanup)
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