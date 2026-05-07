package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.RatingMPAService;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/mpa")
public class RatingMPAController {
    private final RatingMPAService service;

    @GetMapping("/{id}")
    public RatingMPA getRatingMPAById(@PathVariable @Positive Long id) {
        return service.getRatingMPAById(id);
    }

    @GetMapping
    public Collection<RatingMPA> findAll() {
        return service.findAll();
    }

    @PostMapping
    public RatingMPA createRatingMPA(@Valid @RequestBody RatingMPA mpa) {
        return service.createRatingMPA(mpa);
    }
}
