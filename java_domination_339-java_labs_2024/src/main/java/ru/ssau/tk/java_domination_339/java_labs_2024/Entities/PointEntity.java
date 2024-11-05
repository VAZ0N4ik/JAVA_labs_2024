package ru.ssau.tk.java_domination_339.java_labs_2024.Entities;

import jakarta.persistence.*;

@Entity
@Table (name = "points")
public class PointEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long pointId;

    @Column (name = "x")
    private double x;

    @Column (name = "y")
    private double y;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "functionId")
    private Long functionEntity;

    public PointEntity() {
    }

    public PointEntity(long pointId, double x, double y, Long functionEntity) {
        this.pointId = pointId;
        this.x = x;
        this.y = y;
        this.functionEntity = functionEntity;
    }

    public long getPointId() {
        return pointId;
    }

    public void setPointId(long pointId) {
        this.pointId = pointId;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Long getFunctionEntity() {
        return functionEntity;
    }

    public void setFunctionEntity(Long functionEntity) {
        this.functionEntity = functionEntity;
    }
}
