package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTest {

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @Test
    void shouldHandleValidUser() {
        User userValid = new User();
        userValid.setEmail("mail3@test.com");
        userValid.setLogin("login3");
        userValid.setName("Name3");
        userValid.setBirthday(LocalDate.of(1992, 3, 3));

        Set<ConstraintViolation<User>> validViolations = validator.validate(userValid);
        assertTrue(validViolations.isEmpty());
    }

    @Test
    void shouldHandleBlankEmail() {
        User user1 = new User();
        user1.setEmail("");
        user1.setLogin("login1");
        user1.setName("Name1");
        user1.setBirthday(LocalDate.of(1991, 2, 2));

        Set<ConstraintViolation<User>> violations1 = validator.validate(user1);
        assertFalse(violations1.isEmpty());
    }

    @Test
    void shouldHandleEmptyEmail() {
        User user2 = new User();
        user2.setLogin("login2");
        user2.setName("Name2");
        user2.setBirthday(LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations2 = validator.validate(user2);
        assertFalse(violations2.isEmpty());
    }

    @Test
    void shouldHandleEmailWithoutAt() {
        User user3 = new User();
        user3.setEmail("mail3_test.com");
        user3.setLogin("login4");
        user3.setName("Name4");
        user3.setBirthday(LocalDate.of(1993, 4, 4));

        Set<ConstraintViolation<User>> violations3 = validator.validate(user3);
        assertFalse(violations3.isEmpty());
    }

    @Test
    void shouldHandleLoginWithSpaces() {
        User user1 = new User();
        user1.setEmail("mail2@test.com");
        user1.setLogin("login2 ");
        user1.setName("Name2");
        user1.setBirthday(LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<User>> violations1 = validator.validate(user1);
        assertFalse(violations1.isEmpty());
    }

    @Test
        void shouldHandleBlankLogin() {
        User user2 = new User();
        user2.setEmail("mail3@test.com");
        user2.setLogin("");
        user2.setName("Name3");
        user2.setBirthday(LocalDate.of(1992, 3, 3));

        Set<ConstraintViolation<User>> violations2 = validator.validate(user2);
        assertFalse(violations2.isEmpty());
    }

    @Test
    void shouldHandleEmptyLogin() {
        User user3 = new User();
        user3.setEmail("mail3_test.com");
        user3.setName("Name4");
        user3.setBirthday(LocalDate.of(1993, 4, 4));

        Set<ConstraintViolation<User>> violations3 = validator.validate(user3);
        assertFalse(violations3.isEmpty());
    }

    @Test
    void shouldHandleEmptyBdayOfUser() {
        User user1 = new User();
        user1.setEmail("mail3@test.com");
        user1.setLogin("login3");
        user1.setName("Name3");

        Set<ConstraintViolation<User>> violations1 = validator.validate(user1);
        assertFalse(violations1.isEmpty());
    }

    @Test
    void shouldHandleBdayOfUserInFuture() {
        User user2 = new User();
        user2.setEmail("mail3@test.com");
        user2.setLogin("login4");
        user2.setName("Name4");
        user2.setBirthday(LocalDate.of(2028, 4, 4));

        Set<ConstraintViolation<User>> violations2 = validator.validate(user2);
        assertFalse(violations2.isEmpty());
    }
}