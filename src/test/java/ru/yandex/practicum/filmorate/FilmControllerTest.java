package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    FilmStorage filmStorage;
    FilmService filmService;
    FilmController filmController;
    UserStorage userStorage;
    Collection<Film> testFilmList;

    @BeforeEach
    void setUp() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmStorage, filmService);
    }

    Film createTestFilm() {
        Film film = new Film();
        film.setName("Name1");
        film.setDescription("This is the film");
        film.setDuration(175);
        film.setReleaseDate(LocalDate.of(1991, 1, 1));
        filmController.createFilm(film);
        return film;
    }

    @Test
    void shouldCreateFilm() {
        Film film1 = new Film();
        film1.setName("Name1");
        film1.setDescription("This is the first film");
        film1.setDuration(175);
        film1.setReleaseDate(LocalDate.of(1991, 2, 2));

        Film film2 = new Film();
        film2.setName("Name2");
        film2.setDescription("This is the second film");
        film2.setDuration(120);
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));

        filmController.createFilm(film1);
        filmController.createFilm(film2);
        testFilmList = filmController.findAll();
        assertEquals(2, testFilmList.size());
        assertEquals(1, film1.getId());
        assertEquals(2, film2.getId());
        assertEquals("Name1", film1.getName());
        assertEquals("Name2", film2.getName());
    }

    @Test
    void shouldUpdateFilm() {
        Film film = createTestFilm();

        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("Name2");
        film2.setDescription("This is the updated film");
        film2.setDuration(120);
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));

        Film updatedFilm = filmController.updateFilm(film2);
        testFilmList = filmController.findAll();
        assertEquals(1, testFilmList.size());
        assertEquals(1, updatedFilm.getId());
        assertEquals("Name2", updatedFilm.getName());
        assertEquals("This is the updated film", updatedFilm.getDescription());
        assertEquals(120, updatedFilm.getDuration());
        assertEquals(LocalDate.of(2000, 1, 1), updatedFilm.getReleaseDate());
    }

    @Test
    void shouldHandleFilmWithIdForUpdateNotFound() {
        Film film = createTestFilm();

        Film film3 = new Film();
        film3.setId(2L);
        film3.setName("Name3");
        film3.setDescription("This is the third film");
        film3.setReleaseDate(LocalDate.of(2000, 1, 1));

        assertThrows(NotFoundException.class,
                () -> filmController.updateFilm(film3));

        testFilmList = filmController.findAll();
        assertEquals(1, testFilmList.size());
        assertEquals(1, film.getId());
        assertEquals("Name1", film.getName());
    }

    @Test
    void shouldHandleIdNotProvided() {
        Film film = createTestFilm();

        Film film3 = new Film();
        film3.setName("Name3");
        film3.setDescription("This is the third film");
        film3.setReleaseDate(LocalDate.of(2000, 1, 1));

        assertThrows(ConditionsNotMetException.class,
                () -> filmController.updateFilm(film3));

        testFilmList = filmController.findAll();
        assertEquals(1, testFilmList.size());
        assertEquals(1, film.getId());
        assertEquals("Name1", film.getName());
    }
}
