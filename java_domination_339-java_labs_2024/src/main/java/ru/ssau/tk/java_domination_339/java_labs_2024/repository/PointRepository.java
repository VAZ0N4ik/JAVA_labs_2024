package ru.ssau.tk.java_domination_339.java_labs_2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.tk.java_domination_339.java_labs_2024.Entities.MathFunctionEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.Entities.PointEntity;

import java.util.List;

public interface PointRepository extends JpaRepository<PointEntity,Integer> {
    List<PointEntity> findByFunctionEntity(MathFunctionEntity function);
}
