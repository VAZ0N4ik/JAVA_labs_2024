package ru.ssau.tk.java_domination_339.java_labs_2024.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MathFunctionDto {

    @JsonProperty("hash_function")
    private Long hashFunction;

    @JsonProperty("countPoint")
    Integer countPoint;

    List<PointDto> points;

    @JsonProperty("created_at")
    Instant createdAt;

    @JsonProperty("update_at")
    Instant updateAt;

}