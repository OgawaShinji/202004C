package com.example.controller;

import java.util.Objects;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.domain.User;
import com.example.form.InsertUserForm;
import com.example.repository.UsersRepository;
import com.example.service.InsertUserService;
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
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InsertUserControllerTest {

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
	private InsertUserForm form = new InsertUserForm();
	private BindingResult result = new BindException(form, "form");
	private String selectSql = "SELECT name, email, password, zipcode, address, telephone FROM users";
	private static final String TEST_NAME = "a";
	private static final String TEST_EMAIL = "1@1";
	private static final String TEST_PASSWORD = "12345678";
	private static final String TEST_ZIPCODE = "1660003";
	private static final String TEST_ADDRESS = "高円寺";
	private static final String TEST_TELEPHONE = "09011111111";
	private User user = new User();
	public static final Operation DELETE_ALL = Operations.deleteAllFrom("users");
	private MockMvc mockMvc;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private InsertUserController insertUserController;
	@Autowired
	private InsertUserService insertUserService;
	@Autowired
	private NamedParameterJdbcTemplate template;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		Destination dest = new DataSourceDestination(dataSource);
		Operation ops = Operations.sequenceOf(DELETE_ALL);
		DbSetup dbSetup = new DbSetup(dest, ops);
		dbSetup.launch();
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(insertUserController).build();
		this.user.setName(TEST_NAME);
		this.user.setEmail(TEST_EMAIL);
		this.user.setPassword(TEST_PASSWORD);
		this.user.setZipcode(TEST_ZIPCODE);
		this.user.setAddress(TEST_ADDRESS);
		this.user.setTelephone(TEST_TELEPHONE);
		insertUserService.insert(user);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void insert_異常系_email重複とcheckパスワード_正常系_insert() throws Exception {

		form.setName("b");
		form.setEmail("1@1");
		form.setPassword("12345678");
		form.setCheckpassword("12345678");
		form.setZipcode("155-0003");
		form.setAddress("高円寺");
		form.setTelephone("090-1111-1111");
		StringBuilder br = new StringBuilder(form.getZipcode());
		br.deleteCharAt(3);
		// form.setZipcode(br.toString());
		// form.setTelephone(form.getTelephone());

		mockMvc.perform(get("/toInsert/insert/").param("name", form.getName()).param("email", form.getEmail())
				.param("password", form.getPassword()).param("checkpassword", form.getCheckpassword())
				.param("zipcode", form.getZipcode()).param("address", form.getAddress())
				.param("telephone", form.getTelephone()))
				.andExpect(model().attribute("emailError", "そのメールアドレスはすでに使われています"));

		form.setEmail("2@2");
		form.setCheckpassword("12345679");
		mockMvc.perform(get("/toInsert/insert/").param("name", form.getName()).param("email", form.getEmail())
				.param("password", form.getPassword()).param("checkpassword", form.getCheckpassword())
				.param("zipcode", form.getZipcode()).param("address", form.getAddress())
				.param("telephone", form.getTelephone()))
				.andExpect(model().attribute("passwordError", "パスワードと確認用パスワードが不一致です"));

		form.setCheckpassword("12345678");
		mockMvc.perform(get("/toInsert/insert/").param("name", form.getName()).param("email", form.getEmail())
				.param("password", form.getPassword()).param("checkpassword", form.getCheckpassword())
				.param("zipcode", form.getZipcode()).param("address", form.getAddress())
				.param("telephone", form.getTelephone())).andExpect(status().isOk())
				.andExpect(view().name("forward:/toLogin"));
	}

}