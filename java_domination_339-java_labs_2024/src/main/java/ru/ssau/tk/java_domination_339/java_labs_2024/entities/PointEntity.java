package ru.ssau.tk.java_domination_339.java_labs_2024.entities;

import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "points")
public class PointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    Double x;

    Double y;

    public PointEntity(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

}
