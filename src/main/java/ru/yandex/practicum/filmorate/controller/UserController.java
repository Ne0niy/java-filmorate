package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    //создание пользователя
    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Выполнение запроса на создание нового пользователя {}", user);

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Электронная почта не может быть пустой");
            throw new ConditionsNotMetException("Электронная почта не может быть пустой");
        }
        if (!user.getEmail().contains("@")) {
            log.error("Электронная почта должна содержать знак `@` ");
            throw new ConditionsNotMetException("Электронная почта должна содержать знак `@` ");
        }
        if (user.getLogin() != null &&(user.getLogin().isBlank() || user.getLogin().contains(" "))) {
            log.error("логин не может быть пустым и содержать пробелы");
            throw new ConditionsNotMetException("логин не может быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Некорректная дата");
            throw new ConditionsNotMetException("Некорректная дата");
        }

        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    //обновление пользователя
    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Выполнение запроса на изменение пользователя {}", user);
        if (user.getId() == null) {
            log.error("Id не может быть пустым");
            throw new ConditionsNotMetException("Id не может быть пустым");
        }
        if (!users.containsKey(user.getId())) {
            log.error("Такой Id не найден");
            throw new ConditionsNotMetException("Такой Id не найден");
        }
        User oldUser = users.get(user.getId());
        if (user.getEmail() != null && !user.getEmail().isBlank() && user.getEmail().contains("@")) {
            oldUser.setEmail(user.getEmail());
        }
        if (user.getEmail() != null && !user.getLogin().isBlank() && !user.getLogin().contains(" ")) {
            oldUser.setLogin(user.getLogin());
        }
        if (!user.getName().isBlank()) {
            oldUser.setName(user.getName());
        }
        if (user.getBirthday() != null && user.getBirthday().isBefore(LocalDate.now())) {
            oldUser.setBirthday(user.getBirthday());
        }

        return users.put(user.getId(), oldUser);
    }

    //получение списка всех пользователей
    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }


    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
