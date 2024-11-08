package ru.ssau.tk.java_domination_339.java_labs_2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;

import java.util.Optional;

public interface PointRepository extends JpaRepository<PointEntity, Long> {

    Optional<PointEntity> findById(Long id);

}
