package ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.dto.SettingsDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.enums.TabulatedFunctionFactoryType;

import java.util.Objects;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class SettingsController {

    private static TabulatedFunctionFactoryType currentFactoryType = TabulatedFunctionFactoryType.ARRAY_FACTORY;

    @GetMapping("/factory-type")
    public ResponseEntity<SettingsDto> getCurrentFactoryType() {
        return ResponseEntity.ok(new SettingsDto(currentFactoryType));
    }

    @PostMapping("/factory-type")
    public ResponseEntity<SettingsDto> updateFactoryType(@RequestParam String name) {
        if (Objects.equals(name, "Массив"))
            currentFactoryType = TabulatedFunctionFactoryType.ARRAY_FACTORY;
        else
            currentFactoryType = TabulatedFunctionFactoryType.LINKED_LIST_FACTORY;
        SettingsDto settingsDto = new SettingsDto(currentFactoryType);
        return ResponseEntity.ok(settingsDto);
    }
}