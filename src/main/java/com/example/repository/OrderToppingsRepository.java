package com.example.repository;

import com.example.domain.OrderTopping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class OrderToppingsRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    public void insertOrderToppings(OrderTopping orderTopping) {
        String insertSql = "INSERT INTO order_toppings(topping_id, order_item_id) VALUES(:toppingId, :orderItemId)";
        SqlParameterSource param = new MapSqlParameterSource().addValue("toppingId", orderTopping.getToppingId())
                .addValue("orderItemId", orderTopping.getOrderItemId());
        template.update(insertSql, param);
    }
}
