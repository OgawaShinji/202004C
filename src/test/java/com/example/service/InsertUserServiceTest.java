package com.example.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.sql.DataSource;

import com.example.domain.User;
import com.example.repository.UsersRepository;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
public class InsertUserServiceTest {

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
	private static final String TEST_NAME = "a";
	private static final String TEST_EMAIL = "1@1";
	private static final String TEST_PASSWORD = "12345678";
	private static final String TEST_ZIPCODE = "1660003";
	private static final String TEST_ADDRESS = "高円寺";
	private static final String TEST_TELEPHONE = "09011111111";

	private User testUser = new User();

	public static final Operation DELETE_ALL = Operations.deleteAllFrom("users");

	@Autowired
	private InsertUserService insertUserService;

	@Autowired
	private NamedParameterJdbcTemplate template;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/*
	 * @Mock private UsersRepository usersRepository;
	 * 
	 * @InjectMocks private InsertUserService insertUserService;
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Autowired
	private DataSource dataSource;

	@Before
	public void setUp() throws Exception {
		Destination dest = new DataSourceDestination(dataSource);
		Operation ops = Operations.sequenceOf(DELETE_ALL);
		DbSetup dbSetup = new DbSetup(dest, ops);
		dbSetup.launch();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void insert機能のテストとハッシュ化ができているかのテスト() throws Exception {
		this.testUser.setName(TEST_NAME);
		this.testUser.setEmail(TEST_EMAIL);
		this.testUser.setPassword(TEST_PASSWORD);
		this.testUser.setZipcode(TEST_ZIPCODE);
		this.testUser.setAddress(TEST_ADDRESS);
		this.testUser.setTelephone(TEST_TELEPHONE);
		insertUserService.insert(this.testUser);
		List<User> users = template.query(selectSql, USER_ROW_MAPPER);
		if (passwordEncoder.matches(TEST_PASSWORD, users.get(0).getPassword())) {
			this.testUser.setPassword(users.get(0).getPassword());
		}
		// when(usersRepository.insert(this.testUser)).thenReturn(new User());
		// 戻り値が無いから使えない。そのままDBを確認してもよい。
		assertEquals("nameが違います", this.testUser.getName(), users.get(0).getName());
		assertEquals("emailが違います", this.testUser.getEmail(), users.get(0).getEmail());
		assertEquals("passwordが違います", this.testUser.getPassword(), users.get(0).getPassword());
		assertEquals("zipcodeが違います", this.testUser.getZipcode(), users.get(0).getZipcode());
		assertEquals("addressが違います", this.testUser.getAddress(), users.get(0).getAddress());
		assertEquals("telephoneが違います", this.testUser.getTelephone(), users.get(0).getTelephone());
	}

}
