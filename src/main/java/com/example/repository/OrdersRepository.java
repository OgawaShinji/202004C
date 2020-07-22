package com.example.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.domain.Item;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class OrdersRepository {

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
        char[] chars = rs.getString("oriSize").toCharArray();
        orderItem.setSize(chars[0]);

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

    /**
     * userIdを指定してそのuser_idでstatus=0のid取得
     * 
     * @param order
     * @return 取得したid
     */
    public Integer findIdByUserIdAndStatus(Order order) {
        String sql = "SELECT id FROM orders WHERE user_id=:userId AND status=:status ORDER BY id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", order.getUserId()).addValue("status",
                order.getStatus());
        List<Integer> ordersId = template.query(sql, param, ORDERS_ID_ROW_MAPPER);
        if (ordersId.size() == 0) {
            return null;
        } else {
            return ordersId.get(0);
        }

    }

    /**
     * カートに商品が追加されたときにOrdersテーブルにuser_id,status,total_priceインサートする
     * 
     * @param order return 自動採番されたid
     */
    public Integer insertOrdersForAddShoppingCart(Order order) {
        String insertSql = "INSERT INTO orders(user_id, status, total_price)"
                + " VALUES(:userId, :status, :totalPrice) RETURNING id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", order.getUserId())
                .addValue("status", order.getStatus()).addValue("totalPrice", order.getTotalPrice());
        Integer id = template.queryForObject(insertSql, param, Integer.class);
        return id;
    }

    /**
     * 決済処理時にOrdersテーブルのuser_idが一致するstatus=0のデータをupdate
     * 
     * @param order
     */
    public void updateOrdersForPayment(Order order) {
        String insertSql = "UPDATE orders SET status=:status, total_price=:totalPrice, order_date=:orderDate, destination_name=:destinationName, destination_email=:destinationEmail, destination_zipcode=:destinationZipcode,"
                + " destination_address=:destinationAddress, destination_tel=:destinationTel, delivery_time=:deliveryTime, payment_method=:paymentMethod"
                + "WHERE user_id=:userId AND status=0";
        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", order.getUserId())
                .addValue("status", order.getStatus()).addValue("totalPrice", order.getTotalPrice())
                .addValue("orderdate", order.getOrderDate()).addValue("destinationName", order.getDestinationName())
                .addValue("destinationEmail", order.getDestinationEmail())
                .addValue("destinationZipcode", order.getDestinationZipcode())
                .addValue("destinationAddress", order.getDestinationAddress())
                .addValue("destinationTel", order.getDestinationTel()).addValue("deliveryTime", order.getDeliveryTime())
                .addValue("paymentMethod", order.getPaymentMethod());
        template.update(insertSql, param);
    }

    /**
     * ログイン前にカートに商品が既にある場合今まで使用していた仮userIdをログインユーザーのuserIdにupdateする
     * 
     * @param order
     * @param beforeUserId
     */
    public void updateUserId(Order order, Integer beforeUserId) {
        String updateSql = "UPDATE orders SET user_id=:userId WHERE user_id=:beforeUserId AND status=:status";
        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", order.getUserId())
                .addValue("beforeUserId", beforeUserId).addValue("status", order.getStatus());
        template.update(updateSql, param);
    }

    /**
     * Ordersテーブルから該当するidの買い物情報を削除する
     */
    public void deleteOrderById(Order order) {
        String deleteSql = "DELETE FROM orders WHERE id = :orderId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("orderId", order.getId());
        template.update(deleteSql, param);
    }

    /**
     * カートに商品を追加した際にtotal_priceを足すメソッド
     * 
     * @param order
     */
    public void updatePlusTotalPrice(Order order) {
        String sql = "UPDATE orders SET total_price = total_price + :totalPrice WHERE user_id=:userId AND status=0";
        SqlParameterSource param = new MapSqlParameterSource().addValue("totalPrice", order.getTotalPrice())
                .addValue("userId", order.getUserId());
        template.update(sql, param);
    }

    /**
     * カートから商品を削除されたときにtotal_priceを引くメソッド
     * 
     * @param order
     */
    public void updateMinusTotalPrice(Order order) {
        String sql = "UPDATE orders SET total_price = total_price-:totalPrice WHERE user_id=:userId AND status=0";
        SqlParameterSource param = new MapSqlParameterSource().addValue("totalPrice", order.getTotalPrice())
                .addValue("userId", order.getUserId());
        template.update(sql, param);
    }

    /**
     * @param userId
     * @return List<OrderItem>
     */
    public List<Order> findItemsByUserIdAndStatusOverThan1(Integer userId) {

        String sql = "SELECT ord.id AS ordId, ord.user_id AS ordUserId, ord.status AS ordStatus, ord.total_price AS ordTotalPrice,"
                + " ord.order_date AS ordOrderDate, ord.destination_name AS ordDestName, ord.destination_email AS ordDestEmail,"
                + " ord.destination_zipcode AS ordDestZip, ord.destination_address AS ordDestAddress, ord.destination_tel AS ordDestTel,"
                + " ord.delivery_time AS ordDeliveryTime, ord.payment_method AS ordPayMeth, ord.status AS ordStatus, ori.id AS oriId, ori.order_id AS oriOrderId,"
                + " ori.item_id AS oriItemId, ori.quantity AS oriQuantity, ori.size AS oriSize, itm.id AS itmId, itm.name AS itmName, itm.image_path AS itmImagePath,"
                + " itm.price_m AS itmPriceM, itm.price_l AS itmPriceL, top.name AS topName, top.price_m AS topPriceM, top.price_l AS topPriceL, otp.order_item_id AS otpOrdItmId"
                + " FROM orders AS ord LEFT OUTER JOIN order_items AS ori ON ori.order_id = ord.id LEFT OUTER JOIN users AS use on ord.user_id = use.id LEFT OUTER JOIN items as itm ON ori.item_id = itm.id"
                + " LEFT OUTER JOIN order_toppings AS otp ON ori.id = otp.order_item_id LEFT OUTER JOIN toppings AS top ON otp.topping_id = top.id WHERE ord.user_id = :userId AND status >= 1 ORDER BY ordOrderDate ASC";

        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);

        List<Order> orderList = template.query(sql, param, ORDER_ROW_MAPPER);

        return orderList;

    }

    /**
     * @param userId
     * @return List<OrderItem>
     */
    public List<Order> findOrderItemsAndToppingsByUserId(Order orderHasUserIdAndStatus) {

        String sql = "SELECT ord.id AS ordId, ord.user_id AS ordUserId, ord.status AS ordStatus, ord.total_price AS ordTotalPrice,"
                + " ord.order_date AS ordOrderDate, ord.destination_name AS ordDestName, ord.destination_email AS ordDestEmail,"
                + " ord.destination_zipcode AS ordDestZip, ord.destination_address AS ordDestAddress, ord.destination_tel AS ordDestTel,"
                + " ord.delivery_time AS ordDeliveryTime, ord.payment_method AS ordPayMeth, ori.id AS oriId, ori.order_id AS oriOrderId,"
                + " ori.item_id AS oriItemId, ori.quantity AS oriQuantity, ori.size AS oriSize, itm.id AS itmId, itm.name AS itmName, itm.image_path AS itmImagePath,"
                + " itm.price_m AS itmPriceM, itm.price_l AS itmPriceL, top.name AS topName, top.price_m AS topPriceM, top.price_l AS topPriceL, otp.order_item_id AS otpOrdItmId"
                + " FROM orders AS ord LEFT OUTER JOIN order_items AS ori ON ori.order_id = ord.id LEFT OUTER JOIN users AS use on ord.user_id = use.id LEFT OUTER JOIN items as itm ON ori.item_id = itm.id"
                + " LEFT OUTER JOIN order_toppings AS otp ON ori.id = otp.order_item_id LEFT OUTER JOIN toppings AS top ON otp.topping_id = top.id WHERE ord.user_id = :userId AND status = :status";

        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", orderHasUserIdAndStatus.getUserId())
                .addValue("status", orderHasUserIdAndStatus.getStatus());

        List<Order> orderList = template.query(sql, param, ORDER_ROW_MAPPER);

        return orderList;
    }

    public void UpdateWhoPurchaseTheItemstoStatus1(Order order, Integer userId) {

        String sql = "UPDATE orders SET destination_name = :destinationName, destination_email = :destinationEmail, destination_zipcode = :destinationZipcode, destination_address = :destinationAddress, destination_tel = :destinationTel, order_date = :orderDate, delivery_time = :deliveryTime, payment_method = :paymentMethod, status = 1 WHERE user_id = :userId AND status = 0";

        SqlParameterSource param = new MapSqlParameterSource().addValue("destinationName", order.getDestinationName())
                .addValue("destinationEmail", order.getDestinationEmail())
                .addValue("destinationZipcode", order.getDestinationZipcode())
                .addValue("destinationAddress", order.getDestinationAddress())
                .addValue("destinationTel", order.getDestinationTel()).addValue("orderDate", order.getOrderDate())
                .addValue("deliveryTime", order.getDeliveryTime()).addValue("paymentMethod", order.getPaymentMethod())
                .addValue("userId", userId);

        template.update(sql, param);

    }

    public void UpdateWhoPurchaseTheItemstoStatus2(Order order, Integer userId) {

        String sql = "UPDATE orders SET destination_name = :destinationName, destination_email = :destinationEmail, destination_zipcode = :destinationZipcode, destination_address = :destinationAddress, destination_tel = :destinationTel, order_date = :orderDate, delivery_time = :deliveryTime, payment_method = :paymentMethod, status = 2 WHERE user_id = :userId AND status = 0";

        SqlParameterSource param = new MapSqlParameterSource().addValue("destinationName", order.getDestinationName())
                .addValue("destinationEmail", order.getDestinationEmail())
                .addValue("destinationZipcode", order.getDestinationZipcode())
                .addValue("destinationAddress", order.getDestinationAddress())
                .addValue("destinationTel", order.getDestinationTel()).addValue("orderDate", order.getOrderDate())
                .addValue("deliveryTime", order.getDeliveryTime()).addValue("paymentMethod", order.getPaymentMethod())
                .addValue("userId", userId);

        template.update(sql, param);

    }

}
