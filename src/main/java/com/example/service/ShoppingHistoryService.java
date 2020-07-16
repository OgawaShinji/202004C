package com.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;
import com.example.repository.OrderItemsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingHistoryService {

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    public List<OrderItem> findItemHistory(Integer userId){

    List<OrderItem> ordItemList = orderItemsRepository.findOrderItemsAndToppingsByUserId(userId);
    List<OrderTopping> ordToppingList = new ArrayList<>();
    Map<Integer,OrderItem> orderItemMap = new HashMap<>();

    for(OrderItem orderItem : ordItemList){

        List<OrderTopping> orderToppingList = orderItem.getOrderToppingList();

        for(OrderTopping orderTopping : orderToppingList){

            ordToppingList.add(orderTopping);

        }

    }

    for(OrderItem orderItem : ordItemList){

        List<OrderTopping> orderToppingList = new ArrayList<>();

        for(OrderTopping orderTopping : ordToppingList){

           if(orderItem.getId()==orderTopping.getOrderItemId()){

            orderToppingList.add(orderTopping);

           }

        }

        orderItem.setOrderToppingList(orderToppingList);

        orderItemMap.put(orderItem.getId(), orderItem);

    }

    List<OrderItem> orderItemList = new ArrayList<OrderItem>(orderItemMap.values());

    return orderItemList;

    }

}