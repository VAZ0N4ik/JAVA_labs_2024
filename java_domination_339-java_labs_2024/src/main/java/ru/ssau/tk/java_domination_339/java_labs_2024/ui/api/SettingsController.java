package ru.ssau.tk.java_domination_339.java_labs_2024.ui.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.SettingsDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.ui.api.TabulatedFunctionFactoryType;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class SettingsController {

    private TabulatedFunctionFactoryType currentFactoryType = TabulatedFunctionFactoryType.ARRAY_FACTORY;

    @GetMapping("/factory-type")
    public ResponseEntity<SettingsDto> getCurrentFactoryType() {
        return ResponseEntity.ok(new SettingsDto(currentFactoryType));
    }

    @PostMapping("/factory-type")
    public ResponseEntity<SettingsDto> updateFactoryType(@RequestBody SettingsDto settingsDto) {
        this.currentFactoryType = settingsDto.getFactoryType();
        return ResponseEntity.ok(settingsDto);
    }
}