package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@RequiredArgsConstructor
@Service
public class FilmService {

    private final FilmStorage storage;
    private final UserStorage userStorage;

    public void likeFilm(Long filmId, Long userId) {
        validateId(userId, filmId);
        userStorage.getUserById(userId);
        Film film = storage.getFilmById(filmId);
        film.likeFilm(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        validateId(filmId, userId);
        userStorage.getUserById(userId);
        Film film = storage.getFilmById(filmId);
        film.removeLike(userId);
    }

    public void validateId(Long userId, Long filmId) {
        if (filmId == null) {
            throw new ConditionsNotMetException("id фильма не указан");
        }
        if (userId == null) {
            throw new ConditionsNotMetException("id пользователя не указан");
        }

    }

    public List<Film> getMostPopularFilms() {
        List<Film> all = new ArrayList<>(storage.findAll());
        List<Film> mostPopular = new ArrayList<>();
        Collections.sort(all);
        for (int i = 0; i < all.size() && i < 10; i++) {
            mostPopular.add(all.get(i));
        }
        return mostPopular;
    }
}