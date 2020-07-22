package com.example.form;

public class IndexForm {

	private String name;
	private String listType;

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
		return "IndexForm [name=" + name + ", listType=" + listType + "]";
	}

}
