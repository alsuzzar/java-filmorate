package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class FilmService {

    private final FilmStorage storage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage storage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("genreDbStorage") GenreStorage genreStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
    }

    public Film getFilmById(Long id) {
      return storage.getFilmById(id);
    }

    public Collection<Film> findAll() {
        return storage.findAll();
    }

    public Film createFilm(Film film) {
        Set<Genre> incomingGenres = film.getGenres();
        Set<Genre> validatedGenres = new HashSet<>();

        if (incomingGenres != null) {
            for (Genre genre : incomingGenres) {
                if (genre == null) {
                    continue;
                }
                Long id = genre.getId();
                if (id == null) {
                    throw new ConditionsNotMetException("id в списке жанров нет");
                }
                validatedGenres.add(genreStorage.getGenreById(id));
            }
        }
        film.setGenres(validatedGenres);
        RatingMPA mpa = film.getMpa();

        if (mpa == null) {
            throw new ConditionsNotMetException("рейтинг не указан");
        }
        film.setMpa(mpa);
        return storage.createFilm(film);
        }

    public Film updateFilm(Film newFilm) {
        Set<Genre> incomingGenres = newFilm.getGenres();
        Set<Genre> validatedGenres = new HashSet<>();

        if (incomingGenres != null) {
            for (Genre genre : incomingGenres) {
                if (genre == null) {
                    continue;
                }
                Long id = genre.getId();
                if (id == null) {
                    throw new ConditionsNotMetException("id в списке жанров нет");
                }
                validatedGenres.add(genreStorage.getGenreById(id));
            }
        }
        newFilm.setGenres(validatedGenres);
        RatingMPA mpa = newFilm.getMpa();

        if (mpa == null) {
            throw new ConditionsNotMetException("рейтинг не указан");
        }
        newFilm.setMpa(mpa);
        return storage.updateFilm(newFilm);
    }

    public void likeFilm(Long filmId, Long userId) {
        storage.likeFilm(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        storage.removeLike(filmId, userId);
    }

    public List<Film> getMostPopularFilms(Integer count) {
        List<Film> all = new ArrayList<>(storage.findAll());
        all.sort(null);
        int limit = Math.min(all.size(), count);
        return new ArrayList<>(all.subList(0, limit));
    }
}