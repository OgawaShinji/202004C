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
     * order_items,order_toppingsにインサート時は1を返す
     * order_itemsのquantityを増やすときは0を返す
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
     * order_items,order_toppingsテーブルから該当するorder_itemのidのデータを削除する
     * 
     * @param orderItemId
     * @param order
     */
    public void deleteCartItem(Integer orderItemId, Order order) {
        orderItemsRepository.deleteOrderItemsById(orderItemId);
        orderToppingsRepository.deleteOrderToppings(orderItemId);
        ordersRepository.updateMinusTotalPrice(order);
    }

    public void changeUserDuringShopping(Order beforeOrder, Order afterOrder) {
        Integer beforeOrdersId = beforeOrder.getId();
        Integer afterOrdersId = afterOrder.getId();
        orderItemsRepository.updateOrderItemsOrderIdByOrderId(beforeOrdersId, afterOrdersId);
        ordersRepository.updateUserId(afterOrder, beforeOrder.getUserId());
        ordersRepository.deleteOrderById(beforeOrder);
    }

    public void updateOrdersUserId(Integer beforeUserId, Order order) {
        ordersRepository.updateUserId(order, beforeUserId);
    }
}
