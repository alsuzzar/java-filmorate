package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.controller.ReleaseDateConstraint;

import java.time.LocalDate;

@Data
public class Film {
    private static final int MAX_LENGTH = 200;

    Long id;

    @NotBlank
    String name;

    @NotBlank
    @Size(max = MAX_LENGTH)
    String description;

    @ReleaseDateConstraint
    LocalDate releaseDate;

    @Positive
    @NotNull
    Integer duration;
}

