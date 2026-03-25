package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@RequiredArgsConstructor
@Service
public class FilmService {

    private final FilmStorage storage;
    private final UserStorage userStorage;

    public Film getFilmById(Long id) {
      return storage.getFilmById(id);
    }

    public Collection<Film> findAll() {
        return storage.findAll();
    }

    public Film createFilm(Film film) {
        return storage.createFilm(film);
    }

    public Film updateFilm(Film newFilm) {
        return storage.updateFilm(newFilm);
    }

    public void likeFilm(Long filmId, Long userId) {
        userStorage.getUserById(userId);
        Film film = storage.getFilmById(filmId);
        film.likeFilm(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        userStorage.getUserById(userId);
        Film film = storage.getFilmById(filmId);
        film.removeLike(userId);
    }

    public List<Film> getMostPopularFilms(Integer count) {
        List<Film> all = new ArrayList<>(storage.findAll());
        all.sort(null);
        int limit = Math.min(all.size(), count);
        return new ArrayList<>(all.subList(0, limit));
    }
}