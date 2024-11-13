package ru.ssau.tk.java_domination_339.java_labs_2024.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.MathFunctionDto;
import ru.ssau.tk.java_domination_339.java_labs_2024.dto.builder.MathFunctionDtoBuilder;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.MathFunctionRepository;

@RequiredArgsConstructor
@Transactional
@RestController
public class MathFunctionController {

    MathFunctionRepository mathFunctionRepository;

    MathFunctionDtoBuilder mathFunctionDtoBuilder;


    @PostMapping("/api/functions")
    public ResponseEntity<MathFunctionDto> createOrUpdateMathFunction(@RequestBody MathFunctionEntity function) {
        MathFunctionEntity functionEntity = mathFunctionRepository.save(function);
        //TODO throw exceptions and if update change just array
        //MathFunctionEntity foundFunction = mathFunctionRepository.findById(Math.toIntExact(function.getHash())).orElse(null);
        MathFunctionDto dto = MathFunctionDtoBuilder.makeMathFunctionDto(functionEntity);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/api/functions/{hash}")
    public ResponseEntity<Void> delete(@PathVariable Long hash) {
        MathFunctionEntity entity = mathFunctionRepository.findByHash(hash).orElseThrow();
        mathFunctionRepository.delete(entity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/functions/{hash}")
    public ResponseEntity<MathFunctionDto> read(@PathVariable Long hash) {
        MathFunctionDto functionDTO = MathFunctionDtoBuilder.makeMathFunctionDto(mathFunctionRepository.findByHash(hash).orElse(null));
        return functionDTO != null ? ResponseEntity.ok(functionDTO) : ResponseEntity.notFound().build();
    }

}