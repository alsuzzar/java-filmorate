package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    Long id;

    @Email
    @NotBlank
    String email;

    @NotBlank
    @Pattern(regexp = "\\S+")
    String login;
    String name;

    @NotNull
    @PastOrPresent
    LocalDate birthday;
}
