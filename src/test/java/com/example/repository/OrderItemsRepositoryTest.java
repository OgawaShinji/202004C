package com.example.repository;

import static org.junit.Assert.*;

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

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderItemsRepositoryTest {

	public static final Operation DELETE_ALL = Operations.deleteAllFrom("order_items");

	public static final Operation INSERT = Operations.insertInto("order_items")
			.columns("id", "item_id", "order_id", "quantity", "size")
			.values("1", "3", "3", "1", "M")
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
	private OrderItemsRepository orderItemsRepository;

	@Test
	public void findIdByItemIdAndOrderIdAndSizeの正常系() {
		
	}

}
