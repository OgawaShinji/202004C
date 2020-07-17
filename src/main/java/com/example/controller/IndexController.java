package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.form.IndexForm;
import com.example.service.IndexService;

@Controller
@RequestMapping("/item-list")
public class IndexController {

	@Autowired
	private IndexService indexService;

	@ModelAttribute
	public IndexForm setItemForm() {
		return new IndexForm();
	}

	/**
	 * 初期の商品一覧画面を表示するリクエストが送られるメソッド
	 * 
	 * @param form
	 * @param model
	 * @return
	 */
	@RequestMapping("")
	public String index(Model model) {
		// 並び順を変更するセレクトボタンにthymeleafを適用するためのMapを作成
		Map<String, String> selectMap = new HashMap<>();
		selectMap.put("価格安い順（Mサイズ）", "price_m");
		selectMap.put("価格安い順（Lサイズ）", "price_l");
		selectMap.put("名前順", "name");
		model.addAttribute("selectMap", selectMap);
		// 初期画面ではMサイズの価格順に全件表示する処理
		if (!model.containsAttribute("itemList")) {
			String listType = "price_m";
			List<Item> itemList = indexService.findAll(listType);
			model.addAttribute("itemList", itemList);
			// オートコンプリート用にJavaScriptの配列の中身を文字列で作ってスコープへ格納
			StringBuilder itemListForAutocomplete = indexService.getItemListForAutocomplete(itemList);
			model.addAttribute("itemListForAutocomplete", itemListForAutocomplete);

			model.addAttribute("itemList", itemList);
		}

		return "item/item_list";
	}

	/**
	 * 商品検索ボタンが押されたらリクエストが送られるメソッド
	 * 
	 * @param name
	 * @param listType
	 * @param model
	 * @return
	 */
	@RequestMapping("/search")
	public String seach(IndexForm form, Model model) {
		List<Item> itemList = indexService.findByLikeName(form.getName(), form.getListType());

		if (itemList.size() == 0) {
			itemList = indexService.findAll(form.getListType());
			String nullMessage = "該当する商品がありません";
			model.addAttribute("nullMessage", nullMessage);
		}

		model.addAttribute("itemList", itemList);
		// オートコンプリート用にJavaScriptの配列の中身を文字列で作ってスコープへ格納
		List<Item> items=indexService.findAll(form.getListType());
		StringBuilder itemListForAutocomplete = indexService.getItemListForAutocomplete(items);
		model.addAttribute("itemListForAutocomplete", itemListForAutocomplete);

		return index(model);
	}
}
