package com.example.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.servlet.http.HttpSession;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.User;
import com.example.form.ItemDetailForm;
import com.example.form.PaymentForm;
import com.example.service.InsertUserService;
import com.example.service.ItemDetailService;
import com.example.service.SendMailService;
import com.example.service.ShoppingCartService;
import com.example.service.ShoppingHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;

@Controller
@Validated
@RequestMapping("/shoppingcart")
public class ShoppingCartController {

    @Autowired
    SendMailService sendMailService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private ItemDetailService itemDetailService;
    @Autowired
    private ShoppingHistoryService shoppingHistoryService;
    @Autowired
    private InsertUserService insertUserService;
    @Autowired
    private HttpSession session;

    @ModelAttribute
    private ItemDetailForm setUpItemdetailForm() {
        return new ItemDetailForm();
    }

    /**
     * カート内一覧画面を表示するメソッド
     *
     * @param model
     * @return cart_list.html
     */
    @ModelAttribute
    private PaymentForm setUpPaymentForm() {
        return new PaymentForm();
    }

    @RequestMapping("/toCartList")
    public String toCartList(Model model) {
        User userInSession = (User) session.getAttribute("user");
        Order order = new Order();

        try {
            order.setUserId(userInSession.getId());
            order.setStatus(0);
            List<Order> orderList = shoppingHistoryService.findCartHistory(order);
            if (orderList.size() == 0) {
                model.addAttribute("orderItemList", null);
                return "shoppingcart/cart_list";
            }
            model.addAttribute("order", orderList.get(0));
            return "shoppingcart/cart_list";
        } catch (NullPointerException e) {
            e.printStackTrace();
            model.addAttribute("orderItemList", null);
            return "shoppingcart/cart_list";
        }

    }

    /**
     * shoppingCartにItemを追加するメソッド
     * カートに初めて追加されたときのみOrdersテーブルにuser_id,status,total_priceをインサートする
     * order_itemsテーブルにOrderItemをインサートする order_toppingsテーブルにOrderToppingをインサートする
     *
     * @param form
     * @return
     */
    @RequestMapping("/addCartItem")
    public String addCartItem(@Validated ItemDetailForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "forward:/item-detail/showDetail";
        }
        // order_itemsにinsert用のデータ形成
        OrderItem orderItem = new OrderItem();
        orderItem.setItemId(Integer.parseInt(form.getId()));
        orderItem.setQuantity(Integer.parseInt(form.getQuantity()));
        char[] chars = form.getSize().toCharArray();
        orderItem.setSize(chars[0]);
        // oreder_toppingにinsertするorderItemのtoppingListのデータ形成
        List<OrderTopping> orderToppingList = new ArrayList<>();
        if (Objects.nonNull(form.getToppingList())) {
            for (String toppingId : form.getToppingList()) {
                OrderTopping orderTopping = new OrderTopping();
                orderTopping.setToppingId(Integer.parseInt(toppingId));
                orderToppingList.add(orderTopping);
            }
        }
        orderItem.setOrderToppingList(orderToppingList);
        // Ordersにinsert用データ形成
        Order orderForInsertOrders = new Order();
        if (form.getSize().equals("M")) {
            // TODO: quantityが2以上の際はトッピングはすべてにつける仕様になる
            if (Objects.nonNull(form.getToppingList())) {
                orderForInsertOrders.setTotalPrice(
                        (itemDetailService.load(form.getId()).getPriceM() + form.getToppingList().size() * 200)
                                * Integer.parseInt(form.getQuantity()));
            } else {
                orderForInsertOrders.setTotalPrice(
                        itemDetailService.load(form.getId()).getPriceM() * Integer.parseInt(form.getQuantity()));
            }

        }
        if (form.getSize().equals("L")) {
            if (Objects.nonNull(form.getToppingList())) {
                orderForInsertOrders.setTotalPrice(
                        (itemDetailService.load(form.getId()).getPriceL() + form.getToppingList().size() * 300)
                                * Integer.parseInt(form.getQuantity()));
            } else {
                orderForInsertOrders.setTotalPrice(
                        itemDetailService.load(form.getId()).getPriceL() * Integer.parseInt(form.getQuantity()));
            }
        }
        orderForInsertOrders.setStatus(0);

