package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Long, Film> films = new HashMap<>();

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film createFilm(Film film) {
        film.setId(getNextFilmId());
        films.put(film.getId(), film);
        log.info("Фильм {} с id {} успешно добавлен", film.getName(), film.getId());
        return film;
    }

    private long getNextFilmId() {
        long currentMaxFilmId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxFilmId;
    }

    public Film updateFilm(Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("Id для обновления не указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (!films.containsKey(newFilm.getId())) {
            log.warn("Фильм с id {} не найден", newFilm.getId());
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }

        Film oldFilm = films.get(newFilm.getId());

        if (newFilm.getName() != null) {
            oldFilm.setName(newFilm.getName());
        }
        if (newFilm.getDescription() != null) {
            oldFilm.setDescription(newFilm.getDescription());
        }
        if (newFilm.getReleaseDate() != null) {
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }
        if (newFilm.getDuration() != null) {
            oldFilm.setDuration(newFilm.getDuration());
        }
        log.info("Фильм {} с id {} успешно обновлен", oldFilm.getName(), oldFilm.getId());
        return oldFilm;
    }

    public void deleteFilm(Long id) {
        if (id==null) {
            throw new ConditionsNotMetException("id не указан");
        }
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        films.remove(id);
    }

    public Film getFilmById(Long id) {
        if (id==null) {
            throw new ConditionsNotMetException("id не указан");
        }
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        return films.get(id);
    }
}


/*  перенесите туда всю логику хранения, обновления и поиска объектов.  */