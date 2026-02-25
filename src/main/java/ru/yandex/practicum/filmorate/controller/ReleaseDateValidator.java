package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateConstraint, LocalDate> {
    @Override
    public void initialize(ReleaseDateConstraint releaseDateConstraint) {
    }

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext cxt) {
        return releaseDate != null && releaseDate.isAfter(LocalDate.of(1895, 12, 28));
    }
}
