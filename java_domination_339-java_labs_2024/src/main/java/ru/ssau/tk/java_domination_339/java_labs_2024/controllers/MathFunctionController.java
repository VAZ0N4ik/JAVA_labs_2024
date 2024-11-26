package ru.ssau.tk.java_domination_339.java_labs_2024.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder.MathFunctionDtoBuilder;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.builder.MathFunctionEntityBuilder;
import ru.ssau.tk.java_domination_339.java_labs_2024.exceptions.NotFoundException;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.MathFunctionRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MathFunctionController {
    @Autowired
    MathFunctionRepository mathFunctionRepository;

    @PostMapping("/api/functions")
    public ResponseEntity<MathFunctionDto> createOrUpdateMathFunction(@RequestBody MathFunctionDto mathFunctionDto) {
        MathFunctionEntity entityForFind = MathFunctionEntityBuilder.makeMathFunctionEntity((mathFunctionDto));
        Optional<MathFunctionEntity> entityFind = mathFunctionRepository.findByHash(entityForFind.getHash());
        if (entityFind.isPresent()) {
            entityForFind.setUpdateAt(Instant.now());
            entityForFind.setCreatedAt(entityFind.get().getCreatedAt());
        }
        MathFunctionEntity functionEntity = mathFunctionRepository.save(entityForFind);
        MathFunctionDto dto = MathFunctionDtoBuilder.makeMathFunctionDto(functionEntity);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @DeleteMapping("/api/functions/{hash}")
    public ResponseEntity<Void> delete(@PathVariable Long hash) {
        try {
            MathFunctionEntity entity = mathFunctionRepository.findByHash(hash).orElseThrow(() -> new NotFoundException("Can't find function with hash " + hash));
            mathFunctionRepository.delete(entity);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/functions/{hash}")
    public ResponseEntity<MathFunctionDto> read(@PathVariable Long hash) {
        try {
            MathFunctionDto functionDTO = MathFunctionDtoBuilder.makeMathFunctionDto(mathFunctionRepository.findByHash(hash).orElseThrow(() -> new NotFoundException("Can't find function with hash " + hash)));
            return new ResponseEntity<>(functionDTO, HttpStatus.OK);
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}