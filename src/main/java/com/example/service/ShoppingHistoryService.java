package com.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.repository.OrdersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingHistoryService {

    @Autowired
    private OrdersRepository orderRepository;

    /**
     * statusでの場合分けがまだ未完成
     */

    /**
     * @param userId
     * @return List<OrderItem>
     */
    public List<Order> findItemHistory(Integer userId) {
        List<Order> orderList = orderRepository.findItemsByUserIdAndStatusOverThan2(userId);
        List<OrderTopping> ordToppingList = new ArrayList<>();
        Map<Integer, Order> orderMap = new HashMap<>();
        Map<Integer, OrderItem> orderItemMap = new HashMap<>();

        for (Order order : orderList) {

            List<OrderItem> ordItemList = order.getOrderItemList();

            for (OrderItem orderItem : ordItemList) {

                List<OrderTopping> orderToppingList = orderItem.getOrderToppingList();

                for (OrderTopping orderTopping : orderToppingList) {

                    ordToppingList.add(orderTopping);

                }

            }

        }

        List<OrderItem> orderItemListForSameOrderId = new ArrayList<OrderItem>();

        for (Order order : orderList) {

            List<OrderItem> ordItemList = order.getOrderItemList();

            for (OrderItem orderItem : ordItemList) {

                List<OrderTopping> orderToppingList = new ArrayList<>();

                for (OrderTopping orderTopping : ordToppingList) {

                    if (orderItem.getId() == orderTopping.getOrderItemId()) {

                        orderToppingList.add(orderTopping);

                    }

                }

                orderItem.setOrderToppingList(orderToppingList);

                orderItemMap.put(orderItem.getOrderId(), orderItem);

            }
        }
        List<OrderItem> orderItemList = new ArrayList<OrderItem>(orderItemMap.values());

        for (Order order : orderList) {
            for (OrderItem orderItem : orderItemList) {
                if (order.getId() == orderItem.getOrderId()) {
                    orderItemListForSameOrderId.add(orderItem);
                }
            }

        }

        Order order = new Order();

        order.setOrderItemList(orderItemList);

        orderMap.put(order.getId(), order);

        List<Order> orderListForReturn = new ArrayList<Order>(orderMap.values());

        return orderListForReturn;

    }

    /**
     * Repositoryからorders,order_items,order_toppings,items,toppingsをJoinして検索してきたデータを重複分を整理する
     * 
     * @param orderHasUserIdAndStatus(userId,statusをset)
     * @return joinしたデータが格納されたOrderのList
     */
    public List<Order> findCartHistory(Order orderHasUserIdAndStatus) {
        List<Order> orderList = orderRepository.findOrderItemsAndToppingsByUserId(orderHasUserIdAndStatus);
        List<OrderTopping> ordToppingList = new ArrayList<>();
        Map<Integer, Order> orderMap = new HashMap<>();
        Map<Integer, OrderItem> orderItemMap = new HashMap<>();
        // 検索したorderに該当するtoppingだけのListを作成
        for (Order order : orderList) {
            List<OrderItem> ordItemList = order.getOrderItemList();
            for (OrderItem orderItem : ordItemList) {
                List<OrderTopping> orderToppingList = orderItem.getOrderToppingList();
                for (OrderTopping orderTopping : orderToppingList) {
                    ordToppingList.add(orderTopping);
                }
            }
        }
        // いらないのでは？ List<OrderItem> orderItemListForSameOrderId = new
        // ArrayList<OrderItem>();

        // OrderItemに該当するOrderToppingListをそれぞれsetして重複分はmapにキー名のidがかぶるので削除される
        for (Order order : orderList) {
            List<OrderItem> ordItemList = order.getOrderItemList();
            for (OrderItem orderItem : ordItemList) {
                List<OrderTopping> orderToppingList = new ArrayList<>();
                for (OrderTopping orderTopping : ordToppingList) {
                    // Integerは==では値の比較ができないのでequalsを用いる
                    if (orderItem.getId().equals(orderTopping.getOrderItemId())) {
                        orderToppingList.add(orderTopping);
                    }
                }
                orderItem.setOrderToppingList(orderToppingList);
                // getOrderIdではなくgetIdをキーにした
                orderItemMap.put(orderItem.getId(), orderItem);
            }
        }
        // 作成したMapをListにする
        List<OrderItem> orderItemList = new ArrayList<OrderItem>(orderItemMap.values());
        for (Order order : orderList) {
            order.setOrderItemList(orderItemList);
            orderMap.put(order.getId(), order);
        }
        List<Order> orderListForReturn = new ArrayList<Order>(orderMap.values());
        return orderListForReturn;
    }
}