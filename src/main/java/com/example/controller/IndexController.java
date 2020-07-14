package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.service.IndexService;

@Controller
@RequestMapping("/item-list")
public class IndexController {
	
	@Autowired
	private IndexService indexService;

	@RequestMapping("")
	public String index(Model model) {
		List<Item> itemList = indexService.findAll();
		model.addAttribute("itemList", itemList);
		return "item/item_list";
	}
}
