package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.example.domain.Item;
import com.example.domain.Topping;
import com.example.form.ItemDetailForm;
import com.example.service.ItemDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/item-detail")
public class ItemDetailController {

    /**
     * @return ItemDetailForm
     */
    @ModelAttribute
    public ItemDetailForm setUpForm() {
        return new ItemDetailForm();
    }

    @Autowired
    private ItemDetailService itemDetailService;

    /**
     * アイテムの詳細画面を表示する処理
     * 
     * @param model
     * @param itemId
     * @return String
     */
    @RequestMapping("/showDetail")
    public String index(Model model, String itemId) {
        Item item = itemDetailService.load(itemId);
        //itemがnullの場合item-listにフォワードする処理
        if (Objects.isNull(item)) {
			return "forward:/item-list";
		}
        List<Topping> toppingList = itemDetailService.showAll();

        Map<Integer, Topping> toppingMap = new HashMap<Integer, Topping>();
        Map<Integer, String> sizeMap = new HashMap<Integer, String>();
        sizeMap.put(0, "M");
        sizeMap.put(1, "L");

        // toppingListには(toppingTableのid，toppingオブジェクト)を詰める
        for (int i = 0; i < toppingList.size(); i++) {
            Topping topping = toppingList.get(i);
            toppingMap.put(topping.getId(), topping);
        }

        model.addAttribute("item", item);
        model.addAttribute("toppingMap", toppingMap);
        return "item/item_detail";
    }

}
