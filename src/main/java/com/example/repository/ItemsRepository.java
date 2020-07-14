package com.example.repository;

import java.util.List;
import java.util.Objects;

import com.example.domain.Item;

import org.springframework.beans.factory.annotation.Autowired;
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
		item.setDeleted(rs.getBoolean("deleted"));
		return item;
	};

	/**
	 * 全件検索を行う。
	 * 
	 * @return 全アイテム一覧
	 */
	public List<Item> findAll() {
		// 表示確認を優先する為、toppingsのJOINはまだしていません。
		// Mサイズの価格が安い順で表示されるようにしています。
		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted FROM items ORDER BY price_m";

		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);

		return itemList;
	}

	public  Item findById(String id) {
		String sql="SELECT * FROM items WHERE id=:id";
		SqlParameterSource param=new MapSqlParameterSource().addValue("id", id);
		Item item=template.queryForObject(sql, param,ITEM_ROW_MAPPER);
		if(Objects.isNull(item)){
			return null;
		}
		return item;
	}
}
