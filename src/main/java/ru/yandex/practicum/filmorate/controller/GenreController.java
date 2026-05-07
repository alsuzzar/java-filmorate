package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable @Positive Long id) {
        return service.getGenreById(id);
    }

    @GetMapping
    public Collection<Genre> findAll() {
        return service.findAll();
    }

    @PostMapping
    public Genre createGenre(@Valid @RequestBody Genre genre) {
        return service.createGenre(genre);
    }

    @PutMapping
    public Genre updateGenre(@Valid @RequestBody Genre newGenre) {
        return service.updateGenre(newGenre);
    }
}
