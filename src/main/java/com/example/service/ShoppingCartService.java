package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.User;
import com.example.repository.OrderItemsRepository;
import com.example.repository.OrderToppingsRepository;
import com.example.repository.OrdersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderItemsRepository orderItemsRepository;
    @Autowired
    private OrderToppingsRepository orderToppingsRepository;

    /**
     * 初めてカートに商品を追加する際に呼ばれるメソッド ordersテーブルにインサートする
     * order_items,order_toppingsテーブルににインサートする
     * 
     * @param orderItem(userId, status,
     *                          totalPriceがセットされたOrders,orderIdがsetされていないOrderItemを引数に指定)
     */
    public void addShoppingCartItemForFirstOrder(Order order, OrderItem orderItem) {
        // ordersテーブルにinsertして自動採番されたidを取得
        Integer orderId = ordersRepository.insertOrdersForAddShoppingCart(order);
        // orderIdをsetしてorder_itemsテーブルにカートに追加された商品をinsertする
        orderItem.setOrderId(orderId);
        Integer orderItemId = orderItemsRepository.insertOrderItems(orderItem);
        // order_toppingsテーブルにOrderItemのList内のtoppingをinsertする
        for (OrderTopping orderTopping : orderItem.getOrderToppingList()) {
            orderTopping.setOrderItemId(orderItemId);
            orderToppingsRepository.insertOrderToppings(orderTopping);
        }

    }

    /**
     * 既にOrdersテーブルに買い物情報がインサートされている状態でカートに商品を追加するメソッド
     * order_items,order_toppingsにインサート時は1を返す order_itemsのquantityを増やすときは0を返す
     * 
     * @param orderItem(orderIdがsetされているOrderItemを引数に指定)
     */
    public Integer addShoppingCartItem(OrderItem orderItem) {
        List<Integer> orderItemsId = orderItemsRepository.findIdByItemIdAndOrderIdAndSize(orderItem);
        List<Integer> toppingIdsFromView = new ArrayList<Integer>();
        for (OrderTopping orderTopping : orderItem.getOrderToppingList()) {
            toppingIdsFromView.add(orderTopping.getToppingId());
        }
        if (Objects.nonNull(orderItemsId)) {
            for (Integer orderItemId : orderItemsId) {
                List<Integer> toppingIdsFromOrderToppingsTable = orderToppingsRepository
                        .findTopppingIdByOrderItemId(orderItemId);
                if (toppingIdsFromOrderToppingsTable.equals(toppingIdsFromView)) {
                    orderItem.setId(orderItemId);
                    orderItemsRepository.updateQuantityById(orderItem);
                    return 0;
                }
            }
            Integer orderItemIdForInsert = orderItemsRepository.insertOrderItems(orderItem);
            for (OrderTopping orderTopping : orderItem.getOrderToppingList()) {
                orderTopping.setOrderItemId(orderItemIdForInsert);
                orderToppingsRepository.insertOrderToppings(orderTopping);
            }
        } else {
            Integer orderItemIdForInsert = orderItemsRepository.insertOrderItems(orderItem);
            for (OrderTopping orderTopping : orderItem.getOrderToppingList()) {
                orderTopping.setOrderItemId(orderItemIdForInsert);
                orderToppingsRepository.insertOrderToppings(orderTopping);
            }
        }
        return 1;
    }

    /**
     * Ordersテーブルからuser_idを指定してstatus=0のデータを検索しidを返すメソッド
     * 
     * @param order
     * @return ordersの該当するid
     */
    public Integer findIdByUserIdAndStatus(Order order) {
        return ordersRepository.findIdByUserIdAndStatus(order);
    }

    /**
     * カートに追加された際にOrdersテーブルのtotal_priceに小計を足すメソッド
     * 
     * @param order
     */
    public void updateOrdersForPlusTotalPrice(Order order) {
        ordersRepository.updatePlusTotalPrice(order);
    }

    /**
     * カートから商品を削除するメソッド Ordersテーブルのtotal_priceから小計を引く
     * quantityが2以上なら一つ減らし、1ならorder_items,order_toppingsテーブルから該当するorder_itemのidのデータを削除する
     * 
     * @param orderItemId
     * @param order
     */
    public void deleteCartItem(Integer orderItemId, Order orderHasUserIdSAndTotalprice) {
        OrderItem orderItem = orderItemsRepository.findOrderItemById(orderItemId);
        if (orderItem.getQuantity() >= 2) {
            Integer singleTotalPrice = orderHasUserIdSAndTotalprice.getTotalPrice() / orderItem.getQuantity();
            orderHasUserIdSAndTotalprice.setTotalPrice(singleTotalPrice);
            orderItemsRepository.updateMinusQuantityById(orderItemId);
            ordersRepository.updateMinusTotalPrice(orderHasUserIdSAndTotalprice);
        } else {
            orderItemsRepository.deleteOrderItemsById(orderItemId);
            orderToppingsRepository.deleteOrderToppings(orderItemId);
            ordersRepository.updateMinusTotalPrice(orderHasUserIdSAndTotalprice);

        }
    }

    /**
     * ログイン時にカートに追加されたものがあり、そのユーザーが未ログインでカートに追加してログインしたときに呼ばれるメソッド
     * 
     * @param beforeOrder(未ログイン時のOreder情報)
     * @param afterOrder(ログイン時のOrder情報)
     */
    public void changeUserDuringShopping(Order orderForNotLoginHasTotalprice, Order orderForLogin) {
        Integer notLoginId = orderForNotLoginHasTotalprice.getId();
        Integer loginId = orderForLogin.getId();
        orderItemsRepository.updateOrderItemsOrderIdByOrderId(notLoginId, loginId);
        ordersRepository.deleteOrderById(orderForNotLoginHasTotalprice);
        orderForLogin.setTotalPrice(orderForNotLoginHasTotalprice.getTotalPrice());
        ordersRepository.updatePlusTotalPrice(orderForLogin);

    }

    /**
     * Orderテーブルのuser_idを変更するメソッド
     * 
     * @param beforeUserId (変更するOrderのid)
     * @param order        (変更したい新しいOrder情報)
     */
    public void updateOrdersUserId(Integer beforeUserId, Order order) {
        ordersRepository.updateUserId(order, beforeUserId);
    }

    public void updateStatus0To1(Order order, Integer userId) {

        ordersRepository.UpdateWhoPurchaseTheItemstoStatus1(order, userId);

    }

    public void updateStatus0To2(Order order, Integer userId) {

        ordersRepository.UpdateWhoPurchaseTheItemstoStatus2(order, userId);

    }

}
