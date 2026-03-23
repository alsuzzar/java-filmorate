package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;

public interface FilmStorage {
    Film createFilm(Film film);
    Collection<Film> findAll();
    Film updateFilm(Film newFilm);
    void deleteFilm(Long id);
    Film getFilmById(Long id);
}
/* методы добавления, удаления и модификации объектов. */