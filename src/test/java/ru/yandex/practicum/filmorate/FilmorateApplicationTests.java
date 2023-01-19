package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;

    @Test
    void addFilmTest() {
        Film film = Film.builder()
                .name("test")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(110)
                .mpa(new Mpa(1, "G"))
                .genres(null)
                .build();
        filmDbStorage.addFilm(film);
        AssertionsForClassTypes.assertThat(film).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(film).extracting("name").isNotNull();
    }

    @Test
    void updateFilmTest() {
        Film film = Film.builder()
                .name("test")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(110)
                .mpa(new Mpa(1, "G"))
                .genres(null)
                .build();
        filmDbStorage.addFilm(film);
        film.setName("test");
        film.setDescription("test");
        filmDbStorage.updateFilm(film);
        AssertionsForClassTypes.assertThat(filmDbStorage.getFilmId(film.getId()))
                .hasFieldOrPropertyWithValue("name", "test")
                .hasFieldOrPropertyWithValue("description", "test");
    }

    @Test
    void removeFilmTest() {
        Film film = Film.builder()
                .name("test")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(110)
                .mpa(new Mpa(1, "G"))
                .genres(null)
                .build();
        filmDbStorage.addFilm(film);
        filmDbStorage.deleteFilm(film);
        AssertionsForClassTypes.assertThat(film).hasFieldOrPropertyWithValue("id", film.getId());
    }

    @Test
    void addUserTest() {

        User user = User.builder()
                .email("test@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000, 01, 01))
                .build();
        userDbStorage.createUser(user);
        AssertionsForClassTypes.assertThat(user).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(user).extracting("name").isNotNull();
    }

    @Test
    void updateUserByIdTest() {
        User user = User.builder()
                .email("test@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000, 01, 01))
                .build();
        userDbStorage.createUser(user);
        user.setName("bla");
        user.setLogin("blabla");
        user.setEmail("blablabla@mail.ru");
        userDbStorage.updateUser(user);
        AssertionsForClassTypes.assertThat(userDbStorage.getUserId(user.getId()))
                .hasFieldOrPropertyWithValue("login", "blabla")
                .hasFieldOrPropertyWithValue("name", "bla")
                .hasFieldOrPropertyWithValue("email", "blablabla@mail.ru");
    }

    @Test
    void removeUserByIdTest() {
        User user = User.builder()
                .email("test@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000, 01, 01))
                .build();

        userDbStorage.createUser(user);
        userDbStorage.deleteUser(user.getId());
        Assertions.assertThatThrownBy(() -> userDbStorage.getUserId(user.getId()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void testFilmDbStorageMpa() {

        Collection<Mpa> mpaRatingStorage = mpaDbStorage.getMpaAll();
        Assertions.assertThat(mpaRatingStorage)
                .isNotEmpty()
                .extracting(Mpa::getName)
                .containsAll(Arrays.asList("G", "PG", "PG-13", "R", "NC-17"));

    }

    @Test
    public void testFilmDbStorageGetMpaById() {
        Mpa mpa = mpaDbStorage.getMpa(1);
        Assertions.assertThat(mpa)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    public void testGetAllGenres() {
        Collection<Genre> genreStorage = genreDbStorage.getGenresAll();
        Assertions.assertThat(genreStorage)
                .extracting(Genre::getName)
                .containsAll(Arrays.asList("Комедия", "Драма", "Мультфильм", "Триллер", "Документальный", "Боевик"));
    }

    @Test
    public void testGetGenreById() {
        Genre genre1 = genreDbStorage.getGenres(1);
        Assertions.assertThat(genre1)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    void contextLoads() {
    }
}
