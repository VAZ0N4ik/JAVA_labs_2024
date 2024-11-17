package ru.ssau.tk.java_domination_339.java_labs_2024.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.PointDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder.PointDtoBuilder;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.PointRepository;

import java.util.Optional;

@RestController
public class PointController {

    @Autowired
    private PointRepository pointRepository;


    @PostMapping("/api/points")
    public ResponseEntity<PointDto> createOrUpdatePoint( @RequestParam(value = "x", required = false) Double X,
                                                         @RequestParam(value = "y", required = false) Double Y) {
        PointEntity pointEntity = pointRepository.saveAndFlush(new PointEntity(X, Y));
        //TODO throw exceptions and if update change just array
        //MathFunctionEntity foundFunction = mathFunctionRepository.findById(Math.toIntExact(function.getHash())).orElse(null);
        PointDto dto = PointDtoBuilder.makePointDto(pointEntity);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
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

    @GetMapping("/api/points/{id}")
    public ResponseEntity<PointDto> read(@PathVariable Long id) {
        PointDto pointDTO = PointDtoBuilder.makePointDto(pointRepository.findById(id).orElse(null));
        return pointDTO != null ? ResponseEntity.ok(pointDTO) : ResponseEntity.notFound().build();
    }
}
