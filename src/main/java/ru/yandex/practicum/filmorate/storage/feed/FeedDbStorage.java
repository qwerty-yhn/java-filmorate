package ru.yandex.practicum.filmorate.storage.feed;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.enums.EventTypes;
import ru.yandex.practicum.filmorate.model.enums.OperationTypes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component("FeedDbStorage")
@RequiredArgsConstructor
public class FeedDbStorage implements FeedStorage{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> getUserById(int userId) {
        String sqlQuery = "SELECT f.id, f.created_at, f.user_id, f.event_type, " +
                "f.operation, f.entity_id FROM feed AS f WHERE f.user_id = ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeEvent(rs), userId);
    }

    @Override
    public Event addEvent(Event event) {
        String sqlQuery = "INSERT INTO feed (created_at, user_id, event_type, operation, entity_id) " +
                "VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"id"});
            statement.setLong(1, event.getTimestamp());
            statement.setInt(2, event.getUserId());
            statement.setString(3, event.getEventType().toString());
            statement.setString(4, event.getOperation().toString());
            statement.setInt(5, event.getEntityId());
            return statement;
        }, keyHolder);
        event.setEventId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return event;
    }

    private Event makeEvent(ResultSet rs) throws SQLException {
        Long timestamp = rs.getLong("created_at");
        int userId = rs.getInt("user_id");
        EventTypes eventType = EventTypes.valueOf(rs.getString("event_type"));
        OperationTypes operation = OperationTypes.valueOf(rs.getString("operation"));
        int eventId = rs.getInt("id");
        int entityId = rs.getInt("entity_id");

        return new Event(timestamp, userId, eventId, entityId, eventType, operation);
    }
}
