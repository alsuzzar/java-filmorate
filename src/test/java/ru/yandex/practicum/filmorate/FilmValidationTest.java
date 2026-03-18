package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmValidationTest {

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @Test
    void shouldHandleValidFilm() {

        Film filmValid = new Film();
        filmValid.setName("Name1");
        filmValid.setDescription("This is the first film");
        filmValid.setDuration(175);
        filmValid.setReleaseDate(LocalDate.of(1991, 2, 2));

        Set<ConstraintViolation<Film>> validViolations = validator.validate(filmValid);
        assertTrue(validViolations.isEmpty());
    }

    @Test
    void shouldHandleBlankNameOfFilm() {
        Film film1 = new Film();
        film1.setName("");
        film1.setDescription("This is the third film");
        film1.setDuration(190);
        film1.setReleaseDate(LocalDate.of(2025, 2, 2));

        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldHandleEmptyNameOfFilm() {
        Film film2 = new Film();
        film2.setDescription("This is the second film");
        film2.setDuration(200);
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film2);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldHandleEmptyDescriptionOfFilm() {
        Film film2 = new Film();
        film2.setName("Name2");
        film2.setDuration(200);
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film2);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldHandleBlankDescriptionOfFilm() {
        Film film3 = new Film();
        film3.setName("Name3");
        film3.setDescription("");
        film3.setDuration(190);
        film3.setReleaseDate(LocalDate.of(2025, 2, 2));

        Set<ConstraintViolation<Film>> violations = validator.validate(film3);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldHandleTooLargeDescriptionOfFilm() {
        Film film2 = new Film();
        film2.setName("Name2");
        film2.setDescription("This is the second film, and it is intentionally described in much greater detail " +
                "to reach the required length, adding extra context, atmosphere, and narrative elements " +
                "to make the description too long");
        film2.setDuration(200);
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film2);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldHandleEmptyDurationOfFilm() {
        Film film2 = new Film();
        film2.setName("Name2");
        film2.setDescription("This is the second film");
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film2);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldHandleIncorrectDurationOfFilm() {
        Film film3 = new Film();
        film3.setName("Name3");
        film3.setDescription("This is the third film");
        film3.setReleaseDate(LocalDate.of(2000, 1, 1));
        film3.setDuration(-190);

        Set<ConstraintViolation<Film>> violations = validator.validate(film3);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldHandleEmptyFieldsOfFilm() {
        Film film2 = new Film();
        film2.setId(1L);
        film2.setDescription("This is the updated film");
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film2);
        assertEquals(2, violations.size());
    }

    @Test
    void shouldHandleIncorrectReleaseDateOfFilm() {
        Film film = new Film();
        film.setName("Name1");
        film.setDescription("This is the first film");
        film.setDuration(175);
        film.setReleaseDate(LocalDate.of(1894, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
    }
}
