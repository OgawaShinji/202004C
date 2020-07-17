package com.example.repository;

import java.util.ArrayList;
import java.util.List;

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
public class OrderItemsRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    private static final RowMapper<OrderItem> ORDER_ITEM_ROW_MAPPER = (rs, i) -> {

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

        return orderItem;

    };

    /**
     * @param userId
     * @return List<OrderItem>
     */
    public List<OrderItem> findOrderItemsAndToppingsByUserId(Integer userId) {

        String sql = "SELECT ori.id AS oriId, ori.order_id AS oriOrderId, ori.item_id AS oriItemId, ori.quantity AS oriQuantity, ori.size AS oriSize,"
                + " itm.id AS itmId, itm.name AS itmName, itm.image_path AS itmImagePath, itm.price_m AS itmPriceM, itm.price_l AS itmPriceL,"
                + " top.name AS topName, top.price_m AS topPriceM, top.price_l AS topPriceL, otp.order_item_id AS otpOrdItmId"
                + " FROM orders AS ord LEFT OUTER JOIN order_items AS ori ON ori.order_id = ord.id LEFT OUTER JOIN items as itm ON ori.item_id = itm.id"
                + " LEFT OUTER JOIN order_toppings AS otp ON ori.id = otp.order_item_id LEFT OUTER JOIN toppings AS top ON otp.topping_id = top.id WHERE ord.user_id = :userId AND ord.status = 0";

        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);

        List<OrderItem> orderItemsList = template.query(sql, param, ORDER_ITEM_ROW_MAPPER);

        return orderItemsList;

    }

    /**
     * order_itemsテーブルにインサートするメソッド
     * 
     * @param orderItems(itemId,orderId,quantity,size)
     * @return 自動採番されたid
     */
    public Integer insertOrderItems(OrderItem orderItems) {
        String insertSql = "INSERT INTO order_items(item_id, order_id, quantity, size) VALUES(:itemId, :orderId, :quantity, :size) RETURNING id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", orderItems.getItemId())
                .addValue("orderId", orderItems.getOrderId()).addValue("quantity", orderItems.getQuantity())
                .addValue("size", orderItems.getSize());
        Integer id = template.queryForObject(insertSql, param, Integer.class);
        return id;
    }

    /**
     * order_itemsテーブルからidを指定してデリートするメソッド
     * 
     * @param orderItemId
     */
    public void deleteOrderItemsById(Integer orderItemId) {
        String deleteSql = "DELETE FROM order_items WHERE id = :orderItemId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("orderItemId", orderItemId);
        template.update(deleteSql, param);
    }

    /**
     * order_itemsテーブルのorder_idを変更するメソッド
     * 
     * @param beforeOrdersId
     * @param afterOrdersId
     */
    public void updateOrderItemsOrderIdByOrderId(Integer beforeOrdersId, Integer afterOrdersId) {
        String updateSql = "UPDATE order_items SET order_id = :after WHERE order_id = :before";
        SqlParameterSource param = new MapSqlParameterSource().addValue("after", afterOrdersId).addValue("before",
                beforeOrdersId);
        template.update(updateSql, param);
    }
}
