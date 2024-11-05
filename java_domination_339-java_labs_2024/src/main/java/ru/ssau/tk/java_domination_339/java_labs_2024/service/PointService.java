package ru.ssau.tk.java_domination_339.java_labs_2024.service;

import org.springframework.stereotype.Service;
import ru.ssau.tk.java_domination_339.java_labs_2024.DTOs.PointDTO;
import ru.ssau.tk.java_domination_339.java_labs_2024.Entities.MathFunctionEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.Entities.PointEntity;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.MathFunctionRepository;
import ru.ssau.tk.java_domination_339.java_labs_2024.repository.PointRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointService {

    private final PointRepository pointRepository;

    private final MathFunctionRepository mathFunctionRepository;

    public PointService(PointRepository pointRepository, MathFunctionRepository mathFunctionRepository) {
        this.pointRepository = pointRepository;
        this.mathFunctionRepository = mathFunctionRepository;
    }

    public List<PointDTO> findByFunctionEntity(int functionId) {
        MathFunctionEntity function = mathFunctionRepository.findById(functionId).orElse(null);
        if (function == null) {
            return null;
        }
        return this.pointRepository.findByFunctionEntity(function).stream().map(PointMapper::pointEntityToDTO).collect(Collectors.toList());
    }

    public PointDTO create(PointDTO pointDTO) {
        PointEntity point = PointMapper.pointDTOToEntity(pointDTO);
        PointEntity newPoint = pointRepository.save(point);
        return PointMapper.pointEntityToDTO(newPoint);
    }

    public PointDTO read(int id) {
        return pointRepository.findById(id).map(PointMapper::pointEntityToDTO).orElse(null);
    }

    public PointDTO update(PointDTO pointDTO) {
        PointEntity pointEntity = PointMapper.pointDTOToEntity(pointDTO);
        PointEntity editedPoint = pointRepository.save(pointEntity);

        return PointMapper.pointEntityToDTO(editedPoint);
    }

    public void delete(int id) {
        pointRepository.deleteById(id);
    }

}
