package ru.yandex.practicum.filmorate.storage;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryGenreStorage implements GenreStorage{
    private final Map<Long, Genre> genres = new HashMap<>();

    public Genre getGenreById(Long id) {
        if (id == null) {
            throw new ConditionsNotMetException("id не указан");
        }
        if (!genres.containsKey(id)) {
            throw new NotFoundException("Жанр не найден");
        }
        return genres.get(id);
    }

    public Collection<Genre> findAll() {
        return new ArrayList<>(genres.values());
    }

    @PostConstruct
    public void init() {
        Genre comedy = new Genre(1L, "Комедия");
        Genre drama = new Genre(2L, "Драма");
        Genre cartoon = new Genre(3L, "Мультфильм");
        Genre thriller = new Genre(4L, "Триллер");
        Genre doku = new Genre(5L, "Документальный");
        Genre action = new Genre(6L, "Боевик");

        genres.put(1L, comedy);
        genres.put(2L, drama);
        genres.put(3L, cartoon);
        genres.put(4L, thriller);
        genres.put(5L, doku);
        genres.put(6L, action);
    }
}
