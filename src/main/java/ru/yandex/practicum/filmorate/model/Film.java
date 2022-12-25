package ru.yandex.practicum.filmorate.model;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class Film {

    private Long id;

    @NotBlank
    private String name;

    private String description;

    private LocalDate releaseDate;

    private Integer duration;

    private Set<Long> likes = new HashSet<>();

}
