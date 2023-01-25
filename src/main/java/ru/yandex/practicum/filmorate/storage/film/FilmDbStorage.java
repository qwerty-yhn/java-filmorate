package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static java.util.Calendar.DECEMBER;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, DECEMBER, 28))) {
            throw new VaidationExeption("Дата релиза - не раньше 28 декабря 1895 года");
        }
        //Добавляем фильм в таблицу films
        final String sqlQuery = "insert into films(name, description, releaseDate, duration) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        //

        //Теперь загружаем таблицу mpa_films
        String sqlQueryDublicate = "INSERT INTO mpa_films (id_film, id_mpa) VALUES (?, ?)";
        jdbcTemplate.update(sqlQueryDublicate, film.getId(), film.getMpa().getId());
        //
        if (film.getGenres() != null) {
            sqlQueryDublicate = "INSERT INTO filmid_genreid (id_film, id_genre) VALUES (?, ?)";
            for (Genre g : film.getGenres()) {
                jdbcTemplate.update(sqlQueryDublicate, film.getId(), g.getId());
            }
        }
        film.setMpa(MpaOfFilm(film.getId()));
        updateFilmDirector(film);
        return film;
    }

    private Mpa mappingMpa(ResultSet resultSet, int rowNum) throws SQLException {
        final int id = resultSet.getInt("id");
        final String name = resultSet.getString("name");
        return new Mpa(id, name);
    }

    @Override
    public void deleteFilm(Film film) {

        jdbcTemplate.update("DELETE FROM filmid_genreid WHERE id_film = ?", film.getId());
        jdbcTemplate.update("DELETE FROM mpa_films WHERE id_film = ?", film.getId());
        jdbcTemplate.update("DELETE FROM films WHERE id = ?", film.getId());
        updateFilmDirector(film);
    }

    @Override
    public Film updateFilm(Film film) {
        final String checkQuery = "SELECT * FROM films WHERE id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(checkQuery, film.getId());

        if (!filmRows.next()) {
            throw new UserNotFoundException("Фильм с id = " + film.getId() + " не найден");
        }
        final String sqlQuery = "UPDATE films SET name = ?, description = ?, releaseDate = ?, " +
                "duration = ? WHERE id = ?";

        jdbcTemplate.update(sqlQuery,
                film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getId());


        final String deleteMpa = "DELETE FROM mpa_films WHERE id_film = ?";
        final String updateMpa = "INSERT INTO mpa_films (id_film, id_mpa) VALUES (?, ?)";

        jdbcTemplate.update(deleteMpa, film.getId());
        jdbcTemplate.update(updateMpa, film.getId(), film.getMpa().getId());

        if (film.getGenres() != null) {
            final String deleteGenreQuery = "DELETE FROM filmid_genreid WHERE id_film = ?";
            final String updateGenreQuery = "INSERT INTO filmid_genreid (id_film, id_genre) VALUES (?, ?)";

            jdbcTemplate.update(deleteGenreQuery, film.getId());
            for (Genre g : film.getGenres()) {
                String check = "SELECT * FROM filmid_genreid WHERE id_film = ? AND id_genre = ?";
                SqlRowSet checkSqlRowSet = jdbcTemplate.queryForRowSet(check, film.getId(), g.getId());
                if (!checkSqlRowSet.next()) {
                    jdbcTemplate.update(updateGenreQuery, film.getId(), g.getId());
                }
            }
        }
        String mpaSqlQuery = "SELECT id, name " +
                "FROM mpa " +
                "LEFT JOIN mpa_films m ON mpa.id = m.id_mpa " +
                "WHERE id_film = ?";

        Mpa mpa = jdbcTemplate.queryForObject(mpaSqlQuery, this::mappingMpa, film.getId());
        updateFilmDirector(film);
        film.setGenres(genreOfFilm(film.getId()));
        return film;
    }

    public Collection<Film> getFilms() {
        String sqlQuery = "SELECT * FROM films";
        return jdbcTemplate.query(sqlQuery, this::mappingFilm);
    }

    private Film mappingFilm(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        LocalDate releaseDate = resultSet.getDate("releaseDate").toLocalDate();
        int duration = resultSet.getInt("duration");

        Film film = Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(MpaOfFilm(id))
                .genres(genreOfFilm(id))
                .directors(directorOfFilm(id))
                .build();

        return film;
    }

    private Mpa MpaOfFilm(int filmId) {
        final String mpaSqlQuery = "SELECT id, name " +
                "FROM mpa " +
                "LEFT JOIN mpa_films MF ON mpa.id = mf.id_mpa " +
                "WHERE id_film = ?";

        return jdbcTemplate.queryForObject(mpaSqlQuery, this::mappingMpa, filmId);
    }

    private Genre mappingGenre(ResultSet resultSet, int rowNum) throws SQLException {
        final int id = resultSet.getInt("id");
        final String name = resultSet.getString("name");
        return new Genre(id, name);
    }

    private List<Genre> genreOfFilm(int filmId) {
        final String genresSqlQuery = "SELECT genre.id, name " +
                "FROM genre " +
                "LEFT JOIN filmid_genreid f on genre.id = f.id_genre " +
                "WHERE id_film = ?";

        return jdbcTemplate.query(genresSqlQuery, this::mappingGenre, filmId);
    }

    private Set<Director> directorOfFilm(int filmId) {
        final String genresSqlQuery = "SELECT * " +
                "FROM FILM_DIRECTORS AS FD " +
                "LEFT JOIN DIRECTORS AS D on FD.DIRECTOR_ID = D.ID " +
                "WHERE FD.FILM_ID = ?";

        List<Director> directors = jdbcTemplate.query(genresSqlQuery, this::mappingDirector, filmId);
        return new HashSet<>(directors);
    }

    private Director mappingDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return new Director(resultSet.getInt("DIRECTORS.ID"),
                resultSet.getString("NAME")
        );
    }

    public List<Film> getTopFilms(int count) {
        final String sqlQuery = "SELECT id, name, description, releaseDate, duration " +
                "FROM films " +
                "LEFT JOIN like_film f ON films.id = f.film_id " +
                "group by films.id, f.film_id IN ( " +
                "    SELECT film_id " +
                "    FROM like_film " +
                ") " +
                "ORDER BY COUNT(f.film_id) DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, this::mappingFilm, count);
    }

    public Film getFilmId(int id) {
        final String checkQuery = "SELECT * FROM films WHERE id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(checkQuery, id);

        if (!filmRows.next()) {
            throw new FilmNotFoundExeption("Фильм c id =" + id + " не найден");
        }

        final String sqlQuery = "SELECT * FROM films WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mappingFilm, id);
    }

    private void updateFilmDirector(Film film) { // Обновление режиссеров фильма
        String sqlQueryDeleteGenre = "delete from FILM_DIRECTORS where FILM_ID = ?";
        jdbcTemplate.update(sqlQueryDeleteGenre, film.getId());

        if(film.getDirectors() == null || film.getDirectors().isEmpty()) {
            return;
        }

        for (Director director : film.getDirectors()) {
            String sqlQueryAddGenre = "insert into FILM_DIRECTORS (FILM_ID, DIRECTOR_ID) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQueryAddGenre, film.getId(), director.getId());
        }
        film.getDirectors().clear();
        film.setDirectors(directorOfFilm(film.getId()));
    }

    public List<Film> getTopFilmsDirector(int directorId, String sorting) { // Возвращает спсиок фильмов режиссера, отсортированных по году или лайкам
        if (sorting.equals("year")) {
            return getTopFilmsDirectorByYear(directorId);
        } else if (sorting.equals("likes")) {
            return getTopFilmsDirectorByLikes(directorId);
        } else {
            return null;
        }
    }

    private List<Film> getTopFilmsDirectorByYear(int directorId) {
        final String sqlQuery = "SELECT * " +
                "FROM FILM_DIRECTORS AS FD " +
                "JOIN FILMS F on FD.FILM_ID = F.ID " +
                "WHERE FD.DIRECTOR_ID = ? " +
                "ORDER BY F.DURATION";
        return jdbcTemplate.query(sqlQuery, this::mappingFilm, directorId);
    }

    private List<Film> getTopFilmsDirectorByLikes(int directorId) {
        final String sqlQuery = "SELECT *, COUNT(LF.FILM_ID) AS likes " +
                "FROM FILM_DIRECTORS AS FD " +
                "JOIN FILMS F on FD.FILM_ID = F.ID " +
                "LEFT JOIN LIKE_FILM LF on F.ID = LF.FILM_ID " +
                "WHERE FD.DIRECTOR_ID = ? " +
                "group by FD.FILM_ID, DIRECTOR_ID, ID, NAME, DESCRIPTION, RELEASEDATE, DURATION, LF.FILM_ID, USER_ID " +
                "ORDER BY likes DESC";
        return jdbcTemplate.query(sqlQuery, this::mappingFilm, directorId);
    }
}
