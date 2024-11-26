package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController filmController = new FilmController();

    @Test
    void whenCreateFilmNotValidName() {
        Film film = new Film(1L, null, "Описание", LocalDate.now(), 100);
        Assertions.assertThrows(
                ConditionsNotMetException.class,
                () -> filmController.createFilm(film),
                "Название не может быть пустым");
    }

    @Test
    void whenCreateFilmWhitNotValidDescription() {
        Film film = new Film(1L, "Utro",
                "Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять " +
                        "Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять Пять " +
                        "Пять Пять Пять 1", LocalDate.now(), 100);
        Assertions.assertThrows(
                ConditionsNotMetException.class,
                () -> filmController.createFilm(film),
                "Длина описания фильма не должна превышать 200 символов");
    }

    @Test
    void whenCreateFilmWhitNotValidCreateData() {
        Film film = new Film(1L, "Zara", "Описание",
                LocalDate.of(1895, 12, 27), 100);
        Assertions.assertThrows(
                ConditionsNotMetException.class,
                () -> filmController.createFilm(film),
                "дата релиза — должна быть не раньше 28 декабря 1895 года");
    }

    @Test
    void whenCreateFilmWhitNotValidDuration() {
        Film film = new Film(1L, "Zara", "Описание",
                LocalDate.of(1995, 12, 27), -100);
        Assertions.assertThrows(
                ConditionsNotMetException.class,
                () -> filmController.createFilm(film),
                "продолжительность фильма должна быть положительным числом.");
    }

    @Test
    void whenUpdateFilmWhitNullId() {
        Film film = new Film(null, "Zara", "Описание",
                LocalDate.of(1995, 12, 27), 100);
        Assertions.assertThrows(
                ConditionsNotMetException.class,
                () -> filmController.updateFilm(film),
                "Id не может быть пустым");
    }

    @Test
    void whenUpdateFilmWhitNotFoundId() {
        Film film = new Film(999L, "Zara", "Описание",
                LocalDate.of(1995, 12, 27), 100);
        Assertions.assertThrows(
                ConditionsNotMetException.class,
                () -> filmController.updateFilm(film),
                "Такой Id фильма не найден");
    }
}