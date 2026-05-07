package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

@Repository("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    protected final JdbcTemplate jdbc;

    public FilmDbStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    protected final RowMapper<Film> mapper = (resultSet, rowNum) -> {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setDuration(resultSet.getInt("duration"));
        Date date = resultSet.getDate("release_date");
        if (date != null) {
            film.setReleaseDate(date.toLocalDate());
        } else {
            film.setReleaseDate(null);
        }
        RatingMPA mpa = new RatingMPA();
        mpa.setId(resultSet.getLong("mpa_id"));
        mpa.setName(resultSet.getString("mpa_name"));
        film.setMpa(mpa);
        return film;
    };

    private final RowMapper<Genre> genreMapper = (rs, rowNum) -> {
        Genre genre = new Genre();
        genre.setId(rs.getLong("id"));
        genre.setName(rs.getString("name"));
        return genre;
    };

    private static final String FIND_ALL_QUERY = """
                SELECT f.*,
                       m.id   AS mpa_id,
                       m.name AS mpa_name
                FROM films f
                JOIN rating_MPA m ON f.rating_MPA = m.id
            """;

    private static final String FIND_BY_ID_QUERY = """
                SELECT f.*,
                       m.id   AS mpa_id,
                       m.name AS mpa_name
                FROM films f
                JOIN rating_MPA m ON f.rating_MPA = m.id
                WHERE f.id = ?
            """;
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, duration, release_date, rating_MPA)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, duration = ?, rating_MPA = ?, " +
            "release_date = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String FIND_MPA_BY_ID =
            "SELECT * FROM rating_MPA WHERE id = ?";
    private static final String FIND_GENRE_BY_ID =
            "SELECT * FROM genres WHERE id = ?";

    public List<Film> findAll() {
        List<Film> films = jdbc.query(FIND_ALL_QUERY, mapper);

        for (Film film : films) {
            List<Genre> genres = jdbc.query(
                    """
                            SELECT g.*
                            FROM genres g
                            JOIN genre_films gf ON g.id = gf.genre_id
                            WHERE gf.film_id = ?
                            ORDER BY g.id
                            """,
                    genreMapper,
                    film.getId()
            );
            film.setGenres(new LinkedHashSet<>(genres));

            List<Long> likes = jdbc.query(
                    "SELECT user_id FROM likes WHERE film_id = ?",
                    (rs, rowNum) -> rs.getLong("user_id"),
                    film.getId()
            );
            film.setLikes(new HashSet<>(likes));
        }

        return films;
    }

    public Film getFilmById(Long id) {
        Film film = findOne(FIND_BY_ID_QUERY, id);

        List<Genre> genres = jdbc.query(
                """
                        SELECT g.*
                        FROM genres g
                        JOIN genre_films gf ON g.id = gf.genre_id
                        WHERE gf.film_id = ?
                        ORDER BY g.id
                        """,
                genreMapper,
                film.getId()
        );
        film.setGenres(new LinkedHashSet<>(genres));
        List<Long> likes = jdbc.query(
                "SELECT user_id FROM likes WHERE film_id = ?",
                (rs, rowNum) -> rs.getLong("user_id"),
                film.getId()
        );
        film.setLikes(new HashSet<>(likes));

        return film;
    }

    public Film createFilm(Film film) {
        Date date;
        if (film.getReleaseDate() != null) {
            date = Date.valueOf(film.getReleaseDate());
        } else {
            date = null;
        }
        if (film.getMpa() == null) {
            film.setMpa(new RatingMPA());
            film.getMpa().setId(1L);
        } else {
            checkIfMpaExists(film.getMpa().getId());
        }
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                date,
                film.getMpa().getId()
        );
        film.setId(id);
        if (film.getGenres() == null) {
            film.setGenres(new LinkedHashSet<>());
        } else {
            for (Genre genre : film.getGenres()) {
                checkIfGenreExists(genre.getId());
                jdbc.update(
                        "INSERT INTO genre_films (film_id, genre_id) VALUES (?, ?)",
                        film.getId(),
                        genre.getId()
                );
            }
        }
        return getFilmById(id);
    }

    public Film updateFilm(Film newFilm) {
        Film oldFilm = getFilmById(newFilm.getId());

        String name = newFilm.getName();
        if (name == null) {
            name = oldFilm.getName();
        }

        String description = newFilm.getDescription();
        if (description == null) {
            description = oldFilm.getDescription();
        }

        Integer duration = newFilm.getDuration();
        if (duration == null) {
            duration = oldFilm.getDuration();
        }

        LocalDate releaseDateLocal = newFilm.getReleaseDate();
        if (releaseDateLocal == null) {
            releaseDateLocal = oldFilm.getReleaseDate();
        }

        Date releaseDate = null;
        if (releaseDateLocal != null) {
            releaseDate = Date.valueOf(releaseDateLocal);
        }

        Long mpaId;
        if (newFilm.getMpa() != null) {
            mpaId = newFilm.getMpa().getId();
            checkIfMpaExists(mpaId);
        } else {
            mpaId = oldFilm.getMpa().getId();
        }

        int rows = jdbc.update(
                UPDATE_QUERY,
                name,
                description,
                duration,
                mpaId,
                releaseDate,
                newFilm.getId()
        );

        if (rows == 0) {
            throw new NotFoundException("Фильм не найден");
        }

        if (newFilm.getGenres() != null) {
            jdbc.update("DELETE FROM genre_films WHERE film_id = ?", newFilm.getId());

            for (Genre genre : newFilm.getGenres()) {
                checkIfGenreExists(genre.getId());
                jdbc.update(
                        "INSERT INTO genre_films (film_id, genre_id) VALUES (?, ?)",
                        newFilm.getId(),
                        genre.getId()
                );
            }
        }
        return getFilmById(newFilm.getId());
    }

    public void deleteFilm(Long id) {
        delete(DELETE_QUERY, id);
        jdbc.update("DELETE FROM genre_films WHERE film_id = ?", id);
    }

    public void likeFilm(Long filmId, Long userId) {
        jdbc.update(
                "INSERT INTO likes (film_id, user_id) VALUES (?, ?)",
                filmId,
                userId
        );
    }

    public void removeLike(Long filmId, Long userId) {
        jdbc.update(
                "DELETE FROM likes WHERE film_id = ? and user_id = ?",
                filmId,
                userId
        );
    }

    protected Film findOne(String query, Object... params) {
        try {
            return jdbc.queryForObject(query, mapper, params);
        } catch (DataAccessException e) {
            throw new NotFoundException("Нет записи");
        }
    }

    protected List<Film> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    protected void delete(String query, long id) {
        int rowsDeleted = jdbc.update(query, id);
        if (rowsDeleted == 0) {
            throw new RuntimeException("Не удалось удалить");
        }
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            throw new RuntimeException("Не удалось обновить данные");
        }
    }

    protected long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();

        if (key != null) {
            return key.longValue();
        } else {
            throw new RuntimeException("Не удалось сохранить данные");
        }
    }

    protected void checkIfMpaExists(Long id) {
        try {
            jdbc.queryForObject(
                    FIND_MPA_BY_ID,
                    (rs, rowNum) -> 1,
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("MPA не найден");
        }
    }

    protected void checkIfGenreExists(Long id) {
        try {
            jdbc.queryForObject(
                    FIND_GENRE_BY_ID,
                    (rs, rowNum) -> 1,
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр не найден");
        }
    }
}
