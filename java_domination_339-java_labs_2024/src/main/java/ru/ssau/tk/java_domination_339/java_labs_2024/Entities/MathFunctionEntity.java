package ru.ssau.tk.java_domination_339.java_labs_2024.Entities;

import jakarta.persistence.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.Point;

import java.util.List;

@Entity
@Table(name = "functions")
public class MathFunctionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long functionId;

    @Column(name = "start")
    private double start;

    @Column(name = "end")
    private double end;


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Column(name = "count")
    private Integer count;

    @Column(name = "type")
    private String type;

    @Column(name = "is_comp_method")
    private boolean compMethod;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "functionEntity")
    private List<Point> points;


    public MathFunctionEntity(long function_id, double start, double end, String type, boolean compMethod, List<Point> points) {
        this.functionId = function_id;
        this.start = start;
        this.end = end;
        this.type = type;
        this.compMethod = compMethod;
        this.points = points;
    }

    public MathFunctionEntity() {
    }

    public long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(long function_id) {
        this.functionId = function_id;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getCompMethod() {
        return compMethod;
    }

    public void setCompMethod(boolean compMethod) {
        this.compMethod = compMethod;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
