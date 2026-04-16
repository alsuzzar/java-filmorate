package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;

@Getter
@AllArgsConstructor
public enum RatingMPA {
    G(1, "G"),
    PG(2, "PG"),
    PG_13(3, "PG-13"),
    R(4, "R"),
    NC_17(5, "NC-17");

    private final int id;
    private final String name;

    public static RatingMPA fromId(int id) {
        for (RatingMPA rating : values()) {
            if (rating.id == id) {
                return rating;
            }
        }
        throw new ConditionsNotMetException("Рейтинг MPA с id=" + id + " не найден");
    }
}
