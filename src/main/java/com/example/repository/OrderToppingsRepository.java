package com.example.repository;

import java.util.List;

import com.example.domain.OrderTopping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class OrderToppingsRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;
    private static final RowMapper<Integer> ORDER_TOPPING_ID_ROWMAPPER = (rs, i) -> {
        OrderTopping orderTopping = new OrderTopping();
        orderTopping.setToppingId(rs.getInt("topping_id"));
        return orderTopping.getToppingId();
    };

    /**
     * order_item_idを指定してtopping_idを検索する
     * 
     * @param orderItemId
     * @return 検索したtopping_id
     */
    public List<Integer> findTopppingIdByOrderItemId(Integer orderItemId) {
        String sql = "SELECT topping_id FROM order_toppings WHERE order_item_id = :orderItemId ORDER BY topping_id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("orderItemId", orderItemId);
        List<Integer> result = template.query(sql, param, ORDER_TOPPING_ID_ROWMAPPER);
        return result;
    }

    /**
     * topping_idとorder_item_idをorder_toppingsテーブルにインサートする
     * 
     * @param orderTopping(topping_id, order_item_idがsetされている)
     */
    public void insertOrderToppings(OrderTopping orderTopping) {
        String insertSql = "INSERT INTO order_toppings(topping_id, order_item_id) VALUES(:toppingId, :orderItemId)";
        SqlParameterSource param = new MapSqlParameterSource().addValue("toppingId", orderTopping.getToppingId())
                .addValue("orderItemId", orderTopping.getOrderItemId());
        template.update(insertSql, param);
    }

    /**
     * order_item_idを指定してorder_toppingsテーブルから削除する
     * 
     * @param orderItemId
     */
    public void deleteOrderToppings(Integer orderItemId) {
        String deleteSql = "DELETE FROM order_toppings WHERE order_item_id = :orderItemId";
        SqlParameterSource param = new MapSqlParameterSource().addValue("orderItemId", orderItemId);
        template.update(deleteSql, param);
    }
}
