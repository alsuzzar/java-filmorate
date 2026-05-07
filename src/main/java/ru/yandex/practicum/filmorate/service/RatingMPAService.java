package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.RatingMPADbStorage;

import java.util.Collection;

@Service
public class RatingMPAService {

    private final RatingMPADbStorage storage;

    public RatingMPAService(RatingMPADbStorage storage) {
        this.storage = storage;
    }

    public RatingMPA getRatingMPAById(Long id) {
        return storage.getRatingMPAById(id);
    }

    public Collection<RatingMPA> findAll() {
        return storage.findAll();
    }

    public RatingMPA createRatingMPA(RatingMPA mpa) {
        return storage.createRatingMPA(mpa);
    }
}
