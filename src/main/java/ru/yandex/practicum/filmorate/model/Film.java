package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;


@Data
@Builder(toBuilder = true)
public class Film {

    private int id;

    @NotBlank
    private String name;
    @NotNull
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(value = 1)
    @Positive
    private Integer duration;

    private List<Genre> genres;

    //private String mpa;
    private Mpa mpa;

    private Set<Long> likes = new HashSet<>();
    private Set<Director> directors = new HashSet<>();
}
