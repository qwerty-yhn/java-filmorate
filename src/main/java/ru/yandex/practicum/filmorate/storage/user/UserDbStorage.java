package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(User user) {

        String sqlQuery = "insert into users(email, login, name, birthday) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    public void deleteUser(int id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }

    @Override
    public User updateUser(User user) {

        String sqlQueryCheak = "SELECT * FROM users WHERE id = ?";

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQueryCheak, user.getId());
        if (!userRows.next()) {
            throw new UserNotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }

        String sqlQuery = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "where id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
        return user;
    }

    public Collection<User> getUsers() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, this::mappingUser);
    }

    private User mappingUser(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("id");
        String email = resultSet.getString("email");
        String login = resultSet.getString("login");
        String name = resultSet.getString("name");
        LocalDate birthday = resultSet.getDate("birthday").toLocalDate();

        User user = User.builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
        return user;
    }

    public User getUserId(int id) {
        String sqlQueryСheck = "SELECT * FROM users WHERE id = ?";

        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQueryСheck, id);
        if (!userRows.next()) {
            throw new UserNotFoundException("Пользователь с id = " + id + " не найден");
        }
        String sqlQuery = "SELECT * FROM users WHERE id = ?";
        User user = jdbcTemplate.queryForObject(sqlQuery, this::mappingUser, id);
        return user;
    }

    public List<User> getCommonFriend(int id, int otherId) {
        final String sqlQuery = "select users.id, email, login, name, birthday " +
                "                from users LEFT OUTER JOIN FRIENDSHIP on USERS.ID = FRIENDSHIP.ID_USER " +
                "                WHERE users.ID IN ( " +
                "                                       SELECT f_friend.ID_FRIEND " +
                "                                       FROM FRIENDSHIP f_friend LEFT OUTER JOIN USERS u on u.ID = f_friend.ID_USER " +
                "                                       WHERE f_friend.ID_USER = ? and users.ID IN ( " +
                "                                                                                   SELECT fs.ID_FRIEND " +
                "                                                                                   FROM FRIENDSHIP fs " +
                "                                                                                   WHERE fs.ID_USER = ?));";

        return jdbcTemplate.query(sqlQuery, this::mappingUser, id, otherId);
    }



}
