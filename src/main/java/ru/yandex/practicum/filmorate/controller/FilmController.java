package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage storage;
    private final FilmService service;


    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        return storage.getFilmById(id);
    }

    @GetMapping
    public Collection<Film> findAll() {
        return storage.findAll();
    }

    @GetMapping("/popular") //возвращает список из первых count фильмов по количеству лайков.
    // Если значение параметра count не задано, верните первые 10
    public List<Film> getMostPopularFilms(@RequestParam(required = false) Integer count) {
        List<Film> all = service.getMostPopularFilms();
        if (count == null) {
            return all;
        }
        int limit = Math.min(all.size(), count);
        return new ArrayList<>(all.subList(0, limit));
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return storage.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        return storage.updateFilm(newFilm);
    }

    @PutMapping("/{id}/like/{userId}") //пользователь ставит лайк фильму.
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        service.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Map<String, String> removeLike(@PathVariable Long id, @PathVariable Long userId) {
        service.removeLike(id, userId);
        return Map.of("message", "ok");
    }
}

