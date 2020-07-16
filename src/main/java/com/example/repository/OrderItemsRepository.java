package com.example.repository;

import com.example.domain.OrderItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemsRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    public Integer insertOrderItems(OrderItem orderItems) {
        String insertSql = "INSERT INTO order_items(item_id, order_id, quantity, size) VALUES(:itemId, :orderId, :quantity, :size) RETURNING id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", orderItems.getItemId())
                .addValue("orderId", orderItems.getOrderId()).addValue("quantity", orderItems.getQuantity())
                .addValue("size", orderItems.getSize());
        Integer id = template.queryForObject(insertSql, param, Integer.class);
        return id;
    }
    
}
