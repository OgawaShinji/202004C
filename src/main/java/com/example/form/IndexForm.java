package com.example.form;

public class IndexForm {

	/** ID */
	private Integer id;
	/** 商品名 */
	private String name;
	/** 並び順 */
	private String listType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	@Override
	public String toString() {
		return "IndexForm [id=" + id + ", name=" + name + ", listType=" + listType + "]";
	}

}
