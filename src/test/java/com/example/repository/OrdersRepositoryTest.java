package com.example.repository;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.domain.Item;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrdersRepositoryTest {


    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<Order> ORDER_ROW_MAPPER = (rs, i) -> {

        Order order = new Order();

        order.setId(rs.getInt("ordId"));
        order.setUserId(rs.getInt("ordUserId"));
        order.setStatus(rs.getInt("ordStatus"));
        order.setTotalPrice(rs.getInt("ordTotalPrice"));
        order.setOrderDate(rs.getDate("ordOrderDate"));
        order.setDestinationName(rs.getString("ordDestName"));
        order.setDestinationEmail(rs.getString("ordDestEmail"));
        order.setDestinationZipcode(rs.getString("ordDestZip"));
        order.setDestinationAddress(rs.getString("ordDestAddress"));
        order.setDestinationTel(rs.getString("ordDestTel"));
        order.setDeliveryTime(rs.getTimestamp("ordDeliveryTime"));
        order.setPaymentMethod(rs.getInt("ordPayMeth"));

        OrderItem orderItem = new OrderItem();

        orderItem.setId(rs.getInt("oriId"));
        orderItem.setItemId(rs.getInt("oriItemId"));
        orderItem.setOrderId(rs.getInt("oriOrderId"));
        orderItem.setQuantity(rs.getInt("oriQuantity"));
//        char[] chars = rs.getString("oriSize").toCharArray();
//        orderItem.setSize(chars[0]);

        Item item = new Item();

        item.setId(rs.getInt("itmId"));
        item.setName(rs.getString("itmName"));
        item.setImagePass(rs.getString("itmImagePath"));
        item.setPriceM(rs.getInt("itmPriceM"));
        item.setPriceL(rs.getInt("itmPriceL"));

        orderItem.setItem(item);

        Topping topping = new Topping();

        topping.setName(rs.getString("topName"));
        topping.setPriceM(rs.getInt("topPriceM"));
        topping.setPriceL(rs.getInt("topPriceL"));

        OrderTopping orderTopping = new OrderTopping();

        orderTopping.setOrderItemId(rs.getInt("otpOrdItmId"));

        List<OrderTopping> orderToppingList = new ArrayList<>();
        orderTopping.setTopping(topping);
        orderToppingList.add(orderTopping);

        orderItem.setOrderToppingList(orderToppingList);

        List<OrderItem> orderItemList = new ArrayList<>();

        orderItemList.add(orderItem);

        order.setOrderItemList(orderItemList);

        return order;

    };

    private static final RowMapper<Integer> ORDERS_ID_ROW_MAPPER = (rs, i) -> {
        Integer orderId = rs.getInt("id");
        return orderId;
    };
    
    private static final RowMapper<Order> ORDERS_TEST_ROW_MAPPER = (rs, i) -> {
    	 Order testOrder = new Order();

         testOrder.setId(rs.getInt("id"));
         testOrder.setUserId(rs.getInt("user_id"));
         testOrder.setStatus(rs.getInt("status"));
         testOrder.setTotalPrice(rs.getInt("total_price"));
         testOrder.setOrderDate(rs.getDate("order_date"));
         testOrder.setDestinationName(rs.getString("destination_name"));
         testOrder.setDestinationEmail(rs.getString("destination_email"));
         testOrder.setDestinationZipcode(rs.getString("destination_zipcode"));
         testOrder.setDestinationAddress(rs.getString("destination_address"));
         testOrder.setDestinationTel(rs.getString("destination_tel"));
         testOrder.setDeliveryTime(rs.getTimestamp("delivery_time"));
         testOrder.setPaymentMethod(rs.getInt("payment_method"));
		
         return testOrder;
    };
        
    
    private String selectSql = "SELECT * from orders";
    private String sql = "SELECT ord.id AS ordId, ord.user_id AS ordUserId, ord.status AS ordStatus, ord.total_price AS ordTotalPrice,"
            + " ord.order_date AS ordOrderDate, ord.destination_name AS ordDestName, ord.destination_email AS ordDestEmail,"
            + " ord.destination_zipcode AS ordDestZip, ord.destination_address AS ordDestAddress, ord.destination_tel AS ordDestTel,"
            + " ord.delivery_time AS ordDeliveryTime, ord.payment_method AS ordPayMeth, ord.status AS ordStatus, ori.id AS oriId, ori.order_id AS oriOrderId,"
            + " ori.item_id AS oriItemId, ori.quantity AS oriQuantity, ori.size AS oriSize, itm.id AS itmId, itm.name AS itmName, itm.image_path AS itmImagePath,"
            + " itm.price_m AS itmPriceM, itm.price_l AS itmPriceL, top.name AS topName, top.price_m AS topPriceM, top.price_l AS topPriceL, otp.order_item_id AS otpOrdItmId"
            + " FROM orders AS ord LEFT OUTER JOIN order_items AS ori ON ori.order_id = ord.id LEFT OUTER JOIN users AS use on ord.user_id = use.id LEFT OUTER JOIN items as itm ON ori.item_id = itm.id"
            + " LEFT OUTER JOIN order_toppings AS otp ON ori.id = otp.order_item_id LEFT OUTER JOIN toppings AS top ON otp.topping_id = top.id WHERE ord.user_id = 1110111 AND status >= 1 ORDER BY ordId ASC";
    private static final Integer TEST_ID = 2;
    private static final Integer TEST_USER_ID = 1110111;
    private static final Integer TEST_BEFORE_USER_ID = 233333;
    private static final Integer TEST_STATUS = 1;
    private static final Integer TEST_TOTAL_PRICE = 250;
    private static final String TEST_DESTINATION_NAME = "澤口ありさ";
    private static final String TEST_DESTINATION_EMAIL = "ddddd@gg";
    private static final String TEST_DESTINATION_ZIPCODE = "1660003";
    private static final String TEST_DESTINATION_ADDRESS = "東京都杉並区";
    private static final String TEST_DESTINATION_TEL = "000-0000-0000";
    private static final Integer TEST_PAYMENT_METHOD = 1;
    
	public static final Operation DELETE_TOPPINGS = Operations.deleteAllFrom("toppings");
	
	public static final Operation DELETE_ORDERS = Operations.deleteAllFrom("orders");
	
	public static final Operation DELETE_ORDER_ITEMS = Operations.deleteAllFrom("order_items");
	
	public static final Operation DELETE_ORDER_TOPPINGS = Operations.deleteAllFrom("order_toppings");
	
	public static final Operation DELETE_ITEMS = Operations.deleteAllFrom("items");
	
	public static final Operation DELETE_USERS = Operations.deleteAllFrom("users");
	
	private Order orderTest = new Order();
	
	public static final Operation INSERT_ORDERS = Operations.insertInto("orders")
			
			.columns("id", "user_id", "status", "total_price", "order_date", "destination_name", "destination_email", "destination_zipcode", "destination_address", "destination_tel", "delivery_time", "payment_method")
			.values("1", "9625245", "0", "250", null, null, null, null, null, null, null, null)
			.values("2", "1110111", "1", "250", "2020-07-22", "澤口ありさ", "ddddd@gg", "1660003", "東京都杉並区", "000-0000-0000", "2020-07-22 11:00:00", "1")
			.build();

	public static final Operation INSERT_TOPPINGS = Operations.insertInto("toppings")
			.columns("id", "name", "price_m", "price_l")
			.values("1", "コーヒークリーム", "200", "300")
			.build();
	
	public static final Operation INSERT_ORDER_ITEMS = Operations.insertInto("order_items")
			.columns("id", "item_id", "order_id", "quantity", "size")
			.values("1", "3", "3", "1", "M")
			.build();
	
	public static final Operation INSERT_ORDER_TOPPINGS = Operations.insertInto("order_toppings")
			.columns("id", "topping_id", "order_item_id")
			.values("1", "2", "1")
			.build();
	
	public static final Operation INSERT_ITEMS = Operations.insertInto("items")
			.columns("id", "name", "description", "price_m", "price_l", "image_path", "categoryid")
			.values("1", "Gorgeous4サンド", "人気の定番具材「ハム」と「チキン」をセットにした食べごたえ抜群のサンドイッチです。", "480", "700", "1.jpg", "2")
			.values("2", "エスプレッソフラペチーノ",
					"ひと口目に感じるエスプレッソは「リストレット」という方法で抽出した力強い香りと優しい苦味を、ふた口目は全体を混ぜて、こだわりのクリームから来るアフォガートのような味わいをお楽しみください。リフレッシュしたい時や、ほっとひと息つきたい時にも、何度でも飲みたくなるフラペチーノです。",
					"530", "650", "2.jpg", "1")
			.values("3", "Specialキャラメルドーナッツ",
					"ドーナツ生地の中に、なめらかで風味豊かなキャラメルフィリングを入れ、仕上げにキャラメルのビター感と香ばしさが楽しめるキャラメルコーティングをかけました。", "250", "500",
					"3.jpg", "2")
			.values("4", "チョコクッキー",
					"ソフトな食感のクッキー生地には、小麦の香ばしさが感じられるよう全粒粉を入れ、砂糖の一部にはブラウンシュガーを使い、コクのある甘さをプラスしています。風味豊かなスターバックスオリジナルのチョコレートチャンクがごろごろ入っていて、どこを食べてもチョコレートの味わいを存分に楽しめます。ショートサイズのマグカップの上に乗せられるくらいのサイズは、コーヒーと一緒に楽しむのにもぴったりです。",
					"190", "300", "4.jpg", "2")
			.values("5", "カフェモカ", "エスプレッソにほろ苦いチョコレートシロップとミルクを加え、ホットビバレッジにはホイップクリームをトッピング。ちょっとした贅沢を楽しみたい方におすすめです。",
					"400", "520", "5.jpg", "1")
			.values("6", "カフェラテ",
					"最も人気のあるエスプレッソ ビバレッジ。リッチなエスプレッソにスチームミルクを注ぎ、フォームミルクを丁寧にトッピングしました。ミルキーな味わいで気持ちを温かくしてくれます。", "340",
					"460", "6.jpg", "1")
			.values("7", "カプチーノ", "リッチなエスプレッソに一気にミルクを注ぐことで、一口飲んだときからコーヒー感が感じられるビバレッジです。ベルベットのようにきめ細かいフォームミルクをお楽しみください。",
					"340", "460", "7.jpg", "1")
			.values("8", "キャラメルマキアート",
					"バニラシロップとスチームミルクのコンビネーションになめらかなフォームミルクをたっぷりのせ、その上からエスプレッソを注いでアクセントを付けました。仕上げにオリジナルキャラメルソースをトッピングしています。",
					"390", "510", "8.jpg", "1")
			.values("9", "キャラメルフラペチーノ",
					"コーヒー、ミルク、キャラメルシロップを氷とブレンドした、多くのお客様に愛されているフローズン ビバレッジです。トッピングしたホイップクリームとキャラメルソースと混ぜながら、豊かでほんのり香ばしいキャラメルの風味をお楽しみください。",
					"490", "570", "9.jpg", "1")
			.values("10", "バニラ クリーム フラペチーノ",
					"ミルクとバニラシロップを氷とブレンドし、ホイップクリームをトッピングした、クリーミーな味わいのフローズン ビバレッジ。真っ白な見た目も爽やかです。ミルクとバニラシロップというシンプルな組み合わせはいろいろなカスタマイズとも好相性。",
					"490", "570", "10.jpg", "1")
			.values("11", "ダークモカフラペチーノ",
					"コーヒー、ミルク、ダークチョコレートパウダー、そして人気のチョコレートチップを氷とブレンドした、チョコレートラバーズに人気のフローズンビバレッジ。コーヒーとダークチョコレートのほろ苦い味わいと、チョコレートチップの食感が織り成すハーモニーは、ブラックコーヒーファンにも支持されています。",
					"500", "580", "11.jpg", "1")
			.values("12", "抹茶クリームフラペチーノ", "世界中で様々な形で飲用されている抹茶ですが、スターバックスではミルクと甘みを加えて氷でブレンドしたリフレッシングなフラペチーノに仕上げました。",
					"490", "570", "12.jpg", "1")
			.values("13", "ドリップコーヒー",
					"世界中のコーヒー産地から厳選された高品質のアラビカ種コーヒー豆を使用したスターバックスの定番商品です。バラエティあふれるコーヒー豆を通して、スターバックスのコーヒージャーニーをお楽しみください。異なるローストレベルのコーヒーを日替わりでご用意していますので、お気に入りの1杯を見つけてみませんか。",
					"290", "410", "13.jpg", "1")
			.values("14", "アイスコーヒー",
					"熱を加えずに14時間かけてゆっくりと水で抽出したコールドブリュー コーヒー。香り高い風味が引き出されるよう、特別にブレンド、ローストしたコーヒー豆を使用しています。豊かな味わいとなめらかな口あたりをお楽しみください。",
					"330", "450", "14.jpg", "1")
			.values("15", "アメリカン", "エスプレッソに熱いお湯を注いだ、すっきりとしたのどごしのコーヒーです。ドリップ コーヒーのお好きな方にもおすすめです。", "310", "430",
					"15.jpg", "1")
			.values("16", "エスプレッソ",
					"キャラメルのような甘く力強い味とナッツを感じさせる後味。スターバックスのすべてのエスプレッソ ビバレッジに使われているエスプレッソです。どうぞ、お早めにお召し上がりください。", "310",
					"350", "16.jpg", "1")
			.values("17", "ナッティホワイトモカ",
					"ホワイトチョコレートとヘーゼルナッツに香り高いエスプレッソを加えた風味豊かなホワイト モカ。ホイップクリームをツリーに見立て、ナッツ&ホワイトチョコレートソースのリボンと、3色のチョコレート、シルバーのアラザンをイルミネーションのようにちりばめました。ホリデーシーズンにぴったりのあたたかな一杯で、特別なひと時をお楽しみください。",
					"450", "570", "17.jpg", "1")
			.values("18", "ジンジャーブレッドラテ",
					"スターバックスのホリデーといえばやっぱりジンジャーブレッド ラテ、という人も多いのではないでしょうか。ジンジャーブレッドクッキーをイメージした、ほんのり甘くてスパイシーな風味は、この時期にしか味わえない特別なビバレッジです。体の中からじんわりとあたためてくれる一杯で、ほっとリラックスしたひと時をお過ごしください。",
					"450", "570", "18.jpg", "1")
			.build();
	
	public static final Operation INSERT_USERS = Operations.insertInto("users")
			.columns("id", "name", "email", "password", "zipcode", "address", "telephone")
			.values("3", "a", "1@1", "$2a$10$1WGGheGBeF1.rSIQ6CvNp.VV7ol8JkA88lgy26R2ES16f5q8js0ei", "1660003", "高円寺", "09011111111")
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
		Operation ops = Operations.sequenceOf(DELETE_TOPPINGS, DELETE_ORDERS, DELETE_ORDER_ITEMS, DELETE_ORDER_TOPPINGS, DELETE_ITEMS, DELETE_USERS,  INSERT_ORDERS, INSERT_TOPPINGS, INSERT_ORDER_ITEMS, INSERT_ORDER_TOPPINGS, INSERT_ITEMS, INSERT_USERS);
		DbSetup dbSetup = new DbSetup(dest, ops);
		dbSetup.launch();
	}

	@After
	public void tearDown() throws Exception {
	}
	@Autowired
	private OrdersRepository ordersRepository;

	@Test
	public void findIdByUserIdAndStatus() {
		this.orderTest.setUserId(TEST_USER_ID);
		this.orderTest.setStatus(TEST_STATUS);
		Integer orderId = ordersRepository.findIdByUserIdAndStatus(orderTest);
		Integer id = 2;
		assertEquals("idが違います", id, orderId);
	}
	@Test
	 public void insertOrdersForAddShoppingCart() {
		this.orderTest.setId(TEST_ID);
		this.orderTest.setUserId(TEST_USER_ID);
		this.orderTest.setStatus(TEST_STATUS);
		this.orderTest.setTotalPrice(TEST_TOTAL_PRICE);
		ordersRepository.insertOrdersForAddShoppingCart(orderTest);
		List<Integer> id = template.query(selectSql, ORDERS_ID_ROW_MAPPER);
		assertEquals("idが違います", this.orderTest.getId(), id.get(1));
	}
	@Test
	public void updateOrdersForPayment() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String date= "2020-07-22";
			Date testDate = df.parse(date);
			
			SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String dateTime = "2020-07-22 11:00:00";
			Date timeDate = sdf.parse(dateTime);
			Timestamp deliveryTime = new Timestamp(timeDate.getTime());
			this.orderTest.setUserId(TEST_USER_ID);
			this.orderTest.setStatus(TEST_STATUS);
			this.orderTest.setOrderDate(testDate);
			this.orderTest.setTotalPrice(TEST_TOTAL_PRICE);
			this.orderTest.setDestinationName(TEST_DESTINATION_NAME);
			this.orderTest.setDestinationEmail(TEST_DESTINATION_EMAIL);
			this.orderTest.setDestinationZipcode(TEST_DESTINATION_ZIPCODE);
			this.orderTest.setDestinationAddress(TEST_DESTINATION_ADDRESS);
			this.orderTest.setDestinationTel(TEST_DESTINATION_TEL);
			this.orderTest.setDeliveryTime(deliveryTime);
			this.orderTest.setPaymentMethod(TEST_PAYMENT_METHOD);
			ordersRepository.updateOrdersForPayment(orderTest);
			List<Order> orderList = template.query(selectSql, ORDERS_TEST_ROW_MAPPER);
			assertEquals("userIdが違います", this.orderTest.getUserId(), orderList.get(1).getUserId());
			assertEquals("statusが違います", this.orderTest.getStatus(), orderList.get(1).getStatus());
			assertEquals("orderDateが違います", this.orderTest.getOrderDate(), orderList.get(1).getOrderDate());
			assertEquals("totalPriceが違います", this.orderTest.getTotalPrice(), orderList.get(1).getTotalPrice());
			assertEquals("destinationNameが違います", this.orderTest.getDestinationName(), orderList.get(1).getDestinationName());
			assertEquals("destinationEmailが違います", this.orderTest.getDestinationEmail(), orderList.get(1).getDestinationEmail());
			assertEquals("destinationZipcodeが違います", this.orderTest.getDestinationZipcode(), orderList.get(1).getDestinationZipcode());
			assertEquals("destinationAddressが違います", this.orderTest.getDestinationAddress(), orderList.get(1).getDestinationAddress());
			assertEquals("destinationTelが違います", this.orderTest.getDestinationTel(), orderList.get(1).getDestinationTel());
			assertEquals("deliveryTimeが違います", this.orderTest.getDeliveryTime(), orderList.get(1).getDeliveryTime());
			assertEquals("paymentMethodが違います", this.orderTest.getPaymentMethod(), orderList.get(1).getPaymentMethod());
		} catch (ParseException e) {
	        assertEquals("error", 1, 2);
		}
		}
	@Test
	public void updateUserId() {
		this.orderTest.setId(TEST_ID);
		this.orderTest.setUserId(TEST_USER_ID);
		this.orderTest.setStatus(TEST_STATUS);
		this.orderTest.setTotalPrice(TEST_TOTAL_PRICE);
		ordersRepository.updateUserId(orderTest, TEST_BEFORE_USER_ID);
		List<Order> orderList = template.query(selectSql, ORDERS_TEST_ROW_MAPPER);
		assertEquals("userIdが違います", this.orderTest.getUserId(), orderList.get(1).getUserId());
	}
	@Test
	public void deleteOrderById() {
		this.orderTest.setId(TEST_ID);
		this.orderTest.setUserId(TEST_USER_ID);
		this.orderTest.setStatus(TEST_STATUS);
		this.orderTest.setTotalPrice(TEST_TOTAL_PRICE);
		ordersRepository.deleteOrderById(orderTest);
		try {
			List<Integer> id = template.query(selectSql, ORDERS_ID_ROW_MAPPER);
			assertEquals("idが違います", this.orderTest.getId(), id.get(1));
		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
		}
	}
	@Test
	public void updatePlusTotalPrice() {
		Integer firstTotalPriceInDB = 0;
		this.orderTest.setId(TEST_ID);
		this.orderTest.setUserId(TEST_USER_ID);
		this.orderTest.setStatus(TEST_STATUS);
		this.orderTest.setTotalPrice(TEST_TOTAL_PRICE);
		ordersRepository.updatePlusTotalPrice(orderTest);
		List<Order> orderList = template.query(selectSql, ORDERS_TEST_ROW_MAPPER);
		Integer totalPriceForTest = firstTotalPriceInDB + TEST_TOTAL_PRICE;
		assertEquals("totalPriceが違います", totalPriceForTest, orderList.get(0).getTotalPrice());
		assertEquals("userIdが違います", this.orderTest.getUserId(), orderList.get(1).getUserId());
	}
	@Test
	public void updateMinusTotalPrice() {
		Integer firstTotalPriceInDB = 500;
		this.orderTest.setId(TEST_ID);
		this.orderTest.setUserId(TEST_USER_ID);
		this.orderTest.setStatus(TEST_STATUS);
		this.orderTest.setTotalPrice(TEST_TOTAL_PRICE);
		ordersRepository.updateMinusTotalPrice(orderTest);
		List<Order> orderList = template.query(selectSql, ORDERS_TEST_ROW_MAPPER);
		Integer totalPriceForTest = firstTotalPriceInDB - TEST_TOTAL_PRICE;
		assertEquals("totalPriceが違います", totalPriceForTest, orderList.get(1).getTotalPrice());
		assertEquals("userIdが違います", this.orderTest.getUserId(), orderList.get(1).getUserId());
	}
	@Test
	public void findItemsByUserIdAndStatusOverThan1() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String date= "2020-07-22";
			Date testDate = df.parse(date);
			
			SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String dateTime = "2020-07-22 11:00:00";
			Date timeDate = sdf.parse(dateTime);
			Timestamp deliveryTime = new Timestamp(timeDate.getTime());
			this.orderTest.setId(TEST_ID);
			this.orderTest.setUserId(TEST_USER_ID);
			this.orderTest.setStatus(TEST_STATUS);
			this.orderTest.setTotalPrice(TEST_TOTAL_PRICE);
			this.orderTest.setStatus(TEST_STATUS);
			this.orderTest.setOrderDate(testDate);
			this.orderTest.setTotalPrice(TEST_TOTAL_PRICE);
			this.orderTest.setDestinationName(TEST_DESTINATION_NAME);
			this.orderTest.setDestinationEmail(TEST_DESTINATION_EMAIL);
			this.orderTest.setDestinationZipcode(TEST_DESTINATION_ZIPCODE);
			this.orderTest.setDestinationAddress(TEST_DESTINATION_ADDRESS);
			this.orderTest.setDestinationTel(TEST_DESTINATION_TEL);
			this.orderTest.setDeliveryTime(deliveryTime);
			this.orderTest.setPaymentMethod(TEST_PAYMENT_METHOD);
			ordersRepository.findItemsByUserIdAndStatusOverThan1(TEST_ID);
			SqlParameterSource param = new MapSqlParameterSource();
			List<Order> orderList = template.query(sql, param, ORDER_ROW_MAPPER);
			assertEquals("idが違います", this.orderTest.getId(), orderList.get(0).getId());
			assertEquals("userIdが違います", this.orderTest.getUserId(), orderList.get(0).getUserId());
			assertEquals("statusが違います", this.orderTest.getStatus(), orderList.get(0).getStatus());
			assertEquals("orderDateが違います", this.orderTest.getOrderDate(), orderList.get(0).getOrderDate());
			assertEquals("totalPriceが違います", this.orderTest.getTotalPrice(), orderList.get(0).getTotalPrice());
			assertEquals("destinationNameが違います", this.orderTest.getDestinationName(), orderList.get(0).getDestinationName());
			assertEquals("destinationEmailが違います", this.orderTest.getDestinationEmail(), orderList.get(0).getDestinationEmail());
			assertEquals("destinationZipcodeが違います", this.orderTest.getDestinationZipcode(), orderList.get(0).getDestinationZipcode());
			assertEquals("destinationAddressが違います", this.orderTest.getDestinationAddress(), orderList.get(0).getDestinationAddress());
			assertEquals("destinationTelが違います", this.orderTest.getDestinationTel(), orderList.get(0).getDestinationTel());
			assertEquals("deliveryTimeが違います", this.orderTest.getDeliveryTime(), orderList.get(0).getDeliveryTime());
			assertEquals("paymentMethodが違います", this.orderTest.getPaymentMethod(), orderList.get(0).getPaymentMethod());
		} catch (ParseException e) {
	        assertEquals("error", 1, 2);
		}
		}
	@Test
	public void UpdateWhoPurchaseTheItemstoStatus1() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String date= "2020-07-22";
			Date testDate = df.parse(date);
			SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String dateTime = "2020-07-22 11:00:00";
			Date timeDate = sdf.parse(dateTime);
			Timestamp deliveryTime = new Timestamp(timeDate.getTime());
			this.orderTest.setId(TEST_ID);
			this.orderTest.setUserId(TEST_USER_ID);
			this.orderTest.setStatus(TEST_STATUS);
			this.orderTest.setTotalPrice(TEST_TOTAL_PRICE);
			this.orderTest.setStatus(TEST_STATUS);
			this.orderTest.setOrderDate(testDate);
			this.orderTest.setTotalPrice(TEST_TOTAL_PRICE);
			this.orderTest.setDestinationName(TEST_DESTINATION_NAME);
			this.orderTest.setDestinationEmail(TEST_DESTINATION_EMAIL);
			this.orderTest.setDestinationZipcode(TEST_DESTINATION_ZIPCODE);
			this.orderTest.setDestinationAddress(TEST_DESTINATION_ADDRESS);
			this.orderTest.setDestinationTel(TEST_DESTINATION_TEL);
			this.orderTest.setDeliveryTime(deliveryTime);
			this.orderTest.setPaymentMethod(TEST_PAYMENT_METHOD);
			ordersRepository.UpdateWhoPurchaseTheItemstoStatus1(orderTest, TEST_USER_ID);;
			List<Order> orderList = template.query(selectSql, ORDERS_TEST_ROW_MAPPER);
			Integer statusAfterUpdate = 1;
			assertEquals("idが違います", this.orderTest.getId(), orderList.get(1).getId());
			assertEquals("userIdが違います", this.orderTest.getUserId(), orderList.get(1).getUserId());
			assertEquals("statusが違います", statusAfterUpdate, orderList.get(1).getStatus());
			System.out.println(this.orderTest.getOrderDate());
			assertEquals("orderDateが違います", this.orderTest.getOrderDate(), orderList.get(1).getOrderDate());
			assertEquals("totalPriceが違います", this.orderTest.getTotalPrice(), orderList.get(1).getTotalPrice());
			assertEquals("destinationNameが違います", this.orderTest.getDestinationName(), orderList.get(1).getDestinationName());
			assertEquals("destinationEmailが違います", this.orderTest.getDestinationEmail(), orderList.get(1).getDestinationEmail());
			assertEquals("destinationZipcodeが違います", this.orderTest.getDestinationZipcode(), orderList.get(1).getDestinationZipcode());
			assertEquals("destinationAddressが違います", this.orderTest.getDestinationAddress(), orderList.get(1).getDestinationAddress());
			assertEquals("destinationTelが違います", this.orderTest.getDestinationTel(), orderList.get(1).getDestinationTel());
			assertEquals("deliveryTimeが違います", this.orderTest.getDeliveryTime(), orderList.get(1).getDeliveryTime());
			assertEquals("paymentMethodが違います", this.orderTest.getPaymentMethod(), orderList.get(1).getPaymentMethod());
		} catch (ParseException e) {
	        assertEquals("error", 1, 2);
		}
	}
}
