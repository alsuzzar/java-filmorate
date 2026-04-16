package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

import static ru.yandex.practicum.filmorate.model.FriendshipStatus.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    private final UserStorage storage;

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

    public void addFriend(Long senderId, Long receiverId) {
        validateIds(senderId, receiverId);
        User sender = storage.getUserById(senderId);
        if (sender.getFriends().get(receiverId) == UNCONFIRMED) {
            throw new ConditionsNotMetException("заявка уже отправлена");
        }
        if (sender.getFriends().get(receiverId) == CONFIRMED) {
            throw new ConditionsNotMetException("пользователь уже в друзьях");
        }
        sender.addFriend(receiverId, UNCONFIRMED);
    }

    public void confirmFriendship(Long receiverId, Long senderId) {
        validateIds(receiverId, senderId);
        User receiver = storage.getUserById(receiverId);
        User sender = storage.getUserById(senderId);
        Map<Long, FriendshipStatus> friendshipList = sender.getFriends();

        if (!friendshipList.containsKey(receiverId)) {
            throw new NotFoundException("Заявка в друзья не найдена");
        }
        if (friendshipList.get(receiverId) == CONFIRMED) {
            throw new ConditionsNotMetException("Друг уже добавлен");
        }
        sender.addFriend(receiverId, CONFIRMED);
        receiver.addFriend(senderId, CONFIRMED);
    }

    public void removeFriend(Long userId, Long friendId) {
        validateIds(userId, friendId);
        User user = storage.getUserById(userId);
        User friend = storage.getUserById(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public List<User> getCommonUsers(Long userId, Long otherUserId) {
        validateIds(userId, otherUserId);
        List<User> commonFriends = new ArrayList<>();
        User user = storage.getUserById(userId);
        User otherUser = storage.getUserById(otherUserId);

        Set<Long> userConfirmedIds = new HashSet<>();
        Set<Long> otherUserConfirmedIds = new HashSet<>();
        for (Map.Entry<Long, FriendshipStatus> entry : user.getFriends().entrySet()) {
            if (entry.getValue() == CONFIRMED) {
                userConfirmedIds.add(entry.getKey());
            }
        }
        for (Map.Entry<Long, FriendshipStatus> entry : otherUser.getFriends().entrySet()) {
            if (entry.getValue() == CONFIRMED) {
                otherUserConfirmedIds.add(entry.getKey());
            }
        }
        userConfirmedIds.retainAll(otherUserConfirmedIds);

        for (Long id : userConfirmedIds) {
            User commonFriend = storage.getUserById(id);
            commonFriends.add(commonFriend);
        }
        return commonFriends;
    }

    public List<User> getFriendsList(Long userId) {
        if (userId == null) {
            throw new ConditionsNotMetException("пользователь user не указан");
        }
        Set<Long> friendsIds = storage.getUserById(userId).getFriends().keySet();
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
