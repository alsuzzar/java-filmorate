package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.RatingMPADbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({UserDbStorage.class, FilmDbStorage.class, GenreDbStorage.class, RatingMPADbStorage.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private final GenreDbStorage genreStorage;
	private final RatingMPADbStorage mpaStorage;

	//костыли добавления данных в бд
	@Autowired
	private JdbcTemplate jdbcTemplate;

	User user;
	Film film;
	User user2;
	Film film2;
	Genre comedy;
	Genre drama;
	RatingMPA g;
	RatingMPA pg;

	@BeforeEach
	public void setUp() {
		user = new User();
		film = new Film();
		user2 = new User();
		film2 = new Film();
		comedy = new Genre();
		drama = new Genre();
		g = new RatingMPA();
		pg = new RatingMPA();

		//это костыль для заполнения таблицы users
		user.setName("test");
		user.setEmail("test@mail.com");
		user.setLogin("test");
		user.setBirthday(LocalDate.of(2000, 1, 1));
		userStorage.createUser(user);

		user2.setName("test2");
		user2.setEmail("test2@mail.com");
		user2.setLogin("test2");
		user2.setBirthday(LocalDate.of(1990, 1, 1));
		userStorage.createUser(user2);

		//это костыль для заполнения таблицы mpa_rating
		g.setName("G");
		pg.setName("PG");
		mpaStorage.createRatingMPA(g);
		mpaStorage.createRatingMPA(pg);

		//это костыль для заполнения таблицы films
		film.setName("testname1");
		film.setDescription("This is the film");
		film.setDuration(175);
		film.setMpa(g);
		film.setReleaseDate(LocalDate.of(1991, 1, 1));
		filmStorage.createFilm(film);

		film2.setName("testname2");
		film2.setDescription("This is the second film");
		film2.setDuration(200);
		film2.setMpa(pg);
		film2.setReleaseDate(LocalDate.of(2015, 1, 1));
		filmStorage.createFilm(film2);

		//это костыль для заполнения таблицы genres
		comedy.setName("Комедия");
		drama.setName("Драма");
		genreStorage.createGenre(comedy);
		genreStorage.createGenre(drama);
	}

	@Test
	public void testFindUserById() {

		User result = userStorage.getUserById(user.getId());
		assertThat(result.getId()).isEqualTo(user.getId());
	}

	@Test
	public void testFindFilmById() {

		Film result = filmStorage.getFilmById(film.getId());
		assertThat(result.getId()).isEqualTo(film.getId());
	}

	@Test
	public void testFindAllUsers() {

		List<User> result = userStorage.findAll();
		assertTrue(result.contains(user));
		assertEquals(2, result.size());
	}

	@Test
	public void testFindAllFilms() {

		List<Film> result = filmStorage.findAll();
		assertTrue(result.contains(film));
		assertEquals(2, result.size());
	}

	@Test
	public void testUpdateFilm() {

		film.setName("newtestname1");
		film.setDuration(54);
		filmStorage.updateFilm(film);

		Film updResult = filmStorage.getFilmById(film.getId());
		assertThat(updResult.getName()).isEqualTo("newtestname1");
		assertEquals(54, (int) updResult.getDuration());
		assertThat(updResult.getDescription()).isEqualTo("This is the film");
	}

	@Test
	public void testUpdateUser() {

		user.setEmail("newtest@mail.com");
		userStorage.updateUser(user);

		User updResult = userStorage.getUserById(user.getId());
		assertThat(updResult.getName()).isEqualTo("test");
		assertThat(updResult.getEmail()).isEqualTo("newtest@mail.com");
	}

	@Test
	public void testDeleteUser() {

		userStorage.deleteUser(user.getId());

		List<User> result = userStorage.findAll();
		assertEquals(1, result.size());

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> userStorage.getUserById(user.getId()));
		assertThat(ex.getMessage()).isEqualTo("Нет записи");
	}

	@Test
	public void testDeleteFilm() {

		filmStorage.deleteFilm(film.getId());

		List<Film> result = filmStorage.findAll();
		assertEquals(1, result.size());

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> filmStorage.getFilmById(film.getId()));
		assertThat(ex.getMessage()).isEqualTo("Нет записи");
	}

	@Test
	public void testFindGenreById() {

		Genre result = genreStorage.getGenreById(comedy.getId());
		assertThat(result.getId()).isEqualTo(comedy.getId());
	}

	@Test
	public void testFindAllGenres() {

		List<Genre> result = genreStorage.findAll();
		assertTrue(result.contains(comedy));
		assertEquals(2, result.size());
	}

	@Test
	public void testFindRatingMPAById() {

		RatingMPA result = mpaStorage.getRatingMPAById(g.getId());
		assertThat(result.getId()).isEqualTo(g.getId());
	}

	@Test
	public void testFindAllRatingMPA() {

		List<RatingMPA> result = mpaStorage.findAll();
		assertTrue(result.contains(g));
		assertEquals(2, result.size());
	}
}