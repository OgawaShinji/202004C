package com.example.repository;

import static org.junit.Assert.*;

import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.domain.Topping;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ToppingsRepositoryTest {

	public static final Operation DELETE_ALL = Operations.deleteAllFrom("toppings");

	public static final Operation INSERT = Operations.insertInto("toppings")
			.columns("id", "name", "price_m", "price_l")
			.values("1", "コーヒークリーム", "200", "300")
			.values("2", "低脂肪牛乳", "200", "300")
			.values("3", "無脂肪牛乳", "200", "300")
			.values("4", "豆乳", "200", "300")
			.values("5", "オールミルク", "200", "300")
			.values("6", "キャラメルソース", "200", "300")
			.values("7", "チョコソース", "200", "300")
			.values("8", "チョコチップ", "200", "300")
			.values("9", "蜂蜜", "200", "300")
			.values("10", "ホイップクリーム増量", "200", "300")
			.values("11", "シナモン", "200", "300")
			.values("12", "メープル", "200", "300")
			.values("13", "きなこ", "200", "300")
			.values("14", "ココナッツ", "200", "300")
			.values("15", "アイス", "200", "300")
			.values("16", "マシュマロ", "200", "300")
			.values("17", "タピオカ", "200", "300")
			.values("18", "ナッツ", "200", "300")
			.values("19", "ココアパウダー", "200", "300")
			.values("20", "バニラシロップ", "200", "300")
			.build();

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
		Operation ops = Operations.sequenceOf(DELETE_ALL, INSERT);
		DbSetup dbSetup = new DbSetup(dest, ops);
		dbSetup.launch();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Autowired
	private ToppingsRepository toppingsRepository;

	@Test
	public void findAllの正常系テスト() {
		List<Topping> toppingList = toppingsRepository.findAll();
		Integer id = 1;
		Integer priceM = 200;
		Integer priceL = 300;
		assertEquals("IDが１の時、エラーが発生", id, toppingList.get(0).getId());
		assertEquals("IDが１の時、エラーが発生", "コーヒークリーム", toppingList.get(0).getName());
		assertEquals("IDが１の時、エラーが発生", priceM, toppingList.get(0).getPriceM());
		assertEquals("IDが１の時、エラーが発生", priceL, toppingList.get(0).getPriceL());
		id = 2;
		priceM = 200;
		priceL = 300;
		assertEquals("IDが２の時、エラーが発生", id, toppingList.get(1).getId());
		assertEquals("IDが２の時、エラーが発生", "低脂肪牛乳", toppingList.get(1).getName());
		assertEquals("IDが２の時、エラーが発生", priceM, toppingList.get(1).getPriceM());
		assertEquals("IDが２の時、エラーが発生", priceL, toppingList.get(1).getPriceL());
	}

}
