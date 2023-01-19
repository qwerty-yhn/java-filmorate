package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
@Component
@RequiredArgsConstructor
public class FriendDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public List<Integer> addFriend(int id, int friendId) {
        String sqlQueryСheck = "SELECT * FROM users WHERE id = ?";

        SqlRowSet idRows = jdbcTemplate.queryForRowSet(sqlQueryСheck, id);
        SqlRowSet friendIdRows = jdbcTemplate.queryForRowSet(sqlQueryСheck, friendId);
        if (!idRows.next() || !friendIdRows.next()) {
            throw new UserNotFoundException("Пользователь c id =" + id + " или " + friendId + "  не найден");
        }
        String sqlQuery = "INSERT INTO FRIENDSHIP (ID_USER, ID_FRIEND, CONFIRM) " +
                "values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, id, friendId, true);
        return List.of(id, friendId);
    }

    public List<User> getFriend(int id) {
        String sqlQueryСheck = "SELECT * FROM users WHERE id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQueryСheck, id);
        if (!userRows.next()) {
            throw new UserNotFoundException("Пользователь c id =" + id + "  не найден");
        }
        String sqlQuery = "select users.id, email, login, name, birthday from users LEFT OUTER JOIN FRIENDSHIP on USERS.ID = FRIENDSHIP.ID_USER WHERE users.ID IN (SELECT f_friend.ID_FRIEND FROM FRIENDSHIP f_friend WHERE f_friend.ID_USER = ?);";

        return jdbcTemplate.query(sqlQuery, this::mappingUser, id);
    }

    public void deleteFriend(int id, int friendId) {
        final String sqlQuery = "DELETE FROM friendship WHERE ID_USER = ? AND ID_FRIEND = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
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
}
