package ru.ssau.tk.java_domination_339.java_labs_2024.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.ssau.tk.java_domination_339.java_labs_2024.MathApplication;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;

import java.time.Instant;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MathApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTest {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MathFunctionRepository mathFunctionRepository;

    @Test
    public void testCreateAndFindPoint() {
        PointEntity point = new PointEntity();
        point.setX(1.0);
        point.setY(2.0);
        pointRepository.save(point);

        PointEntity foundPoint = pointRepository.findById(point.getId()).orElse(null);
        assertThat(foundPoint).isNotNull();
        assertThat(foundPoint.getX()).isEqualTo(1.0);
        assertThat(foundPoint.getY()).isEqualTo(2.0);
    }

    @Test
    public void testUpdatePoint() {
        PointEntity point = new PointEntity();
        point.setX(1.0);
        point.setY(2.0);
        pointRepository.save(point);

        point.setX(3.0);
        point.setY(4.0);
        pointRepository.save(point);

        PointEntity updatedPoint = pointRepository.findById(point.getId()).orElse(null);
        assertThat(updatedPoint).isNotNull();
        assertThat(updatedPoint.getX()).isEqualTo(3.0);
        assertThat(updatedPoint.getY()).isEqualTo(4.0);
    }

    @Test
    public void testDeletePoint() {
        PointEntity point = new PointEntity();
        point.setX(1.0);
        point.setY(2.0);
        pointRepository.save(point);

        pointRepository.delete(point);

        PointEntity foundPoint = pointRepository.findById(point.getId()).orElse(null);
        assertThat(foundPoint).isNull();
    }

    @Test
    public void testCreateAndFindMathFunction() {
        MathFunctionEntity function = new MathFunctionEntity();
        function.setHash(12345L);
        function.setName("Test Function");
        function.setCreatedAt(Instant.now());
        function.setUpdateAt(Instant.now());

        mathFunctionRepository.save(function);

        MathFunctionEntity foundFunction = mathFunctionRepository.findById(function.getHash()).orElse(null);
        assertThat(foundFunction).isNotNull();
        assertThat(foundFunction.getName()).isEqualTo("Test Function");
    }

    @Test
    public void testDeleteMathFunction() {
        MathFunctionEntity function = new MathFunctionEntity();
        function.setHash(12345L);
        function.setName("Test Function");

        mathFunctionRepository.save(function);

        mathFunctionRepository.delete(function);

        MathFunctionEntity foundFunction = mathFunctionRepository.findById(function.getHash()).orElse(null);
        assertThat(foundFunction).isNull();
    }
}
