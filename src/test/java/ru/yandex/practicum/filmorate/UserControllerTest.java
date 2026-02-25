package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    UserController userControllerTest;
    Collection<User> testUserList;

    User createTestUser() {
        User user = new User();
        user.setName("Name1");
        user.setEmail("mail1@test.com");
        user.setLogin("login1");
        user.setName("Name1");
        user.setBirthday(LocalDate.of(1991, 1, 1));
        userControllerTest.createUser(user);
        return user;
    }

    @BeforeEach
    void setUp() {
        userControllerTest = new UserController();
    }

    @Test
    void shouldCreateUser() {
        User user1 = new User();
        user1.setEmail("mail1@test.com");
        user1.setLogin("login1");
        user1.setName("Name1");
        user1.setBirthday(LocalDate.of(1991, 2, 2));

        User user2 = new User();
        user2.setEmail("mail2@test.com");
        user2.setLogin("login2");
        user2.setName("Name2");
        user2.setBirthday(LocalDate.of(1990, 1, 1));

        userControllerTest.createUser(user1);
        userControllerTest.createUser(user2);
        testUserList = userControllerTest.findAll();
        assertEquals(2, testUserList.size());
        assertEquals(1, user1.getId());
        assertEquals(2, user2.getId());
        assertEquals("Name1", user1.getName());
        assertEquals("Name2", user2.getName());
    }

    @Test
    void shouldHandleEmptyAndBlankNameofUser() {
        User user1 = new User();
        user1.setEmail("mail1@test.com");
        user1.setLogin("login1");
        user1.setName("");
        user1.setBirthday(LocalDate.of(1991, 2, 2));

        User user2 = new User();
        user2.setEmail("mail2@test.com");
        user2.setLogin("login2");
        user2.setBirthday(LocalDate.of(1990, 1, 1));

        User user3 = new User();
        user3.setEmail("mail3@test.com");
        user3.setLogin("login3");
        user3.setName("Name3");
        user3.setBirthday(LocalDate.of(1992, 3, 3));

        userControllerTest.createUser(user1);
        userControllerTest.createUser(user2);
        userControllerTest.createUser(user3);
        testUserList = userControllerTest.findAll();
        assertEquals(3, testUserList.size());
        assertEquals(1, user1.getId());
        assertEquals(2, user2.getId());
        assertEquals(3, user3.getId());
        assertEquals("login1", user1.getName());
        assertEquals("login2", user2.getName());
        assertEquals("Name3", user3.getName());
    }

    @Test
    void shouldUpdateUser() {
        User user = createTestUser();

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("mail2@test.com");
        user2.setLogin("login2");
        user2.setName("Name2");
        user2.setBirthday(LocalDate.of(2000, 1, 1));

        User updatedUser = userControllerTest.updateUser(user2);
        testUserList = userControllerTest.findAll();
        assertEquals(1, testUserList.size());
        assertEquals(1, updatedUser.getId());
        assertEquals("Name2", updatedUser.getName());
        assertEquals("mail2@test.com", updatedUser.getEmail());
        assertEquals("login2", updatedUser.getLogin());
        assertEquals(LocalDate.of(2000, 1, 1), updatedUser.getBirthday());
    }

    @Test
    void shouldHandleUserWithIdForUpdateNotFound() {
        User user = createTestUser();

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("mail2@test.com");
        user2.setLogin("login2");
        user2.setName("Name2");
        user2.setBirthday(LocalDate.of(2000, 1, 1));

        assertThrows(NotFoundException.class,
                () -> userControllerTest.updateUser(user2));

        testUserList = userControllerTest.findAll();
        assertEquals(1, testUserList.size());
        assertEquals(1, user.getId());
        assertEquals("Name1", user.getName());
    }

    @Test
    void shouldHandleIdNotProvided() {
        User user = createTestUser();

        User user2 = new User();
        user2.setEmail("mail2@test.com");
        user2.setLogin("login2");
        user2.setName("Name2");
        user2.setBirthday(LocalDate.of(2000, 1, 1));

        assertThrows(ConditionsNotMetException.class,
                () -> userControllerTest.updateUser(user2));

        testUserList = userControllerTest.findAll();
        assertEquals(1, testUserList.size());
        assertEquals(1, user.getId());
        assertEquals("Name1", user.getName());
    }

    @Test
    void shouldHandleEmptyFieldsOfUpdatedUser() {
        User user = createTestUser();

        User user2 = new User();
        user2.setId(1L);
        user2.setLogin("login2");
        user2.setName("Name2");

        User updatedUser = userControllerTest.updateUser(user2);
        testUserList = userControllerTest.findAll();
        assertEquals(1, testUserList.size());
        assertEquals(1, updatedUser.getId());
        assertEquals("Name2", updatedUser.getName());
        assertEquals("mail1@test.com", updatedUser.getEmail());
        assertEquals("login2", updatedUser.getLogin());
        assertEquals(LocalDate.of(1991, 1, 1), updatedUser.getBirthday());
    }

    @Test
    void shouldHandleBlankNameOfUpdatedUser() {
        User user = createTestUser();

        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("mail2@test.com");
        user2.setLogin("login2");
        user2.setName("");
        user2.setBirthday(LocalDate.of(2000, 1, 1));

        User updatedUser = userControllerTest.updateUser(user2);
        testUserList = userControllerTest.findAll();
        assertEquals(1, testUserList.size());
        assertEquals(1, updatedUser.getId());
        assertEquals("login2", updatedUser.getName());
        assertEquals("mail2@test.com", updatedUser.getEmail());
        assertEquals("login2", updatedUser.getLogin());
        assertEquals(LocalDate.of(2000, 1, 1), updatedUser.getBirthday());
    }
}
