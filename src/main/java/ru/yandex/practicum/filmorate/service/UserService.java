package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    private final UserStorage storage;

    public void addFriend(Long userId, Long friendId) {
        validateIds(userId, friendId);
        User user = storage.getUserById(userId);
        User friend = storage.getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        validateIds(userId, friendId);
        User user = storage.getUserById(userId);
        User friend = storage.getUserById(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public List<User> getCommonUsers(Long userId, Long otherId) {
        validateIds(userId, otherId);
        List<User> commonFriends = new ArrayList<>();
        User user = storage.getUserById(userId);
        User friend = storage.getUserById(otherId);
        Set<Long> commonIds = new HashSet<>(user.getFriends());
        commonIds.retainAll(friend.getFriends());
        for (Long id : commonIds) {
            User commonFriend = storage.getUserById(id);
            commonFriends.add(commonFriend);
        }
        return commonFriends;
    }

    public List<User> getFriendsList(Long userId) {
        if (userId == null) {
            throw new ConditionsNotMetException("пользователь user не указан");
        }
        Set<Long> friendsIds = storage.getUserById(userId).getFriends();
        ArrayList<User> friendsList = new ArrayList<>();
        for (Long i : friendsIds) {
            User friend = storage.getUserById(i);
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
