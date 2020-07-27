package com.example.repository;

import java.util.List;

import com.example.domain.OrderItem;

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

        orderItem.setId(rs.getInt("id"));
        orderItem.setItemId(rs.getInt("item_id"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        char[] chars = rs.getString("size").toCharArray();
        orderItem.setSize(chars[0]);
        return orderItem;

    };
    private static final RowMapper<Integer> ORDER_ITEM_ID_ROW_Mapper = (rs, i) -> {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getInt("id"));
        return orderItem.getId();
    };

    /**
     * order_itemsテーブルからidが一致するデータを検索しorderItem型で返すメソッド
     * 
     * @param id
     * @return 検索したorderitem
     */
    public OrderItem findOrderItemById(Integer id) {
        String sql = "SELECT id, item_id, order_id, quantity, size FROM order_items WHERE id = :id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        List<OrderItem> orderItems = template.query(sql, param, ORDER_ITEM_ROW_MAPPER);
        if (orderItems.size() == 0) {
            return null;
        }
        return orderItems.get(0);
    }

    /**
     * 指定したidのquantityを1減らすメソッド
     * 
     * @param id
     */
    public void updateMinusQuantityById(Integer id) {
        String sql = "UPDATE order_items SET quantity = quantity - 1 WHERE id = :id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        template.update(sql, param);
    }

    /**
     * item_id,order_id,sizeを指定してidを検索する
     * 
     * @param orderItem
     * @return
     */
    public List<Integer> findIdByItemIdAndOrderIdAndSize(OrderItem orderItem) {
        String sql = "SELECT id FROM order_items WHERE item_id=:itemId AND order_id=:orderId AND size=:size ORDER BY id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", orderItem.getItemId())
                .addValue("orderId", orderItem.getOrderId()).addValue("size", orderItem.getSize());
        List<Integer> orderItemIdList = template.query(sql, param, ORDER_ITEM_ID_ROW_Mapper);
        return orderItemIdList;
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
    public void updateOrderItemsOrderIdByOrderId(Integer oldOrdersId, Integer newOrdersId) {
        String updateSql = "UPDATE order_items SET order_id = :new WHERE order_id = :old";
        SqlParameterSource param = new MapSqlParameterSource().addValue("new", newOrdersId).addValue("old",
                oldOrdersId);
        template.update(updateSql, param);
    }

    /**
     * order_itemsのidを指定して同じ商品がカートに追加されたときquantityを増やす
     * 
     * @param orderItemHasIdAndQuantity(idとquantityがsetされているOrderItem)
     */
    public void updateQuantityById(OrderItem orderItemHasIdAndQuantity) {
        String sql = "UPDATE order_items SET quantity = quantity + :orderQuantity WHERE id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("orderQuantity", orderItemHasIdAndQuantity.getQuantity())
                .addValue("id", orderItemHasIdAndQuantity.getId());
        template.update(sql, param);
    }
}
