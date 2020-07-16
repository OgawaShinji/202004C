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
}
