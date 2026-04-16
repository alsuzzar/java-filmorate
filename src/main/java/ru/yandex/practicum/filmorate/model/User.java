package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class User {
    private Long id;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "\\S+")
    private String login;
    private String name;

    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    @Getter
    private Map<Long, FriendshipStatus> friends = new HashMap<>();

    public void addFriend(Long id, FriendshipStatus friendshipStatus) {
        friends.put(id, friendshipStatus);
    }

    public void removeFriend(Long id) {
        friends.remove(id);
    }
}
