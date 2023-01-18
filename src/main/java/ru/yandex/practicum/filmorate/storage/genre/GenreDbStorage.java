package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Component
@RequiredArgsConstructor
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public Genre getGenres(int id) {
        String sqlQueryCheck = "SELECT * FROM mpa WHERE id = ?";
        SqlRowSet GenreRows = jdbcTemplate.queryForRowSet(sqlQueryCheck, id);

        if (!GenreRows.next()) {
            throw new GenreNotFoundException("Жанр c id = " + id + " не найден");
        }

        String sqlQuery = "SELECT * FROM genre WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mappingGenre, id);
    }

    public List<Genre> getGenresAll() {
        String sqlQuery = "SELECT * FROM genre";
        return jdbcTemplate.query(sqlQuery, this::mappingGenre);
    }
    private Genre mappingGenre(ResultSet resultSet, int rowNum) throws SQLException {
        final int id = resultSet.getInt("id");
        final String name = resultSet.getString("name");
        return new Genre(id, name);
    }
}
