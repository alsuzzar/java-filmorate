package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
public class GenreService {

    private final GenreStorage storage;

    public GenreService(@Qualifier("genreDbStorage") GenreStorage storage) {
        this.storage = storage;
    }

    public Genre getGenreById(Long id) {
        return storage.getGenreById(id);
    }

    public Collection<Genre> findAll() {
        return storage.findAll();
    }

    public Genre createGenre(Genre genre) {
        return storage.createGenre(genre);
    }

    public Genre updateGenre(Genre newGenre) {
        return storage.updateGenre(newGenre);
    }
}
