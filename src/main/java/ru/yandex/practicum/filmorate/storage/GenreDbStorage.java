package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class GenreDbStorage implements GenreStorage {

    protected final JdbcTemplate jdbc;

    public GenreDbStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    protected final RowMapper<Genre> genreMapper = (resultSet, rowNum) -> {
        Genre genre = new Genre();
        genre.setId(resultSet.getLong("id"));
        genre.setName(resultSet.getString("name"));
        return genre;
    };

    private static final String FIND_ALL_QUERY = "SELECT * FROM genres ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO genres(name) VALUES (?)";
    private static final String UPDATE_QUERY = "UPDATE genres SET name = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM genres WHERE id = ?";

    public Genre getGenreById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public List<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    protected Genre findOne(String query, Object... params) {
        try {
            return jdbc.queryForObject(query, genreMapper, params);
        } catch (DataAccessException e) {
            throw new NotFoundException("Нет записи");
        }
    }

    protected List<Genre> findMany(String query, Object... params) {
        return jdbc.query(query, genreMapper, params);
    }

    public Genre createGenre(Genre genre) {
        long id = insert(
                INSERT_QUERY,
                genre.getName()
        );
        genre.setId(id);
        return genre;
    }

    public Genre updateGenre(Genre newGenre) {
        update(
                UPDATE_QUERY,
                newGenre.getName(),
                newGenre.getId()
        );
        return newGenre;
    }

    public void deleteGenre(Long id) {
        delete(DELETE_QUERY, id);
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
}
