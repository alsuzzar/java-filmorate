package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserStorage storage;
    private final UserService service;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return storage.getUserById(id);
    }

    @GetMapping
    public Collection<User> findAll() {
        return storage.findAll();
    }

    @GetMapping("/{id}/friends") //возвращаем список пользователей, являющихся его друзьями
    public List<User> getFriendsList(@PathVariable Long id) {
        return service.getFriendsList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}") //список друзей, общих с другим пользователем
    public Collection<User> getCommonUsers(@PathVariable Long id, @PathVariable Long otherId) {
        return service.getCommonUsers(id, otherId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return storage.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        return storage.updateUser(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}") // добавление в друзья
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}") // удаление из друзей
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        service.removeFriend(id, friendId);
    }
}
