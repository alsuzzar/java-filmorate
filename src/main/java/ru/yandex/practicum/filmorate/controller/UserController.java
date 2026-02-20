package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Создание пользователя. Некорректный логин");
            throw new ConditionsNotMetException("Некорректный логин");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Создание пользователя. Поле имейл пустое");
            throw new ConditionsNotMetException("Email не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Создание пользователя. Поле имейл не содержит @");
            throw new ConditionsNotMetException("Email должен содержать @");
        }

        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Создание пользователя. Некорректная дата рождения");
            throw new ConditionsNotMetException("Некорректная дата рождения");
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

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
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
            if (newUser.getLogin().isBlank()) {
                log.warn("Обновление. Пустой логин");
                throw new ConditionsNotMetException("Обновление. Пустой логин");
            }
            if (newUser.getLogin().contains(" ")) {
                log.warn("Обновление. Пробелы в логин");
                throw new ConditionsNotMetException("Обновление. Пробелы в логин");
            }
        }
        if (newUser.getEmail() != null) {
            if (newUser.getEmail().isBlank()) {
                log.warn("Обновление. Поле имейл пустое");
                throw new ConditionsNotMetException("Email не может быть пустым");
            }
            if (!newUser.getEmail().contains("@")) {
                log.warn("Обновление. Поле имейл не содержит @");
                throw new ConditionsNotMetException("Email должен содержать @");
            }
        }

        if (newUser.getBirthday() != null) {
            if (newUser.getBirthday().isAfter(LocalDate.now())) {
                log.warn("Обновление. Некорректная дата рождения");
                throw new ConditionsNotMetException("Обновление. Некорректная дата рождения");
            }
        }

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
}
