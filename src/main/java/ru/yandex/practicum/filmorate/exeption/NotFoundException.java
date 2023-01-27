package ru.yandex.practicum.filmorate.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends NullPointerException {
    public NotFoundException(String message) {
        super(message);
    }
}

