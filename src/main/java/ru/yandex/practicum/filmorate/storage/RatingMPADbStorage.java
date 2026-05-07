package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository("RatingMPADbStorage")
public class RatingMPADbStorage {
    protected final JdbcTemplate jdbc;

    public RatingMPADbStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    protected final RowMapper<RatingMPA> mapper = (resultSet, rowNum) -> {
        RatingMPA mpa = new RatingMPA();
        mpa.setId(resultSet.getLong("id"));
        mpa.setName(resultSet.getString("name"));
        return mpa;
    };

    private static final String FIND_ALL_QUERY = "SELECT * FROM Rating_MPA";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM Rating_MPA WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO Rating_MPA(name) VALUES (?)";
    private static final String DELETE_QUERY = "DELETE FROM Rating_MPA WHERE id = ?";

    public RatingMPA getRatingMPAById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public List<RatingMPA> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public RatingMPA createRatingMPA(RatingMPA ratingMPA) {
        long id = insert(
                INSERT_QUERY,
                ratingMPA.getName()
        );
        ratingMPA.setId(id);
        return ratingMPA;
    }

    public void deleteRatingMPA(Long id) {
        delete(DELETE_QUERY, id);
    }

    protected void delete(String query, long id) {
        int rowsDeleted = jdbc.update(query, id);
        if (rowsDeleted == 0) {
            throw new RuntimeException("Не удалось удалить");
        }
    }

    protected RatingMPA findOne(String query, Object... params) {
        try {
            return jdbc.queryForObject(query, mapper, params);
        } catch (DataAccessException e) {
            throw new NotFoundException("Нет записи");
        }
    }

    protected List<RatingMPA> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
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
