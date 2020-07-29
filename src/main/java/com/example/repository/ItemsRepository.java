package com.example.repository;

import java.util.List;

import com.example.domain.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class ItemsRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	// メソッド内でJOINする前提なので、toppinListは加えてません。
	private static final RowMapper<Item> ITEM_ROW_MAPPER = (rs, i) -> {
		Item item = new Item();
		item.setId(rs.getInt("id"));
		item.setName(rs.getString("name"));
		item.setDescription(rs.getString("description"));
		item.setPriceM(rs.getInt("price_m"));
		item.setPriceL(rs.getInt("price_l"));
		item.setImagePass(rs.getString("image_path"));
		item.setCategoryId(rs.getInt("categoryid"));
		item.setArrivalDate(rs.getDate("arrival_date"));
		item.setSpec(rs.getString("spec"));
		item.setWarmId(rs.getInt("warmid"));
		item.setDeleted(rs.getBoolean("deleted"));
		return item;
	};

	/**
	 * 選択された並び順で全件検索を行う.
	 * 
	 * @param listType 並び順
	 * @return 全アイテム一覧
	 */
	public List<Item> findAll(String listType) {
		// 表示確認を優先する為、toppingsのJOINはまだしていません。
		// Mサイズの価格が安い順で表示されるようにしています。
		String sql = "SELECT * FROM items"
				+ " WHERE deleted != true ORDER BY " + listType;

		SqlParameterSource param = new MapSqlParameterSource().addValue("listType", listType);

		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);

		if (itemList.size() == 0) {
			return null;
		}

		return itemList;
	}

	/**
	 * @param id
	 * @return Item
	 */
	public Item load(Integer id) {
		try{
			String sql = "SELECT * FROM items WHERE id=:id";
			SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
			Item item = template.queryForObject(sql, param, ITEM_ROW_MAPPER);
			return item;
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}
	/**
	 * 選択された並び順で名前からアイテムを曖昧検索する.
	 * 
	 * @param name     名前
	 * @param listType 並び順
	 * @return 検索されたアイテム一覧
	 */
	public List<Item> findByLikeName(String name, String listType) {
		String sql = "SELECT * FROM items"
				+ " WHERE name LIKE :name AND deleted != true ORDER BY " + listType;

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");

		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);

		return itemList;
	}

	
	/** 
	 * itemsをcategoryidで抽出
	 * 
	 * @param categoryId
	 * @param listType
	 * @return List<Item>
	 */
	public List<Item> findByCategoryId(Integer categoryId,String listType){
		String sql="SELECT * FROM items WHERE categoryid=:categoryid AND deleted != true ORDER BY " + listType;

		SqlParameterSource param = new MapSqlParameterSource().addValue("categoryid",categoryId);
		List<Item> itemList =template.query(sql,param, ITEM_ROW_MAPPER);

		return itemList;
	}

	public List<Item> findByWarmId(Integer warmId,String listType){
		String sql="SELECT * FROM items WHERE warmid=:warmid AND deleted != true ORDER BY " + listType;

		SqlParameterSource param = new MapSqlParameterSource().addValue("warmid",warmId);
		List<Item> itemList =template.query(sql,param, ITEM_ROW_MAPPER);

		return itemList;
	}
}
