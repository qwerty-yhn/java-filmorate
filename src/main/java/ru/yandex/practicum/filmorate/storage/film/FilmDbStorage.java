package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.sql.In;
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
    private Integer mappingInteger(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("FILM_ID");
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

    public Film getFilmId(int id) {
        final String checkQuery = "SELECT * FROM films WHERE id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(checkQuery, id);

        if (!filmRows.next()) {
            throw new FilmNotFoundExeption("Фильм c id =" + id + " не найден");
        }

        final String sqlQuery = "SELECT * FROM films WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mappingFilm, id);
    }
    public List<Film> getRecommendations(int id){

        Map<Integer, List<Integer>> MapToLikes = new HashMap<>();
        List<Integer> resultIntermediate = new ArrayList<>();
        List<Integer> resultPreIntermediate = new ArrayList<>();
        List<Film> result = new ArrayList<>();

        int inc = 1;
        while (true){
            SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT *  FROM LIKE_FILM WHERE USER_ID = ?", inc);
            if (!filmRows.next()) {
                inc = 1;
                break;
            }
            List<Integer> filmIdByUserId = jdbcTemplate.query("SELECT DISTINCT FILM_ID  FROM LIKE_FILM WHERE USER_ID = ?", this::mappingInteger, inc);
            MapToLikes.put(inc,filmIdByUserId);
            inc++;
        }

        List<Integer> listLikingFilms = jdbcTemplate.query("SELECT DISTINCT FILM_ID  FROM LIKE_FILM WHERE USER_ID = ?", this::mappingInteger, id);

        MapToLikes.remove(id);

        int count = 0;
        int max = 0;
        for(List<Integer> l : MapToLikes.values()){
            for(Integer s : l){
                if(listLikingFilms.contains(s)){
                    count++;
                }
                else{
                    resultIntermediate.add(s);
                }
            }
            if(max < count){
                max = count;
                resultPreIntermediate = new ArrayList<Integer>(resultIntermediate);
            }
            count = 0;
            result.clear();
        }
        for(Integer i: resultPreIntermediate){
            result.add(getFilmId(i));
        }
        return result;
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
            throw new DirectorNotFoundException("Не верный тип сортировки");
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

    public List<Film> searchFilm(String query, String by) {
        String queryExtended = "%" + query + "%";
        String[] splitedBy = by.split(",");

        if(splitedBy.length == 1) {
            if (splitedBy[0].equals("title")) {
                final String sqlQuery = "SELECT id, name, description, releaseDate, duration " +
                        "FROM films AS f " +
                        "LEFT JOIN( " +
                        "SELECT film_id, COUNT(film_id) as total " +
                        "FROM like_film " +
                        "GROUP BY film_id " +
                        ") AS l ON l.film_id = f.id " +
                        "WHERE UCASE(f.name) LIKE UCASE(?) " +
                        "ORDER BY l.total DESC";
                return jdbcTemplate.query(sqlQuery, this::mappingFilm, queryExtended);
            } else if (splitedBy[0].equals("director")) {
                final String sqlQuery = "SELECT id, name, description, releaseDate, duration " +
                        "FROM films AS f " +
                        "LEFT JOIN( " +
                        "SELECT film_id, COUNT(film_id) as total " +
                        "FROM like_film " +
                        "GROUP BY film_id " +
                        ") AS l ON l.film_id = f.id " +
                        "WHERE f.id IN ( " +
                        "SELECT film_id " +
                        "FROM film_directors " +
                        "WHERE director_id IN ( " +
                        "SELECT id " +
                        "FROM directors " +
                        "WHERE UCASE(name) LIKE UCASE(?) " +
                        ") " +
                        "GROUP BY film_id " +
                        ") " +
                        "ORDER BY l.total DESC";
                return jdbcTemplate.query(sqlQuery, this::mappingFilm, queryExtended);
            }
        }else
        if(splitedBy.length == 2) {
            if (by.contains("title") && by.contains("director")) {
                final String sqlQuery = "SELECT id, name, description, releaseDate, duration " +
                        "FROM films AS f " +
                        "LEFT JOIN( " +
                        "SELECT film_id, COUNT(film_id) as total " +
                        "FROM like_film " +
                        "GROUP BY film_id " +
                        ") AS l ON l.film_id = f.id " +
                        "WHERE f.id IN ( " +
                        "SELECT film_id " +
                        "FROM film_directors " +
                        "WHERE director_id IN ( " +
                        "SELECT id " +
                        "FROM directors " +
                        "WHERE UCASE(name) LIKE UCASE(?) " +
                        ") " +
                        "GROUP BY film_id " +
                        ") " +
                        "OR UCASE(f.name) LIKE UCASE(?) " +
                        "ORDER BY l.total DESC";
                return jdbcTemplate.query(sqlQuery, this::mappingFilm, queryExtended, queryExtended);
            }
        }
        throw new DirectorNotFoundException("Неверный запрос поиска");
    }


    public List<Film> getPopularFilms(Integer count, Integer genreId, Integer year) {
        int correctCount;
        if (count != null) {
            correctCount = count;
        } else {
            correctCount = 10;
        }

        return jdbcTemplate.query(getQueryPopularFilms(genreId, year), this::mappingFilm, correctCount);
    }

    private String getQueryPopularFilms(Integer genreId, Integer year){
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT *, COUNT(l.FILM_ID) AS likes ")
                .append("FROM FILMS AS f ")
                .append("LEFT JOIN LIKE_FILM AS l ON f.ID = l.FILM_ID ");

        if (genreId != null) {
            sqlQuery.append("JOIN FILMID_GENREID AS fg ON (fg.id_film = f.id AND fg.id_genre = ").append(genreId).append(") ");
        }

        if (year != null) {
            sqlQuery.append("WHERE EXTRACT(YEAR from f.releaseDate) = ").append(year).append(" ");
        }

        sqlQuery.append("group by f.ID, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, l.USER_ID  ")
                .append("ORDER BY likes DESC ")
                .append("LIMIT ?");

        return sqlQuery.toString();
    }
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        final String sqlQuery = "SELECT id, name, description, releaseDate, duration " +
                "FROM films AS f " +
                "WHERE f.id IN ( " +
                " SELECT film_id " +
                " FROM like_film " +
                " WHERE user_id = ? AND film_id IN ( " +
                "   SELECT film_id " +
                "   FROM like_film " +
                "   WHERE user_id = ? " +
                " ) " +
                " GROUP BY film_id " +
                ")";
        return jdbcTemplate.query(sqlQuery, this::mappingFilm, userId, friendId);

    }
}
