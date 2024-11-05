package ru.ssau.tk.java_domination_339.java_labs_2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.tk.java_domination_339.java_labs_2024.Entities.MathFunctionEntity;

import java.util.List;

public interface MathFunctionRepository extends JpaRepository<MathFunctionEntity, Integer> {
    List<MathFunctionEntity> findByFunctionType(String type);
}