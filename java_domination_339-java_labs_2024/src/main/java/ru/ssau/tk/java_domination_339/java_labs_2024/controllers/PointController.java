package ru.ssau.tk.java_domination_339.java_labs_2024.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.PointDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder.PointDtoBuilder;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.exceptions.NotFoundException;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.PointRepository;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PointController {

    @Autowired
    private PointRepository pointRepository;


    @PostMapping("/api/points")
    public ResponseEntity<PointDto> createOrUpdatePoint(@RequestParam(value = "x", required = false) Double X,
                                                        @RequestParam(value = "y", required = false) Double Y) {
        PointEntity pointEntity = pointRepository.saveAndFlush(new PointEntity(X, Y));

        //MathFunctionEntity foundFunction = mathFunctionRepository.findById(Math.toIntExact(function.getHash())).orElse(null);
        PointDto dto = PointDtoBuilder.makePointDto(pointEntity);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("/api/points/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            PointEntity entity = pointRepository.findById(id).orElseThrow(() -> new NotFoundException("Can't find point with id " + id));
            pointRepository.delete(entity);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();

        }
    }

    @GetMapping("/api/points/{id}")
    public ResponseEntity<PointDto> read(@PathVariable Long id) {
        try {
            PointDto pointDTO = PointDtoBuilder.makePointDto(pointRepository.findById(id).orElseThrow(() -> new NotFoundException("Can't find point with id " + id)));
            return ResponseEntity.ok(pointDTO);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
