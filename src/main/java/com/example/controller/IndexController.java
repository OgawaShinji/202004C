package com.example.controller;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

	/**
	 * 初期の商品一覧画面を表示するリクエストが送られるメソッド 商品検索ボタンが押されたらリクエストが送られるメソッド
	 * 
	 * @param form
	 * @param model
	 * @return
	 */
	@RequestMapping("")
	public String index(Model model, Integer page, String name, String listType) {
		// 並び順を変更するセレクトボタンにthymeleafを適用するためのMapを作成
		Map<String, String> selectMap = new HashMap<>();
		selectMap.put("----------", "name");
		selectMap.put("価格安い順（Mサイズ）", "price_m");
		selectMap.put("価格安い順（Lサイズ）", "price_l");
		selectMap.put("価格高い順（Mサイズ）", "price_m DESC");
		selectMap.put("価格高い順（Lサイズ）", "price_l DESC");
		model.addAttribute("selectMap", selectMap);
		// ページング機能追加
		if (page == null) {
			page = 1;
		}
		List<Item> itemList = null;
		// 初めてitem-listへ遷移してきた時
		if (Objects.isNull(name)) {
			listType = "name";
			itemList = indexService.findAll(listType);
			model.addAttribute("itemList", itemList);
			// 2回目以降にitem-listへ遷移してきた時
		} else {
			// 並び順を選択してないときは価格安い順（Mサイズ）で表示する
			if (listType.equals("")) {
				itemList = indexService.findByLikeName(name, "price_m");
			} else {
				itemList = indexService.findByLikeName(name, listType);
			}
			// 取得された商品が null の場合は全件取得してエラーメッセージ
			if (itemList.size() == 0) {
				itemList = indexService.findAll(listType);
				String nullMessage = "該当する商品がありません";
				model.addAttribute("nullMessage", nullMessage);
			}
		}

		// 取得したitemListを元にページング機能を導入
		Page<Item> itemPage = indexService.showListPaging(page, 6, itemList);
		model.addAttribute("itemPage", itemPage);
		List<Integer> pageNumbers = calcPageNumbers(model, itemPage);
		model.addAttribute("pageNumbers", pageNumbers);
		// ページングの数字からも検索できるように検索フォームをスコープに格納しておく
		model.addAttribute("name", name);
		model.addAttribute("listType", listType);
		// オートコンプリート用にJavaScriptの配列の中身を文字列で作ってスコープへ格納
		List<Item> autocompleteList = indexService.findAll("name");
		StringBuilder itemListForAutocomplete = indexService.getItemListForAutocomplete(autocompleteList);
		model.addAttribute("itemListForAutocomplete", itemListForAutocomplete);

		return "item/item_list";
	}

	/**
	 * ページングのリンクに使うページ数をスコープに格納 (例)28件あり1ページにつき10件表示させる場合→1,2,3がpageNumbersに入る
	 * 
	 * @param model        モデル
	 * @param employeePage ページング情報
	 */
	private List<Integer> calcPageNumbers(Model model, Page<Item> itemPage) {
		int totalPages = itemPage.getTotalPages();
		List<Integer> pageNumbers = null;
		if (totalPages > 0) {
			pageNumbers = new ArrayList<>();
			for (int i = 1; i <= totalPages; i++) {
				pageNumbers.add(i);
			}
		}
		return pageNumbers;
	}

}