        if (Objects.nonNull(session.getAttribute("user"))) {
            User userInSession = (User) session.getAttribute("user");
            orderForInsertOrders.setUserId(userInSession.getId());
            Integer ordersId = shoppingCartService.findIdByUserIdAndStatus(orderForInsertOrders);
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
                if (Objects.isNull(shoppingCartService.findIdByUserIdAndStatus(orderForInsertOrders))) {
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

    /**
     * shoppingCartからItemを削除するメソッド Ordersテーブルのtotal_priceを減らす
     * order_itemsから該当するidのデータをデリートする order_toppingsから該当するorder_item_idのデータをデリートする
     *
     * @param orderItemId
     * @param subTotal
     * @return
     */
    @RequestMapping("/deleteCartItem")
    public String deleteCartItem(Integer orderItemId, Integer subTotal) {
        User user = (User) session.getAttribute("user");
        Order order = new Order();
        order.setTotalPrice(subTotal);
        order.setUserId(user.getId());
        shoppingCartService.deleteCartItem(orderItemId, order);
        return "redirect:/shoppingcart/toCartList";
    }

    @RequestMapping("/confirm")
    public String confirmToBuy(Model model) {

        User userInSession = (User) session.getAttribute("user");
        model.addAttribute("user", userInSession);
        Order order = new Order();

        try {
            order.setUserId(userInSession.getId());
            order.setStatus(0);
            List<Order> orderList = shoppingHistoryService.findCartHistory(order);

            model.addAttribute("order", orderList.get(0));
            return "shoppingcart/confirm_to_buy";
        } catch (NullPointerException e) {
            e.printStackTrace();
            model.addAttribute("orderItemList", null);
            return "shoppingcart/confirm_to_buy";
        }
    }

    @RequestMapping("/updateOrders")
    public String completeBuying(@Validated PaymentForm paymentForm, BindingResult result, Model model, Integer cashMethod) {

        // if (result.hasErrors()) {
        // return confirmToBuy(model);
        // }
        Order order = new Order();
        User afterGetUser = (User) session.getAttribute("user");
        Integer userId = afterGetUser.getId();
        String userName = afterGetUser.getName();

        order.setDestinationName(paymentForm.getName());
        order.setDestinationEmail(paymentForm.getEmail());
        StringBuilder br = new StringBuilder(paymentForm.getZipcode());
        br.deleteCharAt(3);
        order.setDestinationZipcode(br.toString());
        order.setDestinationAddress(paymentForm.getAddress());
        order.setDestinationTel(paymentForm.getTelephone());

        try{
        Date orderDate = new Date();
        SimpleDateFormat smpDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String afterFormatOrderDateStr = smpDateFormat.format(orderDate);
        Date afterFormatOrderDate = smpDateFormat.parse(afterFormatOrderDateStr);
        order.setOrderDate(afterFormatOrderDate);
        }catch(ParseException e){
            e.printStackTrace();
        }

        String deliveryWantStr = paymentForm.getYmd() + " " + paymentForm.getTime();
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // DateTimeFormatter dtFormatterForMail = DateTimeFormatter.ofPattern("EEEE MM/dd HH:mm");
        // LocalDateTime ldtForMail = LocalDateTime.parse(deliveryWantStr, dtFormatterForMail);
        LocalDateTime ldt = LocalDateTime.parse(deliveryWantStr, dtFormatter);
        Timestamp deliveryWantTS = Timestamp.valueOf(ldt);
        order.setDeliveryTime(deliveryWantTS);



        if (cashMethod == 1) {
            order.setPaymentMethod(1);
            shoppingCartService.updateStatus0To1(order, userId);
        } else if (cashMethod == 2) {
            order.setPaymentMethod(2);
            shoppingCartService.updateStatus0To2(order, userId);
        }

        // shoppingCartService.sendMail(order);

        List<Order> ordersList = shoppingHistoryService.findItemHistory(userId);
        Order orderWhatBoughtLatest = ordersList.get(0);
        Context context = new Context();

        context.setVariable("name", userName);
        // context.setVariable("deliveryTime", ldtForMail);
        context.setVariable("orderList", orderWhatBoughtLatest);

//        sendMailService.sendMail(context, order); // ページ遷移できなかったのでコメントアウトしました to笠脇氏
        
        // 注文をしたユーザーの保有ポイントをお会計金額の１％ぶん追加する
        Integer plusPoint = afterGetUser.getPoint() + (orderWhatBoughtLatest.getTotalPrice() / 100);
        afterGetUser.setPoint(plusPoint);
        shoppingCartService.updatePoint(afterGetUser);

        return "/shoppingcart/complete";

    }

    @RequestMapping("/backToTop")
    public String backToTop() {

        return "redirect:/item-list";

    }

}
