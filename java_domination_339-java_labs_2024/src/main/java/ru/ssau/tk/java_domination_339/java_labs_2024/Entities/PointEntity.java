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
    private MathFunctionEntity functionEntity;

    public PointEntity() {
    }

    public PointEntity(long pointId, double x, double y, MathFunctionEntity functionEntity) {
        this.pointId = pointId;
        this.x = x;
        this.y = y;
        this.functionEntity = functionEntity;
    }

    public long getId() {
        return pointId;
    }

    public void setId(long pointId) {
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

    public MathFunctionEntity getFunctionEntity() {
        return functionEntity;
    }

    public void setFunctionEntity(MathFunctionEntity functionEntity) {
        this.functionEntity = functionEntity;
    }
}
