package com.example.service;

import java.util.Collections;
import java.util.List;

import com.example.domain.Item;
import com.example.repository.ItemsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IndexService {

	@Autowired
	private ItemsRepository itemRepository;

	/**
	 * 選択された並び順で全件検索を行う.
	 * 
	 * @param listType 並び順
	 * @return 全アイテム一覧
	 */
	public List<Item> findAll(String listType) {
		List<Item> itemList = itemRepository.findAll(listType);
		return itemList;
	}

	/**
	 * 選択された並び順で名前からアイテムを曖昧検索する.
	 * 
	 * @param name     名前
	 * @param listType 並び順
	 * @return 検索されたアイテム一覧
	 */
	public List<Item> findByLikeName(String name, String listType) {
		List<Item> itemList = itemRepository.findByLikeName(name, listType);
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
			itemListForAutocomplete.append("'");
			itemListForAutocomplete.append(item.getName());
			itemListForAutocomplete.append("'");
		}
		return itemListForAutocomplete;
	}

	/**
	 * 商品リストをページオブジェクトに関連付けるためのリスト
	 * 
	 * @param page     ページ数
	 * @param size     1ページのサイズ数
	 * @param itemList 商品リスト
	 * @return Page<Item>オブジェクトリスト
	 */
	public Page<Item> showListPaging(int page, int size, List<Item> itemList) {
		page--;
		int startItemCount = page * size;
		List<Item> list;

		if (itemList.size() < startItemCount) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItemCount + size, itemList.size());
			list = itemList.subList(startItemCount, toIndex);
		}

		Page<Item> itemPage = new PageImpl<Item>(list, PageRequest.of(page, size), itemList.size());
		return itemPage;
	}


	
	/** 
	 * itemsをcategoryidで抽出
	 * 
	 * @param categoryId
	 * @param listType
	 * @return List<Item>
	 */
	public List<Item> findByCategoryId(Integer categoryId,String listType) {
		List<Item> itemList = itemRepository.findByCategoryId(categoryId, listType);

		return itemList;
	}
}
