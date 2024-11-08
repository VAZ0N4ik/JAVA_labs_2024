package ru.ssau.tk.java_domination_339.java_labs_2024.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "functions")
public class MathFunctionEntity {

    @Id
    @Column(name = "hash_id")
    Long hash;

    String name;

    @OneToMany(fetch = FetchType.EAGER)
    List<PointEntity> points;

    @Column(name = "created_at")
    Instant createdAt;

    @Column(name = "modified_at")
    Instant updateAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updateAt = Instant.now();
    }

}
