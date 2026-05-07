package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository("userDbStorage")
public class UserDbStorage implements UserStorage {

    protected final JdbcTemplate jdbc;

    public UserDbStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    protected final RowMapper<User> mapper = (resultSet, rowNum) -> {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        Date date = resultSet.getDate("birthday");
        if (date != null) {
            user.setBirthday(date.toLocalDate());
        } else {
            user.setBirthday(null);
        }
        return user;
    };

    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(name, email, login, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET name = ?, email = ?, login = ?, birthday = ? " +
            "WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";

    private static final String FIND_ALL_FRIENDS_QUERY = "SELECT friend_id FROM friendships WHERE user_id = ?";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friendships (user_id, friend_id) VALUES (?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";

    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public User getUserById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public User createUser(User user) {
        Date date;
        if (user.getBirthday() != null) {
            date = Date.valueOf(user.getBirthday());
        } else {
            date = null;
        }
        long id = insert(
                INSERT_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                date
        );
        user.setId(id);
        return user;
    }

    public User updateUser(User newUser) {
        Date date;
        if (newUser.getBirthday() != null) {
            date = Date.valueOf(newUser.getBirthday());
        } else {
            date = null;
        }
        update(
                UPDATE_QUERY,
                newUser.getName(),
                newUser.getEmail(),
                newUser.getLogin(),
                date,
                newUser.getId()
        );
        return newUser;
    }

    public void deleteUser(Long id) {
        delete(DELETE_QUERY, id);
    }

    public void addFriend(Long userId, Long friendId) {
        insertFriend(INSERT_FRIEND_QUERY, userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        jdbc.update(DELETE_FRIEND_QUERY, userId, friendId);
    }

    public List<Long> getFriendIds(Long userId) {
        return findIds(FIND_ALL_FRIENDS_QUERY, userId);
    }

    protected User findOne(String query, Object... params) {
        try {
            return jdbc.queryForObject(query, mapper, params);
        } catch (DataAccessException e) {
            throw new NotFoundException("Нет записи");
        }
    }

    protected List<User> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            throw new NotFoundException("Не удалось");
        }
    }

    protected void delete(String query, Object... params) {
        int rowsDeleted = jdbc.update(query, params);
        if (rowsDeleted == 0) {
            throw new NotFoundException("Не удалось");
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

    protected void insertFriend(String query, long firstId, long secondId) {
        try {
            jdbc.update(query, firstId, secondId);
        } catch (DataIntegrityViolationException e) {
            throw new ConditionsNotMetException("Связь уже существует или пользователь не найден");
        }
    }

    protected List<Long> findIds(String query, Object... params) {
        return jdbc.query(
                query,
                (rs, rowNum) -> rs.getLong(1),
                params
        );
    }
}

