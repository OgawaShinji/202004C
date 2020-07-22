package com.example.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.sql.DataSource;

import com.example.domain.User;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsersRepositoryTest {

	public static final Operation DELETE_ALL = Operations.deleteAllFrom("users");

	public static final Operation INSERT = Operations.insertInto("users")
			.columns("id", "name", "email", "password", "zipcode", "address", "telephone").values("2", "a", "1@1",
					"$2a$10$vfOQWEFz58aX6VPJj8LBv.Gw9rUwh9SE/yQwIMhajd0d7vXjR80tK", "1660003", "高円寺", "09011111111")
			.build();

	public static final RowMapper<User> USER_ROW_MAPPER = (rs, i) -> {
		User user = new User();
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		user.setZipcode(rs.getString("zipcode"));
		user.setAddress(rs.getString("address"));
		user.setTelephone(rs.getString("telephone"));
		return user;
	};

	private String selectSql = "SELECT name, email, password, zipcode, address, telephone FROM users";
	private static final String TEST_NAME = "b";
	private static final String TEST_EMAIL = "2@2";
	private static final String TEST_PASSWORD = "12345678";
	private static final String TEST_ZIPCODE = "1660003";
	private static final String TEST_ADDRESS = "高円寺";
	private static final String TEST_TELEPHONE = "09011111111";

	private User testUser = new User();

	@Autowired
	private NamedParameterJdbcTemplate template;

	@Autowired
	private UsersRepository usersRepository;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private DataSource dataSource;

	@Before
	public void setUp() throws Exception {
		Destination dest = new DataSourceDestination(dataSource);
		Operation ops = Operations.sequenceOf(DELETE_ALL, INSERT);
		DbSetup dbSetup = new DbSetup(dest, ops);
		dbSetup.launch();
	}

	@After
	public void tearDown() throws Exception {
	}

	// 引数にDBに存在するemail="1@1",password="$2a$10$vfOQWEFz58aX6VPJj8LBv.Gw9rUwh9SE/yQwIMhajd0d7vXjR80tK"を入力した時の動作確認
	@Test
	public void findByMailAddressAndPasswardメソッドの正常系のテスト() {
		String sql = "select * from users where email=:email and password=:password";
		String email = "1@1";
		String password = "$2a$10$vfOQWEFz58aX6VPJj8LBv.Gw9rUwh9SE/yQwIMhajd0d7vXjR80tK";

		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email).addValue("password", password);
		User expected = template.queryForObject(sql, param, USER_ROW_MAPPER);
		User actual = usersRepository.findByMailAddressAndPassward(email, password);

		assertEquals("nameが違います", expected.getName(), actual.getName());
		assertEquals("emailが違います", expected.getEmail(), actual.getEmail());
		assertEquals("passwordが違います", expected.getPassword(), actual.getPassword());
		assertEquals("zipcodeが違います", expected.getZipcode(), actual.getZipcode());
		assertEquals("addressが違います", expected.getAddress(), actual.getAddress());
		assertEquals("telephoneが違います", expected.getTelephone(), actual.getTelephone());
	}

	// 引数にDBに存在しないemail="2@2",password="22222222"を入力した時の動作確認
	@Test
	public void findByMailAddressAndPasswardメソッドの異常系のテスト() {
		String email = "2@2";
		String password = "22222222";

		User expected = null;
		User actual = usersRepository.findByMailAddressAndPassward(email, password);

		assertEquals("actualの値にnullが入っていません", expected, actual);
	}

	// testUserをinsertした時に正常にinsertされているか
	@Test
	public void insertメソッドの正常系テスト() {
		this.testUser.setName(TEST_NAME);
		this.testUser.setEmail(TEST_EMAIL);
		this.testUser.setPassword(TEST_PASSWORD);
		this.testUser.setZipcode(TEST_ZIPCODE);
		this.testUser.setAddress(TEST_ADDRESS);
		this.testUser.setTelephone(TEST_TELEPHONE);
		usersRepository.insert(this.testUser);
		List<User> users = template.query(selectSql, USER_ROW_MAPPER);
		if (passwordEncoder.matches(TEST_PASSWORD, users.get(1).getPassword())) {
			this.testUser.setPassword(users.get(1).getPassword());
		}
		assertEquals("nameが違います", this.testUser.getName(), users.get(1).getName());
		assertEquals("emailが違います", this.testUser.getEmail(), users.get(1).getEmail());
		assertEquals("passwordが違います", this.testUser.getPassword(), users.get(1).getPassword());
		assertEquals("zipcodeが違います", this.testUser.getZipcode(), users.get(1).getZipcode());
		assertEquals("addressが違います", this.testUser.getAddress(), users.get(1).getAddress());
		assertEquals("telephoneが違います", this.testUser.getTelephone(), users.get(1).getTelephone());
	}

	// 引数にDBに存在するemail="1@1"を入力した時の正常系の動作確認
	@Test
	public void findByMailAddressメソッドの正常系テスト() {
		String sql = "select * from users where email=:email";
		String email = "1@1";

		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
		User expected = template.queryForObject(sql, param, USER_ROW_MAPPER);
		User actual = usersRepository.findByMailAddress(email);
		assertEquals("nameが違います", expected.getName(), actual.getName());
		assertEquals("emailが違います", expected.getEmail(), actual.getEmail());
		assertEquals("passwordが違います", expected.getPassword(), actual.getPassword());
		assertEquals("zipcodeが違います", expected.getZipcode(), actual.getZipcode());
		assertEquals("addressが違います", expected.getAddress(), actual.getAddress());
		assertEquals("telephoneが違います", expected.getTelephone(), actual.getTelephone());
	}

	// 引数に存在しないemail="2@2"を入力した時の異常系の動作確認
	@Test
	public void findByMailAddressメソッドの異常系テスト() {
		String email = "2@2";

		User expected = null;
		User actual = usersRepository.findByMailAddress(email);

		assertEquals("actualの値にnullが入っていません", expected, actual);
	}

}
