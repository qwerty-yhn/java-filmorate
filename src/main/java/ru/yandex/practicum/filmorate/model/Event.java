package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.enums.EventTypes;
import ru.yandex.practicum.filmorate.model.enums.OperationTypes;

import javax.validation.constraints.Positive;

@Data
@Builder
public class Event {
    private Long timestamp;
    private int userId;
    private int eventId;
    private int entityId;
    private EventTypes eventType;
    private OperationTypes operation;
}
