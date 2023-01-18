package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Component
@RequiredArgsConstructor
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public Mpa getMpa(int id) {
        String sqlQuery = "SELECT * FROM mpa WHERE id = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (!mpaRows.next()) {
            throw new MpaNotFoundException("Рейтинг c id = " + id + " не найден");
        }

        sqlQuery = "SELECT * FROM mpa WHERE id = ?;";
        return jdbcTemplate.queryForObject(sqlQuery, this::mappingMpa, id);
    }

    public List<Mpa> getMpaAll() {
        String sqlQuery = "SELECT * FROM mpa";
        return jdbcTemplate.query(sqlQuery, this::mappingMpa);
    }
    private Mpa mappingMpa(ResultSet resultSet, int rowNum) throws SQLException {
        final int id = resultSet.getInt("id");
        final String name = resultSet.getString("name");
        return new Mpa(id, name);
    }

}
