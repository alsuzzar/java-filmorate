package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/films")
public class FilmController {

    private final FilmService service;

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable @Positive Long id) {
        return service.getFilmById(id);
    }

    @GetMapping
    public Collection<Film> findAll() {
        return service.findAll();
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") @Positive Integer count) {
        return service.getMostPopularFilms(count);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return service.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        return service.updateFilm(newFilm);
    }

    @PutMapping("/{id}/like/{userId}") //пользователь ставит лайк фильму.
    public void likeFilm(@PathVariable @Positive Long id, @PathVariable @Positive Long userId) {
        service.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Map<String, String> removeLike(@PathVariable @Positive Long id, @PathVariable @Positive Long userId) {
        service.removeLike(id, userId);
        return Map.of("message", "ok");
    }
}

