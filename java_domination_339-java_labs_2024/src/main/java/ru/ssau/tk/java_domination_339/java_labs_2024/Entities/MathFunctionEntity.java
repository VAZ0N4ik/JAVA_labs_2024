package ru.ssau.tk.java_domination_339.java_labs_2024.Entities;

import jakarta.persistence.*;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.MathFunction;
import ru.ssau.tk.java_domination_339.java_labs_2024.functions.Point;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "functions")
public class MathFunctionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long functionId;

    //@Column(name = "function")
    //private MathFunction function;

    @Column(name = "start")
    private double start;

    @Column(name = "end")
    private double end;

    @Column(name = "type")
    private double type;

    @Column(name = "is_comp_method")
    private boolean comp_method;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "functionEntity")
    private List<Point> points;


    public MathFunctionEntity(long function_id, double start, double end, double type, boolean comp_method, List<Point> points) {
        this.functionId = function_id;
        this.start = start;
        this.end = end;
        this.type = type;
        this.comp_method = comp_method;
        this.points = points;
    }

    public MathFunctionEntity() {
    }

    public long getFunction_id() {
        return functionId;
    }

    public void setFunction_id(long function_id) {
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

    public double getType() {
        return type;
    }

    public void setType(double type) {
        this.type = type;
    }

    public boolean isComp_method() {
        return comp_method;
    }

    public void setComp_method(boolean comp_method) {
        this.comp_method = comp_method;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
