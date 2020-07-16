package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import com.example.domain.Item;
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
    public List<OrderItem> findOrderItemsAndToppingsByUserId(Integer userId){

        String sql = "SELECT ori.id AS oriId, ori.order_id AS oriOrderId, ori.item_id AS oriItemId, ori.quantity AS oriQuantity, ori.size AS oriSize, itm.id AS itmId, itm.name AS itmName, itm.image_path AS itmImagePath, itm.price_m AS itmPriceM, itm.price_l AS itmPriceL, top.name AS topName, top.price_m AS topPriceM, top.price_l AS topPriceL, otp.order_item_id AS otpOrdItmId FROM order_items AS ori JOIN orders AS ord ON ori.order_id = ord.id JOIN users AS use on ord.user_id = use.id JOIN items as itm ON ori.item_id = itm.id JOIN order_toppings AS otp ON ori.id = otp.order_item_id JOIN toppings AS top ON otp.topping_id = top.id WHERE use.id = :userId AND status = 2";

        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);

        List<OrderItem> orderItemsList = template.query(sql, param, ORDER_ITEM_ROW_MAPPER);

        return orderItemsList;

    }


    /**
     * @param orderItems
     * @return Integer
     */
    public Integer insertOrderItems(OrderItem orderItems) {
        String insertSql = "INSERT INTO order_items(item_id, order_id, quantity, size) VALUES(:itemId, :orderId, :quantity, :size) RETURNING id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", orderItems.getItemId())
                .addValue("orderId", orderItems.getOrderId()).addValue("quantity", orderItems.getQuantity())
                .addValue("size", orderItems.getSize());
        Integer id = template.queryForObject(insertSql, param, Integer.class);
        return id;
    }

    public void deleteOrderItems(Integer orderItemId) {
        String deleteSql = "DELETE FROM order_items WHERE id = :orderItemId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("orderItemId", orderItemId);
        template.update(deleteSql, param);
    }
}
