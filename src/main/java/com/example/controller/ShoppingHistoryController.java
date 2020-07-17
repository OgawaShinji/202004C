package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.service.ShoppingHistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class ShoppingHistoryController {

    @Autowired
    private ShoppingHistoryService shoppingHistoryService;


    /**
     * @return String
     */
    @RequestMapping("/history")
    public String showHistory(Model model){

        // User user =(User) session.getAttribute("user");

        // Integer userId = user.getId();

        Integer userId = 1;

       List<Order> orderList = shoppingHistoryService.findItemHistory(userId);

       model.addAttribute("orderList", orderList);

        return "/shoppingHistory/shoppingHistory";
    }

}