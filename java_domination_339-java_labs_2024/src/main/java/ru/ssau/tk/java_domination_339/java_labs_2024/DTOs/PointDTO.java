package ru.ssau.tk.java_domination_339.java_labs_2024.DTOs;

public class PointDTO {
    private Long pointId;
    private Long functionEntity;
    private Double x;
    private Double y;

    public PointDTO() {
    }

    public PointDTO(Long pointId, Long functionEntity, Double x, Double y) {
        this.pointId = pointId;
        this.functionEntity = functionEntity;
        this.x = x;
        this.y = y;
    }

    public Long getPointId() {
        return pointId;
    }

    public void setPointId(Long pointId) {
        this.pointId = pointId;
    }

    public Long getFunctionEntity() {
        return functionEntity;
    }

    public void setFunctionEntity(Long functionEntity) {
        this.functionEntity = functionEntity;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
