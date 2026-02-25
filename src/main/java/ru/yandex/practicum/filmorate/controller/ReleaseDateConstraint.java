package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

    @Constraint(validatedBy = ReleaseDateValidator.class)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ReleaseDateConstraint {
        String message() default "Incorrect ReleaseDate";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

