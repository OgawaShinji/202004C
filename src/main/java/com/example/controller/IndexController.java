package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.service.IndexService;

@Controller
@RequestMapping("/item-list")
public class IndexController {
	
	@Autowired
	private IndexService indexService;
	
	@ModelAttribute
	public Item setItem() {
		return new Item();
	}

	@RequestMapping("")
	public String index(Model model) {
		List<Item> itemList = indexService.findAll();
		model.addAttribute("itemList", itemList);
		return "item/item_list";
	}
	
	@RequestMapping("/search")
	public String seach(String name, Model model) {
		List<Item> itemList = indexService.findByLikeName(name);
		
		if(itemList.size()==0) {
			itemList = indexService.findAll();
			String nullMessage = "該当する商品がありません";
			model.addAttribute("nullMessage", nullMessage);
		}
		model.addAttribute("itemList", itemList);
		return "item/item_list";
	}
}
