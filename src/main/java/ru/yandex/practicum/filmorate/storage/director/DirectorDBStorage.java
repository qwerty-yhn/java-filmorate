package ru.yandex.practicum.filmorate.storage.director;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DirectorDBStorage implements DirectorStorage{
	
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Director> getAll() {
        final String sqlQuery = "select * from DIRECTORS order by ID";
        return jdbcTemplate.query(sqlQuery, this::makeDirector);
    }

    @Override
    public Director getById(int id) {
        final String sqlQuery = "select * from DIRECTORS where ID = ?";
        List<Director> directors = jdbcTemplate.query(sqlQuery, this::makeDirector, id);
        if (directors.isEmpty()) {
            throw new DirectorNotFoundException(id);
        }
        return directors.get(0);
    }

    @Override
    public Director create(Director director) {
        final String sqlQuery = "insert into DIRECTORS(NAME) values (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"ID"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);

        director.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return director;
    }

    @Override
    public Director update(Director director) {
        final String sqlQuery = "update DIRECTORS set NAME = ? where ID = ?";
        int changes = jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
        if (changes == 1) {
            return director;
        }
        return null;
    }

    @Override
    public void remove(int id) {
        final String sqlQuery = "delete from DIRECTORS where ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    private Director makeDirector(ResultSet rs, int id) throws SQLException {
        return new Director(rs.getInt("DIRECTORS.ID"),
                rs.getString("DIRECTORS.NAME")
        );
    }
}
