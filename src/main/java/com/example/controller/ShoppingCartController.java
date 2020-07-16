package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.User;
import com.example.form.ItemDetailForm;
import com.example.service.ItemDetailService;
import com.example.service.ShoppingCartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shoppingcart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private ItemDetailService itemDetailService;
    @Autowired
    private HttpSession session;

    @ModelAttribute
    private ItemDetailForm setUpItemdetailForm() {
        return new ItemDetailForm();
    }

    @RequestMapping("/toCartList")
    public String toCartList() {
        return "shoppingcart/cart_list";
    }

    // shoppingCartに追加する機能
    @RequestMapping("/addCartItem")
    public String addCartItem(ItemDetailForm form) {
        // order_itemsにinsert用のデータ形成
        OrderItem orderItem = new OrderItem();
        orderItem.setItemId(Integer.parseInt(form.getId()));
        orderItem.setQuantity(Integer.parseInt(form.getQuantity()));
        char[] chars = form.getSize().toCharArray();
        orderItem.setSize(chars[0]);
        // oreder_toppingにinsertするorderItemのtoppingListのデータ形成
        List<OrderTopping> orderToppingList = new ArrayList<>();
        for (String toppingId : form.getToppingList()) {
            OrderTopping orderTopping = new OrderTopping();
            orderTopping.setToppingId(Integer.parseInt(toppingId));
            orderToppingList.add(orderTopping);
        }
        orderItem.setOrderToppingList(orderToppingList);

        // Ordersにinsert用データ形成
        Order orderForInsertOrders = new Order();
        if (form.getSize().equals("M")) {
            // TODO: quantityが2以上の際はトッピングはすべてにつける仕様になる
            orderForInsertOrders.setTotalPrice(
                    (itemDetailService.load(form.getId()).getPriceM() + form.getToppingList().size() * 200)
                            * Integer.parseInt(form.getQuantity()));
        }
        if (form.getSize().equals("L")) {
            orderForInsertOrders.setTotalPrice(
                    (itemDetailService.load(form.getId()).getPriceL() + form.getToppingList().size() * 300)
                            * Integer.parseInt(form.getQuantity()));
        }
        orderForInsertOrders.setStatus(0);

        if (Objects.nonNull(session.getAttribute("user"))) {
            User userInSession = (User) session.getAttribute("user");
            orderForInsertOrders.setUserId(userInSession.getId());
            Integer ordersId = shoppingCartService.findByUserIdOnlyStatusIsZero(orderForInsertOrders);
            if (Objects.isNull(ordersId)) {// ログイン済みユーザーが初めてカートに追加したとき
                shoppingCartService.addShoppingCartItemForFirstOrder(orderForInsertOrders, orderItem);
            } else {// ログイン問わず未入金商品がカートにある状態でカートに追加したとき
                orderItem.setOrderId(ordersId);
                shoppingCartService.addShoppingCartItem(orderItem);
                shoppingCartService.updateOrdersForPlusTotalPrice(orderForInsertOrders);
            }
        } else {// 未ログインユーザーが初めてカートに追加したとき
            while (true) {
                // 仮userId発行のための乱数生成
                Random random = new Random();
                int tentativeUserId = random.nextInt(10000000);
                orderForInsertOrders.setUserId(tentativeUserId);
                if (Objects.isNull(shoppingCartService.findByUserIdOnlyStatusIsZero(orderForInsertOrders))) {
                    shoppingCartService.addShoppingCartItemForFirstOrder(orderForInsertOrders, orderItem);
                    User user = new User();
                    user.setId(tentativeUserId);
                    session.setAttribute("user", user);
                    break;
                }
            }
        }
        return "redirect:/shoppingcart/toCartList";
    }
}
