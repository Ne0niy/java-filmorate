package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController userController = new UserController();

    @Test
    void whenCreateUserWhitNullEmail() {
        User user = new User(1L, null, "777Ultra", "Egor", LocalDate.of(1999,12,12));
        Assertions.assertThrows(
                ConditionsNotMetException.class,
                () -> userController.createUser(user),
                "Электронная почта не может быть пустой");
    }

    @Test
    void whenCreateUserWhitDogSign() {
        User user = new User(1L, "superEmal", "777Ultra", "Egor", LocalDate.of(1999,12,12));
        Assertions.assertThrows(
                ConditionsNotMetException.class,
                () -> userController.createUser(user),
                "Электронная почта должна содержать знак `@` ");
    }

    @Test
    void whenCreateUserWhitNotValidLogin() {
        User user = new User(1L, "super@Emal", "  ", "Egor", LocalDate.of(1999,12,12));
        Assertions.assertThrows(
                ConditionsNotMetException.class,
                () -> userController.createUser(user),
                "логин не может быть пустым и содержать пробелы");
    }

    @Test
    void whenCreateUserWhitNotValidBirthday() {
        User user = new User(1L, "super@Emal", "Ultra", "Egor", LocalDate.now().plusMonths(1));
        Assertions.assertThrows(
                ConditionsNotMetException.class,
                () -> userController.createUser(user),
                "Некорректная дата");
    }

    @Test
    void whenUpdateUserWhitNotValidId() {
        User user = new User(null, "super@Emal", "Ultra", "Egor", LocalDate.of(1999,12,12));
        Assertions.assertThrows(
                ConditionsNotMetException.class,
                () -> userController.updateUser(user),
                "Id не может быть пустым");
    }

    @Test
    void whenUpdateUserWhitNotZoneId() {
        User user = new User(999L, "super@Emal", "Ultra", "Egor", LocalDate.of(1999,12,12));
        Assertions.assertThrows(
                ConditionsNotMetException.class,
                () -> userController.updateUser(user),
                "Такой Id не найден");
    }

}