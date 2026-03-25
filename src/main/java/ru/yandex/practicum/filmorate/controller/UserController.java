package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @GetMapping
    public Collection<User> findAll() {
        return service.findAll();
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
        return service.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        return service.updateUser(newUser);
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
