package ru.yandex.practicum.filmorate.storage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User newUser);

    void deleteUser(Long id);

    Collection<User> findAll();

    User getUserById(Long id);

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<Long> getFriendIds(Long userId);
}

/* методы добавления, удаления и модификации объектов. */