package com.example.repository;

import static org.junit.Assert.assertEquals;

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

import com.example.domain.OrderTopping;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

// springのtestを行う時は必須
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderToppingsRepositoryTest {
	
	public static final Operation DELETE_ALL = Operations.deleteAllFrom("order_toppings");
	
	public static final Operation INSERT = Operations.insertInto("order_toppings")
			.columns("id", "topping_id", "order_item_id")
			.values("1", "2", "1")
			.values("2", "1", "2")
			.values("3", "3", "2")
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
	private OrderToppingsRepository orderToppingsRepository;
	
	@Test
	public void findTopppingIdByOrderItemIdの正常系テスト() {
		List<Integer> toppingIdList = orderToppingsRepository.findTopppingIdByOrderItemId(1);
		Integer toppingId = 2;
		assertEquals("注文商品IDが１の時、エラーが発生します", toppingId, toppingIdList.get(0));
		toppingIdList = orderToppingsRepository.findTopppingIdByOrderItemId(2);
		Integer toppingId_1 = 1;
		Integer toppingId_2 = 3;
		assertEquals("注文商品IDが２の時、エラーが発生します", toppingId_1, toppingIdList.get(0));
		assertEquals("注文商品IDが２の時、エラーが発生します", toppingId_2, toppingIdList.get(1));
	}
	
	@Test
	public void insertOrderToppingsの正常系テスト() {
		OrderTopping orderTopping = new OrderTopping();
		orderTopping.setId(4);
		orderTopping.setToppingId(2);
		orderTopping.setOrderItemId(5);
		orderToppingsRepository.insertOrderToppings(orderTopping);
	}
}