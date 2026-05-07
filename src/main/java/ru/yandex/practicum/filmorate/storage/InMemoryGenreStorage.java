package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryGenreStorage implements GenreStorage {
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

    private long getNextGenreId() {
        long currentMaxFilmId = genres.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxFilmId;
    }

    public Collection<Genre> findAll() {
        return new ArrayList<>(genres.values());
    }

    public Genre createGenre(Genre genre) {
        genre.setId(getNextGenreId());
        genres.put(genre.getId(), genre);
        log.info("Жанр {} с id {} успешно добавлен", genre.getName(), genre.getId());
        return genre;
    }

    public Genre updateGenre(Genre newGenre) {
        if (newGenre.getId() == null) {
            log.warn("Id для обновления не указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (!genres.containsKey(newGenre.getId())) {
            log.warn("Фильм с id {} не найден", newGenre.getId());
            throw new NotFoundException("Фильм с id = " + newGenre.getId() + " не найден");
        }

        Genre oldGenre = genres.get(newGenre.getId());

        if (newGenre.getName() != null) {
            oldGenre.setName(newGenre.getName());
        }
        log.info("Жанр {} с id {} успешно обновлен", oldGenre.getName(), oldGenre.getId());
        return newGenre;
    }

    public void deleteGenre(Long id) {
        if (id == null) {
            throw new ConditionsNotMetException("id не указан");
        }
        if (!genres.containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        genres.remove(id);
    }
}
