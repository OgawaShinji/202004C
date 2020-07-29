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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.domain.OrderItem;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderItemsRepositoryTest {
	
	private static final RowMapper<OrderItem> ORDER_ITEM_ROW_MAPPER = (rs, i) -> {
        OrderItem orderItem = new OrderItem();

        orderItem.setId(rs.getInt("id"));
        orderItem.setItemId(rs.getInt("item_id"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        char[] chars = rs.getString("size").toCharArray();
        orderItem.setSize(chars[0]);
        return orderItem;
    };
    
    private String selectSql = "SELECT id, item_id, order_id, quantity, size FROM order_items";
    private static final Integer TEST_ID = 1;
    private static final Integer TEST_ITEM_ID = 3;
    private static final Integer TEST_ORDER_ID = 3;
    private static final Integer TEST_NEW_ORDER_ID = 4;
    private static final Integer TEST_QUANTITY = 1;
    private static final Integer TEST_QUANTITY_FOR_MINUSUPDATE = 0;
    private static final Integer TEST_QUANTITY_FOR_PLUSUPDATE = 2;
    private static final char TEST_SIZE = 'M';

	public static final Operation DELETE_ALL = Operations.deleteAllFrom("order_items");
	
	private OrderItem orderItemTest = new OrderItem();

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
	
	@Autowired
	private NamedParameterJdbcTemplate template;

	@Test
	public void findIdByItemIdAndOrderIdAndSizeの正常系() {
		orderItemTest.setItemId(TEST_ITEM_ID);
		orderItemTest.setOrderId(TEST_ORDER_ID);
		orderItemTest.setSize(TEST_SIZE);
		List<Integer> itemIdList = orderItemsRepository.findIdByItemIdAndOrderIdAndSize(orderItemTest);
		Integer id = 1;
		assertEquals("idが違います", id, itemIdList.get(0));
	}
	@Test
	public void findOrderItemByIdの正常系() {
		OrderItem orderItem = orderItemsRepository.findOrderItemById(1);
		Integer id = 1;
		Integer itemId = 3;
		Integer orderId = 3;
		Integer quantity = 1;
		Character size = 'M';
		assertEquals("idが違います", id, orderItem.getId());
		assertEquals("itemIdが違います", itemId, orderItem.getItemId());
		assertEquals("orderIdが違います", orderId, orderItem.getOrderId());
		assertEquals("quantityが違います", quantity, orderItem.getQuantity());
		assertEquals("sizeが違います", size, orderItem.getSize());
	}
	
	@Test
	 public void updateMinusQuantityByIdの正常系() {
		Integer firstQuantityInDB = 1;
		this.orderItemTest.setId(TEST_ID);
		this.orderItemTest.setItemId(TEST_ITEM_ID);
		this.orderItemTest.setOrderId(TEST_ORDER_ID);
		this.orderItemTest.setQuantity(TEST_QUANTITY_FOR_MINUSUPDATE);
		this.orderItemTest.setSize(TEST_SIZE);
		orderItemsRepository.updateMinusQuantityById(TEST_ID);
		List<OrderItem> items = template.query(selectSql, ORDER_ITEM_ROW_MAPPER);
		Integer quantityForTest = firstQuantityInDB-TEST_QUANTITY;
		assertEquals("idが違います", this.orderItemTest.getId(), items.get(0).getId());
		assertEquals("itemIdが違います", this.orderItemTest.getItemId(), items.get(0).getItemId());
		assertEquals("orderIdが違います", this.orderItemTest.getOrderId(), items.get(0).getOrderId());
		assertEquals("quantityが違います", quantityForTest, items.get(0).getQuantity());
		assertEquals("sizeが違います", this.orderItemTest.getSize(), items.get(0).getSize());
	}
	@Test
	public void insertOrderItemsの正常系 (){
		this.orderItemTest.setId(TEST_ID);
		this.orderItemTest.setItemId(TEST_ITEM_ID);
		this.orderItemTest.setOrderId(TEST_ORDER_ID);
		this.orderItemTest.setQuantity(TEST_QUANTITY);
		this.orderItemTest.setSize(TEST_SIZE);
		orderItemsRepository.insertOrderItems(orderItemTest);
		List<OrderItem> items = template.query(selectSql, ORDER_ITEM_ROW_MAPPER);
        assertEquals("idが違います", this.orderItemTest.getId(), items.get(0).getId());
		assertEquals("itemIdが違います", this.orderItemTest.getItemId(), items.get(0).getItemId());
		assertEquals("orderIdが違います", this.orderItemTest.getOrderId(), items.get(0).getOrderId());
		assertEquals("quantityが違います", this.orderItemTest.getQuantity(), items.get(0).getQuantity());
		assertEquals("sizeが違います", this.orderItemTest.getSize(), items.get(0).getSize());
	}
    @Test
	public void deleteOrderItemsByIdの正常系() {
    	this.orderItemTest.setId(TEST_ID);
		this.orderItemTest.setItemId(TEST_ITEM_ID);
		this.orderItemTest.setOrderId(TEST_ORDER_ID);
		this.orderItemTest.setQuantity(TEST_QUANTITY);
		this.orderItemTest.setSize(TEST_SIZE);
		orderItemsRepository.deleteOrderItemsById(TEST_ID);
		try {
			List<OrderItem> orderItemList = template.query(selectSql,  ORDER_ITEM_ROW_MAPPER);
			orderItemList.get(0);
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
			Integer id = 1;
			assertEquals("idが違います", 1, 1 );
		}
    }
    @Test
    public void updateOrderItemsOrderIdByOrderIdの正常系() {
    	this.orderItemTest.setId(TEST_ID);
		this.orderItemTest.setItemId(TEST_ITEM_ID);
		this.orderItemTest.setOrderId(TEST_NEW_ORDER_ID);
		this.orderItemTest.setQuantity(TEST_QUANTITY);
		this.orderItemTest.setSize(TEST_SIZE);
		orderItemsRepository.updateOrderItemsOrderIdByOrderId(TEST_ORDER_ID, TEST_NEW_ORDER_ID);
		List<OrderItem> items = template.query(selectSql, ORDER_ITEM_ROW_MAPPER);
        assertEquals("idが違います", this.orderItemTest.getId(), items.get(0).getId());
		assertEquals("itemIdが違います", this.orderItemTest.getItemId(), items.get(0).getItemId());
		assertEquals("orderIdが違います", this.orderItemTest.getOrderId(), items.get(0).getOrderId());
		assertEquals("quantityが違います", this.orderItemTest.getQuantity(), items.get(0).getQuantity());
		assertEquals("sizeが違います", this.orderItemTest.getSize(), items.get(0).getSize());
    }
    @Test
    public void updateQuantityById() {
    	Integer firstQuantityInDB = 1;
    	this.orderItemTest.setId(TEST_ID);
		this.orderItemTest.setItemId(TEST_ITEM_ID);
		this.orderItemTest.setOrderId(TEST_ORDER_ID);
		this.orderItemTest.setQuantity(TEST_QUANTITY_FOR_PLUSUPDATE);
		this.orderItemTest.setSize(TEST_SIZE);
		orderItemTest.setId(TEST_ID);
		orderItemsRepository.updateQuantityById(orderItemTest);
		List<OrderItem> items = template.query(selectSql, ORDER_ITEM_ROW_MAPPER);
		Integer quantityForTest = firstQuantityInDB+TEST_QUANTITY_FOR_PLUSUPDATE;
        assertEquals("idが違います", this.orderItemTest.getId(), items.get(0).getId());
		assertEquals("itemIdが違います", this.orderItemTest.getItemId(), items.get(0).getItemId());
		assertEquals("orderIdが違います", this.orderItemTest.getOrderId(), items.get(0).getOrderId());
		assertEquals("quantityが違います", quantityForTest, items.get(0).getQuantity());
		assertEquals("sizeが違います", this.orderItemTest.getSize(), items.get(0).getSize());
    }
}
