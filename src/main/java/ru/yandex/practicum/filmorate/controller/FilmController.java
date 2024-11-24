package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    //добавление фильма
    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.info("Выполнение запроса на создание нового фильма {}", film);
        if (film.getName().isBlank()) {
            log.warn("Название не может быть пустым");
            throw new ConditionsNotMetException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Длина описания фильма не должна превышать 200 символов");
            throw new ConditionsNotMetException("Длина описания фильма не должна превышать 200 символов");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("дата релиза — должна быть не раньше 28 декабря 1895 года");
            throw new ConditionsNotMetException("дата релиза — должна быть не раньше 28 декабря 1895 года");
        }

        if (film.getDuration() < 1) {
            log.warn("продолжительность фильма должна быть положительным числом.");
            throw new ConditionsNotMetException("продолжительность фильма должна быть положительным числом.");
        }

        film.setId(getNextFilmId());
        films.put(film.getId(), film);
        return film;
    }

    //обновление фильма
    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Выполнение запроса на изменение фильма {}", film);
        if (film.getId() == null) {
            log.warn("Id не может быть пустым");
            throw new ConditionsNotMetException("Id не может быть пустым");
        }
        if (!films.containsKey(film.getId())) {
            log.warn("Такой Id фильма не найден");
            throw new ConditionsNotMetException("Такой Id фильма не найден");
        }
        Film oldFilm = films.get(film.getId());
        if (!film.getName().isBlank()) {
            oldFilm.setName(film.getName());
        }
        if (!film.getDescription().isBlank() && film.getDescription().length() <= 200) {
            oldFilm.setDescription(film.getDescription());
        }
        if (film.getReleaseDate() != null && !film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            oldFilm.setReleaseDate(film.getReleaseDate());
        }
        if (film.getDuration() > 0) {
            oldFilm.setDuration(film.getDuration());
        }

        return films.put(film.getId(), oldFilm);
    }

    //получение всех фильмов
    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }


    private long getNextFilmId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
