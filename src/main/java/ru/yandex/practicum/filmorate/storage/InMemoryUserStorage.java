package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextUserId());
        users.put(user.getId(), user);
        log.info("Пользователь {} с id {} успешно добавлен", user.getName(), user.getId());
        return user;
    }

    private long getNextUserId() {
        long currentMaxUserId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxUserId;
    }

    public User updateUser(User newUser) {
        if (newUser.getId() == null) {
            log.warn("Id не указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (!users.containsKey(newUser.getId())) {
            log.warn("Пользователь с id {} не найден", newUser.getId());
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }

        User oldUser = users.get(newUser.getId());

        if (newUser.getLogin() != null) {
            oldUser.setLogin(newUser.getLogin());
        }
        if (newUser.getName() != null) {
            if (newUser.getName().isBlank()) {
                oldUser.setName(oldUser.getLogin());
            } else {
                oldUser.setName(newUser.getName());
            }
        }
        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getBirthday() != null) {
            oldUser.setBirthday(newUser.getBirthday());
        }
        log.info("Пользователь {} с id {} успешно обновлен", oldUser.getName(), oldUser.getId());
        return oldUser;
    }

    public Collection<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public void deleteUser(Long id) {
        if (id == null) {
            throw new ConditionsNotMetException("id не указан");
        }
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
       users.remove(id);
    }

    public User getUserById(Long id) {
        if (id == null) {
            throw new ConditionsNotMetException("id не указан");
        }
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return users.get(id);
    }
}
/*  перенесите туда всю логику хранения, обновления и поиска объектов.*/