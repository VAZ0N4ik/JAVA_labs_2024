package ru.ssau.tk.java_domination_339.java_labs_2024.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.PointDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder.MathFunctionDtoBuilder;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder.PointDtoBuilder;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.PointRepository;

@RestController
public class PointController {
    private PointRepository pointRepository;
    private PointDtoBuilder pointDtoBuilder;

    @PostMapping("/api/points")
    public ResponseEntity<PointDto> createOrUpdatePoint(@RequestBody PointEntity point){
        PointEntity pointEntity =pointRepository.save(point);
        //TODO throw exceptions and if update change just array
        //MathFunctionEntity foundFunction = mathFunctionRepository.findById(Math.toIntExact(function.getHash())).orElse(null);
        PointDto dto = PointDtoBuilder.makePointDto(pointEntity);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/api/points/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        PointEntity entity = pointRepository.findById(id).orElse(null);
        if (entity != null)
            pointRepository.delete(entity);
        else
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/points/{hash}")
    public ResponseEntity<PointDto> read(@PathVariable Long id) {
        PointDto pointDTO = PointDtoBuilder.makePointDto(pointRepository.findById(id).orElse(null));
        return pointDTO != null ? ResponseEntity.ok(pointDTO) : ResponseEntity.notFound().build();
    }
}
