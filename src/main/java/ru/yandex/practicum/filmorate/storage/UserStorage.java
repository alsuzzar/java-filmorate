package ru.yandex.practicum.filmorate.storage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User newUser);

    void deleteUser(Long id);

    Collection<User> findAll();

    User getUserById(Long id);
}

/* методы добавления, удаления и модификации объектов. */