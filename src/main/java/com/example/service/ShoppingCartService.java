package com.example.service;

import java.util.Objects;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
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

    // order_idがsetされていないOrderItemを引数に指定
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

    // order_idがsetされているOrderItemを引数に指定
    public void addShoppingCartItem(OrderItem orderItem) {
        Integer orderItemId = orderItemsRepository.insertOrderItems(orderItem);
        for (OrderTopping orderTopping : orderItem.getOrderToppingList()) {
            orderTopping.setOrderItemId(orderItemId);
            orderToppingsRepository.insertOrderToppings(orderTopping);
        }
    }

    public Integer findByUserIdOnlyStatusIsZero(Order order) {
        return ordersRepository.findByUserIdOnlyStatusIsZero(order);
    }
    public void updateOrdersForPlusTotalPrice(Order order){
        ordersRepository.updatePlusTotalPrice(order);
    }
}
