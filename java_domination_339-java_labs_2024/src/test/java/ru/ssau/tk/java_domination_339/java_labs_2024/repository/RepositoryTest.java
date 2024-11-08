import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.ssau.tk.java_domination_339.java_labs_2024.MathApplication;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.MathFunctionEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.entities.PointEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.MathFunctionRepository;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.PointRepository;

import java.time.Instant;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MathApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Используйте реальную БД PostgreSQL
public class RepositoryTest {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MathFunctionRepository mathFunctionRepository;

    @Test
    public void testCreateAndFindPoint() {
        // Создание точки
        PointEntity point = new PointEntity();
        point.setX(1.0);
        point.setY(2.0);
        pointRepository.save(point);

        // Поиск точки по ID
        PointEntity foundPoint = pointRepository.findById(point.getId()).orElse(null);
        assertThat(foundPoint).isNotNull();
        assertThat(foundPoint.getX()).isEqualTo(1.0);
        assertThat(foundPoint.getY()).isEqualTo(2.0);
    }

    @Test
    public void testUpdatePoint() {
        // Создание и сохранение точки
        PointEntity point = new PointEntity();
        point.setX(1.0);
        point.setY(2.0);
        pointRepository.save(point);

        // Обновление точки
        point.setX(3.0);
        point.setY(4.0);
        pointRepository.save(point);

        // Поиск обновленной точки
        PointEntity updatedPoint = pointRepository.findById(point.getId()).orElse(null);
        assertThat(updatedPoint).isNotNull();
        assertThat(updatedPoint.getX()).isEqualTo(3.0);
        assertThat(updatedPoint.getY()).isEqualTo(4.0);
    }

    @Test
    public void testDeletePoint() {
        // Создание точки
        PointEntity point = new PointEntity();
        point.setX(1.0);
        point.setY(2.0);
        pointRepository.save(point);

        // Удаление точки
        pointRepository.delete(point);

        // Проверка, что точка была удалена
        PointEntity foundPoint = pointRepository.findById(point.getId()).orElse(null);
        assertThat(foundPoint).isNull();
    }

    @Test
    public void testCreateAndFindMathFunction() {
        // Создание математической функции
        MathFunctionEntity function = new MathFunctionEntity();
        function.setHash(12345L);
        function.setName("Test Function");
        function.setCreatedAt(Instant.now());
        function.setUpdateAt(Instant.now());

        mathFunctionRepository.save(function);

        // Поиск математической функции
        MathFunctionEntity foundFunction = mathFunctionRepository.findById(Math.toIntExact(function.getHash())).orElse(null);
        assertThat(foundFunction).isNotNull();
        assertThat(foundFunction.getName()).isEqualTo("Test Function");
    }

    @Test
    public void testDeleteMathFunction() {
        // Создание математической функции
        MathFunctionEntity function = new MathFunctionEntity();
        function.setHash(12345L);
        function.setName("Test Function");

        mathFunctionRepository.save(function);

        // Удаление математической функции
        mathFunctionRepository.delete(function);

        // Проверка, что математическая функция была удалена
        MathFunctionEntity foundFunction = mathFunctionRepository.findById(Math.toIntExact(function.getHash())).orElse(null);
        assertThat(foundFunction).isNull();
    }
}
