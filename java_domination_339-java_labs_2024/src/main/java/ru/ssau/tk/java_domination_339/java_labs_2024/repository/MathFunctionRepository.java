package ru.ssau.tk.java_domination_339.java_labs_2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface MathFunctionRepository extends JpaRepository<MathFunctionEntity, Long> {

    List<MathFunctionEntity> findAllBy();

    Stream<MathFunctionEntity> streamAllBy();

    Optional<MathFunctionEntity> findByHash(Long id);

    List<MathFunctionEntity> findByName(String functionName);

}