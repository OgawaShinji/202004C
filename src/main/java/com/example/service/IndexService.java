package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Item;
import com.example.repository.ItemsRepository;

@Service
@Transactional
public class IndexService {

	@Autowired
	private ItemsRepository itemRepository;

	/**
	 * 全件検索を行う.
	 * 
	 * @return 全アイテム一覧
	 */
	public List<Item> findAll() {
		List<Item> itemList = itemRepository.findAll();
		return itemList;
	}

	/**
	 * 名前からアイテムを曖昧検索する.
	 * 
	 * @param name 名前
	 * @return 検索されたアイテム一覧
	 */
	public List<Item> findByLikeName(String name) {
		List<Item> itemList = itemRepository.findByLikeName(name);
		return itemList;
	}

	/**
	 * オートコンプリート用にJavaScriptの配列の中身を文字列で作ります.
	 * 
	 * @param itemList 商品一覧
	 * @return オートコンプリート用JavaScriptの配列の文字列        
	 **/
	public StringBuilder getItemListForAutocomplete(List<Item> itemList) {
		StringBuilder itemListForAutocomplete = new StringBuilder();
		for (int i = 0; i < itemList.size(); i++) {
			if (i != 0) {
				itemListForAutocomplete.append(",");
			}
			Item item = itemList.get(i);
			itemListForAutocomplete.append("\"");
			itemListForAutocomplete.append(item.getName());
			itemListForAutocomplete.append("\"");
		}
		return itemListForAutocomplete;
	}
}
