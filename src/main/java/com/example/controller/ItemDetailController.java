package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param model
     * @param itemId
     * @return String
     */
    @RequestMapping("/showDetail")
    public String index(Model model, String itemId) {
        Item item = itemDetailService.load(itemId);
        List<Topping> toppingList =itemDetailService.showAll();

        Map<Integer, Topping> toppingMap = new HashMap<Integer, Topping>();


        for(int i=0;i<toppingList.size();i++){
            Topping topping = toppingList.get(i);
            toppingMap.put(i,topping);
        }


        model.addAttribute("item", item);
        model.addAttribute("toppingMap", toppingMap);

        return "item/item_detail";
    }

  
}
