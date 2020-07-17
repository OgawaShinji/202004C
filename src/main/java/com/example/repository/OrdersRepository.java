package com.example.repository;

import java.util.List;
import java.util.Objects;

import com.example.domain.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class OrdersRepository {

    @Autowired
    private NamedParameterJdbcTemplate template;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Order> ORDER_ROW_Mapper = (rs, i) -> {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setStatus(rs.getInt("status"));
        order.setTotalPrice(rs.getInt("total_price"));
        order.setOrderDate(rs.getDate("order_date"));
        order.setDestinationName(rs.getString("destination_name"));
        order.setDestinationEmail(rs.getString("destination_email"));
        order.setDestinationZipcode(rs.getString("destination_zipcode"));
        order.setDestinationAddress(rs.getString("destination_address"));
        order.setDestinationTel(rs.getString("destination_tel"));
        order.setDeliveryTime(rs.getTimestamp("delivery_time"));
        order.setPaymentMethod(rs.getInt("payment_method"));
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
    public Integer findIdByUserId(Order order) {
        String sql = "SELECT id FROM orders WHERE user_id=:userId AND status=:status";
        SqlParameterSource param = new MapSqlParameterSource().addValue("userId", order.getUserId()).addValue("status", order.getStatus());
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
        String sql = "UPDATE orders SET total_price = total_price+:totalPrice WHERE user_id=:userId AND status=0";
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
}
