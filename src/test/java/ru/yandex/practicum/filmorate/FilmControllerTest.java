package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    FilmController filmControllerTest;
    Collection<Film> testFilmList;

    @BeforeEach
    void setUp() {
        filmControllerTest = new FilmController();
    }

    Film createTestFilm() {
        Film film = new Film();
        film.setName("Name1");
        film.setDescription("This is the film");
        film.setDuration(175);
        film.setReleaseDate(LocalDate.of(1991, 1, 1));
        filmControllerTest.createFilm(film);
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

        filmControllerTest.createFilm(film1);
        filmControllerTest.createFilm(film2);
        testFilmList = filmControllerTest.findAll();
        assertEquals(2, testFilmList.size());
        assertEquals(1, film1.getId());
        assertEquals(2, film2.getId());
        assertEquals("Name1", film1.getName());
        assertEquals("Name2", film2.getName());
    }

    @Test
    void shouldHandleEmptyAndBlankNameOfFilm() {

        Film film1 = new Film();
        film1.setName("Name1");
        film1.setDescription("This is the first film");
        film1.setDuration(175);
        film1.setReleaseDate(LocalDate.of(1991, 2, 2));

        Film film2 = new Film();
        film2.setDescription("This is the second film");
        film2.setDuration(200);
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));

        Film film3 = new Film();
        film3.setName("");
        film3.setDescription("This is the third film");
        film3.setDuration(190);
        film3.setReleaseDate(LocalDate.of(2025, 2, 2));


        filmControllerTest.createFilm(film1);
        assertThrows(ConditionsNotMetException.class,
                () -> filmControllerTest.createFilm(film2));
        assertThrows(ConditionsNotMetException.class,
                () -> filmControllerTest.createFilm(film3));

        testFilmList = filmControllerTest.findAll();
        assertEquals(1, testFilmList.size());
        assertEquals(1, film1.getId());
        assertEquals("Name1", film1.getName());
    }

    @Test
    void shouldHandleEmptyAndBlankDescriptionOfFilm() {

        Film film1 = new Film();
        film1.setName("Name1");
        film1.setDescription("This is the first film");
        film1.setDuration(175);
        film1.setReleaseDate(LocalDate.of(1991, 2, 2));

        Film film2 = new Film();
        film2.setName("Name2");
        film2.setDuration(200);
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));

        Film film3 = new Film();
        film3.setName("Name3");
        film3.setDescription("");
        film3.setDuration(190);
        film3.setReleaseDate(LocalDate.of(2025, 2, 2));

        filmControllerTest.createFilm(film1);
        assertThrows(ConditionsNotMetException.class,
                () -> filmControllerTest.createFilm(film2));
        assertThrows(ConditionsNotMetException.class,
                () -> filmControllerTest.createFilm(film3));

        testFilmList = filmControllerTest.findAll();
        assertEquals(1, testFilmList.size());
        assertEquals(1, film1.getId());
        assertEquals("Name1", film1.getName());
    }

    @Test
    void shouldHandleTooLargeDescriptionOfFilm() {

        Film film1 = new Film();
        film1.setName("Name1");
        film1.setDescription("This is the first film");
        film1.setDuration(175);
        film1.setReleaseDate(LocalDate.of(1991, 2, 2));

        Film film2 = new Film();
        film2.setName("Name2");
        film2.setDescription ("This is the second film, and it is intentionally described in much greater detail " +
                        "to reach the required length, adding extra context, atmosphere, and narrative elements " +
                        "to make the description too long");
        film2.setDuration(200);
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));

        Film film3 = new Film();
        film3.setName("Name3");
        film3.setDescription("This is the third film");
        film3.setDuration(190);
        film3.setReleaseDate(LocalDate.of(2025, 2, 2));

        filmControllerTest.createFilm(film1);
        filmControllerTest.createFilm(film3);

        assertThrows(ConditionsNotMetException.class,
                () -> filmControllerTest.createFilm(film2));

        testFilmList = filmControllerTest.findAll();
        assertEquals(2, testFilmList.size());
        assertEquals(1, film1.getId());
        assertEquals("Name1", film1.getName());
        assertEquals(2, film3.getId());
        assertEquals("Name3", film3.getName());
    }

    @Test
    void shouldHandleIncorrectReleaseDateOfFilm() {
        Film film1 = new Film();
        film1.setName("Name1");
        film1.setDescription("This is the first film");
        film1.setDuration(175);
        film1.setReleaseDate(LocalDate.of(1991, 2, 2));

        Film film2 = new Film();
        film2.setName("Name2");
        film2.setDescription("This is the second film");
        film2.setDuration(120);
        film2.setReleaseDate(LocalDate.of(1500, 1, 1));

        Film film3 = new Film();
        film3.setName("Name3");
        film3.setDescription("This is the third film");
        film3.setDuration(190);

        filmControllerTest.createFilm(film1);

        assertThrows(ConditionsNotMetException.class,
                () -> filmControllerTest.createFilm(film2));
        assertThrows(ConditionsNotMetException.class,
                () -> filmControllerTest.createFilm(film3));

        testFilmList = filmControllerTest.findAll();
        assertEquals(1, testFilmList.size());
        assertEquals(1, film1.getId());
        assertEquals("Name1", film1.getName());
    }

    @Test
    void shouldHandleEmptyOrIncorrectDurationOfFilm() {
        Film film1 = new Film();
        film1.setName("Name1");
        film1.setDescription("This is the first film");
        film1.setDuration(175);
        film1.setReleaseDate(LocalDate.of(1991, 2, 2));

        Film film2 = new Film();
        film2.setName("Name2");
        film2.setDescription("This is the second film");
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));

        Film film3 = new Film();
        film3.setName("Name3");
        film3.setDescription("This is the third film");
        film3.setReleaseDate(LocalDate.of(2000, 1, 1));
        film3.setDuration(-190);

        filmControllerTest.createFilm(film1);

        assertThrows(ConditionsNotMetException.class,
                () -> filmControllerTest.createFilm(film2));
        assertThrows(ConditionsNotMetException.class,
                () -> filmControllerTest.createFilm(film3));

        testFilmList = filmControllerTest.findAll();
        assertEquals(1, testFilmList.size());
        assertEquals(1, film1.getId());
        assertEquals("Name1", film1.getName());
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

        Film updatedFilm = filmControllerTest.updateFilm(film2);
        testFilmList = filmControllerTest.findAll();
        assertEquals(1, testFilmList.size());
        assertEquals(1, updatedFilm.getId());
        assertEquals("Name2", updatedFilm.getName());
        assertEquals("This is the updated film", updatedFilm.getDescription());
        assertEquals(120, updatedFilm.getDuration());
        assertEquals(LocalDate.of(2000, 1, 1), updatedFilm.getReleaseDate());
    }

    @Test
    void shouldHandleBlankNameOfUpdatedFilm() {

        Film film = createTestFilm();

        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("");
        film2.setDescription("This is the updated film");
        film2.setDuration(120);
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));

        assertThrows(ConditionsNotMetException.class,
                () -> filmControllerTest.updateFilm(film2));

        testFilmList = filmControllerTest.findAll();
        assertEquals(1, testFilmList.size());
        assertEquals(1, film.getId());
        assertEquals("Name1", film.getName());
    }

    @Test
    void shouldHandleEmptyFieldsOfUpdatedFilm() {

        Film film = createTestFilm();

        Film film2 = new Film();
        film2.setId(1L);
        film2.setDescription("This is the updated film");
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));

        Film updatedFilm = filmControllerTest.updateFilm(film2);
        testFilmList = filmControllerTest.findAll();
        assertEquals(1, testFilmList.size());
        assertEquals(1, film.getId());
        assertEquals("Name1", updatedFilm.getName());
        assertEquals(175, updatedFilm.getDuration());
        assertEquals("This is the updated film", updatedFilm.getDescription());
        assertEquals(LocalDate.of(2000, 1, 1), updatedFilm.getReleaseDate());
    }

    @Test
    void shouldHandleBlankDescriptionOfFilm() {

        Film film = createTestFilm();

        Film film3 = new Film();
        film3.setId(1L);
        film3.setName("Name3");
        film3.setDescription("");
        film3.setDuration(190);
        film3.setReleaseDate(LocalDate.of(2025, 2, 2));

        assertThrows(ConditionsNotMetException.class,
                () -> filmControllerTest.updateFilm(film3));

        testFilmList = filmControllerTest.findAll();
        assertEquals(1, testFilmList.size());
        assertEquals(1, film.getId());
        assertEquals("Name1", film.getName());
    }

    @Test
    void shouldHandleTooLargeDescriptionOfUpdatedFilm() {

        Film film = createTestFilm();

        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("Name2");
        film2.setDescription ("This is the second film, and it is intentionally described in much greater detail " +
                        "to reach the required length, adding extra context, atmosphere, and narrative elements " +
                        "to make the description too long");
        film2.setDuration(200);
        film2.setReleaseDate(LocalDate.of(2000, 1, 1));

        assertThrows(ConditionsNotMetException.class,
                () -> filmControllerTest.updateFilm(film2));

        testFilmList = filmControllerTest.findAll();
        assertEquals(1, testFilmList.size());
        assertEquals(1, film.getId());
        assertEquals("Name1", film.getName());
    }

    @Test
    void shouldHandleIncorrectReleaseDateOfUpdatedFilm() {

        Film film = createTestFilm();

        Film film2 = new Film();
        film2.setId(1L);
        film2.setName("Name2");
        film2.setDescription("This is the second film");
        film2.setDuration(120);
        film2.setReleaseDate(LocalDate.of(1500, 1, 1));

        assertThrows(ConditionsNotMetException.class,
                () -> filmControllerTest.updateFilm(film2));

        testFilmList = filmControllerTest.findAll();
        assertEquals(1, testFilmList.size());
        assertEquals(1, film.getId());
        assertEquals("Name1", film.getName());
    }

    @Test
    void shouldHandleIncorrectDurationOfUpdatedFilm() {

        Film film = createTestFilm();

        Film film3 = new Film();
        film3.setId(1L);
        film3.setName("Name3");
        film3.setDescription("This is the third film");
        film3.setReleaseDate(LocalDate.of(2000, 1, 1));
        film3.setDuration(-190);

        assertThrows(ConditionsNotMetException.class,
                () -> filmControllerTest.updateFilm(film3));

        testFilmList = filmControllerTest.findAll();
        assertEquals(1, testFilmList.size());
        assertEquals(1, film.getId());
        assertEquals("Name1", film.getName());
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
                () -> filmControllerTest.updateFilm(film3));

        testFilmList = filmControllerTest.findAll();
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
                () -> filmControllerTest.updateFilm(film3));

        testFilmList = filmControllerTest.findAll();
        assertEquals(1, testFilmList.size());
        assertEquals(1, film.getId());
        assertEquals("Name1", film.getName());
    }
}
