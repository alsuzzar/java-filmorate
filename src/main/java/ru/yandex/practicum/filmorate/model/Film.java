package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.controller.ReleaseDateConstraint;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film implements Comparable<Film> {
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

    private Set<Long> likes = new HashSet<>();
    public void likeFilm(Long userId) {
        likes.add(userId);
    }

    public void removeLike(Long userId) {
        likes.remove(userId);
    }

    @Override
    public int compareTo(Film otherFilm) {
        return Integer.compare(otherFilm.likes.size(), this.likes.size());
    }
}

