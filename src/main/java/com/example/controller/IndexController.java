package com.example.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.example.domain.Item;
import com.example.form.IndexForm;
import com.example.service.IndexService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/item-list")
public class IndexController {

	@Autowired
	private IndexService indexService;
	
	// 検索された内容をフォームに表示されたままにするため追加
	@ModelAttribute
	public IndexForm setIndexForm() {
		return new IndexForm();
	}

	/**
	 * 初期の商品一覧画面を表示するリクエストが送られるメソッド 商品検索ボタンが押されたらリクエストが送られるメソッド
	 * 
	 * @param form
	 * @param model
	 * @return
	 */
	@RequestMapping("")
	public String index(Model model, Integer page, String searchName, String listType, IndexForm indexForm,String categoryid) {
		
		// 並び順を変更するセレクトボタンにthymeleafを適用するためのMapを作成
		Map<String, String> selectMap = new LinkedHashMap<>();
		selectMap.put("新着順", "arrival_date desc,categoryid");
		selectMap.put("価格安い順", "price_m");
		selectMap.put("価格高い順", "price_m DESC");
		model.addAttribute("selectMap", selectMap);
		// ページング機能追加
		if (page == null) {
			page = 1;
		}
		List<Item> itemList =new ArrayList<>();
		// 初めてitem-listへ遷移してきた時
		if (Objects.isNull(categoryid)) {
			searchName="";
			categoryid ="0";
			listType = "arrival_date desc,categoryid";
			itemList = indexService.findAll(listType);
			model.addAttribute("itemList", itemList);
			// 2回目以降にitem-listへ遷移してきた時
		} else {

			// categoryidで取得したitemsを格納するlist
			List<Item> itemListByCategoryId=null;

			// 取得してきたcategoryidがnullの時
			if(categoryid.equals("")){
				categoryid="0";
			}
			Integer categoryId=Integer.parseInt(categoryid);

			// 取得してきたcategoryidが0の時
			if(categoryId == 0){
				itemListByCategoryId=indexService.findAll(listType);
			}else if(categoryId>0){
				itemListByCategoryId=indexService.findByCategoryId(categoryId, listType);
			}

			// searchNameで取得したitemsを格納するlist
			List<Item> itemListBysearchName=null;
			// 並び順を選択してないときは価格安い順（Mサイズ）で表示する
			
			itemListBysearchName = indexService.findByLikeName(searchName, listType);
			

			// itemListBycategoryIdとitemListBysearchNameの重複している部分をlistとして取得
			for(Item itema:itemListByCategoryId){
				for(Item itemb:itemListBysearchName){
					if(Objects.equals(itema.getId(), itemb.getId())){
						itemList.add(itema);
						break;
					}
				}
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
		for(Item item:itemPage.getContent()){
			LocalDate arrivalDate=item.getArrivalDate().toLocalDate();
			if(arrivalDate.isAfter(LocalDate.of(2020, 6, 1))){
				item.setIsNewItem(true);
			}else{
				item.setIsNewItem(false);
			}
		}
		model.addAttribute("itemPage", itemPage);
		List<Integer> pageNumbers = calcPageNumbers(itemPage);
		model.addAttribute("pageNumbers", pageNumbers);
		// ページングの数字からも検索できるように検索フォームをスコープに格納しておく
		model.addAttribute("searchName", searchName);
		model.addAttribute("listType", listType);
		model.addAttribute("categoryid",categoryid);
		// 検索結果を分かりやすくするために取得してきたcategoryidからcategorynameを割り出しmodelに格納する
		String categoryName=null;
		if(Objects.equals(categoryid, "0")){
			categoryName="全て";
		}else if(Objects.equals(categoryid, "1")){
			categoryName="飲み物";
		}else if(Objects.equals(categoryid, "2")){
			categoryName="食べ物";
		}else if(Objects.equals(categoryid, "3")){
			categoryName="グッズ";
		}else if(Objects.equals(categoryid, "4")){
			categoryName="ギフトアイテム";
		}
		model.addAttribute("categoryName", categoryName);
		// 取得件数を分かりやすくするために取得したitemPageのsizeをmodelに格納
		model.addAttribute("itemPageTotalElements", itemPage.getTotalElements());
		// 検索ワードを分かりやすくするために、取得したsearchNameをmodelに格納
		String search=searchName;
		if(Objects.equals(search,"")){
			search ="指定なし";
		}
		model.addAttribute("search",search);
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
	private List<Integer> calcPageNumbers(Page<Item> itemPage) {
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
