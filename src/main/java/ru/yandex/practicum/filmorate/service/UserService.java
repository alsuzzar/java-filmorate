package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage storage;

    public UserService(@Qualifier("userDbStorage") UserStorage storage) {
        this.storage = storage;
    }

    public User getUserById(Long id) {
        return storage.getUserById(id);
    }

    public Collection<User> findAll() {
        return storage.findAll();
    }

    public User createUser(User user) {
        return storage.createUser(user);
    }

    public User updateUser(User newUser) {
        return storage.updateUser(newUser);
    }

    public void addFriend(Long userId, Long friendId) {
        validateIds(userId, friendId);

        storage.getUserById(userId);
        storage.getUserById(friendId);
        List<Long> friends = storage.getFriendIds(userId);
        if (friends.contains(friendId)) {
            throw new ConditionsNotMetException("уже в друзьях");
        }
        storage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        validateIds(userId, friendId);
        storage.getUserById(userId);
        storage.getUserById(friendId);
        storage.removeFriend(userId, friendId);
    }

    public List<User> getCommonUsers(Long userId, Long otherUserId) {
        validateIds(userId, otherUserId);
        storage.getUserById(userId);
        storage.getUserById(otherUserId);
        List<Long> userFriends = storage.getFriendIds(userId);
        List<Long> otherFriends = storage.getFriendIds(otherUserId);

        userFriends.retainAll(otherFriends);

        List<User> result = new ArrayList<>();
        for (Long id : userFriends) {
            result.add(storage.getUserById(id));
        }
        return result;
    }

    public List<User> getFriendsList(Long userId) {
        if (userId == null) {
            throw new ConditionsNotMetException("пользователь user не указан");
        }
        storage.getUserById(userId);
        List<Long> friendsIds = storage.getFriendIds(userId);
        List<User> friendsList = new ArrayList<>();
        for (Long id : friendsIds) {
            User friend = storage.getUserById(id);
            friendsList.add(friend);
        }
        return friendsList;
    }

    public void validateIds(Long userId, Long friendId) {
        if (userId == null) {
            throw new ConditionsNotMetException("пользователь user не указан");
        }
        if (friendId == null) {
            throw new ConditionsNotMetException("пользователь friend не указан");
        }
        if (userId.equals(friendId)) {
            throw new ConditionsNotMetException("пользователь один и тот же");
        }
    }
}
