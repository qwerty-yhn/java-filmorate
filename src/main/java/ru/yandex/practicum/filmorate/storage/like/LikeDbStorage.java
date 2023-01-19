package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
@Component
@RequiredArgsConstructor
public class LikeDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public Film addLikeToFilm(int id, int userId) {
        final String sqlQuery = "INSERT INTO like_film (film_id, user_id)" +
                "VALUES (?, ?);";
        jdbcTemplate.update(sqlQuery, id, userId);
        return getFilmId(id);
    }

    public void deleteLikeToFilm(int id, int userId) {

        final String checkFilm = "SELECT * FROM films WHERE id = ?";
        final String checkUser = "SELECT * FROM users WHERE id = ?";

        SqlRowSet SqlFilm = jdbcTemplate.queryForRowSet(checkFilm, id);
        SqlRowSet SqlUser = jdbcTemplate.queryForRowSet(checkUser, userId);

        if (!SqlFilm.next() || !SqlUser.next()) {
            throw new FilmNotFoundExeption("Фильм c id = " + id + " не найден");
        }
        final String sqlQuery = "DELETE FROM like_film " +
                "WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sqlQuery, id, userId);
    }
    private Film getFilmId(int id) {
        final String checkQuery = "SELECT * FROM films WHERE id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(checkQuery, id);

        if (!filmRows.next()) {
            throw new FilmNotFoundExeption("Фильм c id = " + id + " не найден");
        }

        final String sqlQuery = "SELECT * FROM films WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mappingFilm, id);
    }
    private Film mappingFilm(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        LocalDate releaseDate = resultSet.getDate("releaseDate").toLocalDate();
        int duration = resultSet.getInt("duration");

        Film film = Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(MpaOfFilm(id))
                .genres(GenreOfFilm(id))
                .build();

        return film;
    }
    private Mpa MpaOfFilm(int filmId) {
        final String mpaSqlQuery = "SELECT id, name " +
                "FROM mpa " +
                "LEFT JOIN mpa_films MF ON mpa.id = mf.id_mpa " +
                "WHERE id_film = ?";

        return jdbcTemplate.queryForObject(mpaSqlQuery, this::mappingMpa, filmId);
    }
    private Genre mappingGenre(ResultSet resultSet, int rowNum) throws SQLException {
        final int id = resultSet.getInt("id");
        final String name = resultSet.getString("name");
        return new Genre(id, name);
    }

    private List<Genre> GenreOfFilm(int filmId) {
        final String genresSqlQuery = "SELECT genre.id, name " +
                "FROM genre " +
                "LEFT JOIN filmid_genreid f on genre.id = f.id_genre " +
                "WHERE id_film = ?";

        return jdbcTemplate.query(genresSqlQuery, this::mappingGenre, filmId);
    }
    private Mpa mappingMpa(ResultSet resultSet, int rowNum) throws SQLException {
        final int id = resultSet.getInt("id");
        final String name = resultSet.getString("name");
        return new Mpa(id, name);
    }
}
