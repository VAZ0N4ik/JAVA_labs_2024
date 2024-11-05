package ru.ssau.tk.java_domination_339.java_labs_2024.DTOs;

import java.util.List;

public class MathFunctionDTO {
    private Long  functionId;
    private static String type;
    private Double start;
    private Double end;
    private Integer count;
    private boolean isCompMethod;
    List<PointDTO> pointsDTO;


    public MathFunctionDTO() {
    }

    public MathFunctionDTO(Long functionId, Double start, Double end, Integer count, boolean isCompMethod, List<PointDTO> pontsDTO) {
        this.functionId = functionId;
        this.start = start;
        this.end = end;
        this.count = count;
        this.isCompMethod = isCompMethod;
        this.pointsDTO = pontsDTO;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        MathFunctionDTO.type = type;
    }

    public Double getStart() {
        return start;
    }

    public void setStart(Double start) {
        this.start = start;
    }

    public Double getEnd() {
        return end;
    }

    public void setEnd(Double end) {
        this.end = end;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public boolean getCompMethod() {
        return isCompMethod;
    }

    public void setCompMethod(boolean compMethod) {
        isCompMethod = compMethod;
    }

    public List<PointDTO> getPointsDTO() {
        return pointsDTO;
    }

    public void setPointsDTO(List<PointDTO> pointsDTO) {
        this.pointsDTO = pointsDTO;
    }
}
