package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();
    int MAX_LENGTH = 200;

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Создание фильма. Пустое название");
            throw new ConditionsNotMetException("Название не может быть пустым");
        }
        if (film.getDescription() == null || film.getDescription().isBlank()) {
            log.warn("Создание фильма. Пустое описание");
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        if (film.getDescription().length() >= MAX_LENGTH) {
            log.warn("Создание фильма. Описание слишком длинное");
            throw new ConditionsNotMetException("Описание слишком длинное");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("Создание фильма. Некорректная дата релиза");
            throw new ConditionsNotMetException("Некорректная дата релиза");
        }
        if (film.getDuration() == null || film.getDuration()<=0) {
            log.warn("Создание фильма. Некорректная продолжительность");
            throw new ConditionsNotMetException("Некорректная продолжительность");
        }

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

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
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
            if (newFilm.getName().isBlank()) {
                log.warn("Обновление фильма. Пустое название");
                throw new ConditionsNotMetException("Название не может быть пустым");
            }
        }

        if (newFilm.getDescription() != null) {
            if (newFilm.getDescription().isBlank() || newFilm.getDescription().length() > MAX_LENGTH) {
                log.warn("Обновление фильма. Некорректное описание");
                throw new ConditionsNotMetException("Некорректное описание");
            }
        }

        if (newFilm.getReleaseDate() != null) {
            if (newFilm.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
                log.warn("Обновление фильма. Некорректная дата релиза");
                throw new ConditionsNotMetException("Некорректная дата релиза");
            }
        }

        if (newFilm.getDuration() != null) {
            if (newFilm.getDuration()<=0) {
                log.warn("Обновление фильма. Некорректная продолжительность");
                throw new ConditionsNotMetException("Некорректная продолжительность");
            }
        }

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
}

