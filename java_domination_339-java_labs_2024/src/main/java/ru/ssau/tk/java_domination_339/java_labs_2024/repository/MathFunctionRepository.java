package ru.ssau.tk.java_domination_339.java_labs_2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface MathFunctionRepository extends JpaRepository<MathFunctionEntity, Integer> {

    Stream<MathFunctionEntity> findAllStream();

    Optional<MathFunctionEntity> findByHash(Integer id);

    List<MathFunctionEntity> findByName(String functionName);

}