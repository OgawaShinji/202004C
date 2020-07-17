package com.example.form;

import java.util.List;

import javax.validation.constraints.NotBlank;

public class ItemDetailForm {

    // itemのid
    private String id;
    // itemのsize,controllerで価格に変更してください
    private String size;
    private String price;
    private List<String> toppingList;
    // itemの数量
    @NotBlank(message = "数量を選択してください")
    private String quantity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<String> getToppingList() {
        return toppingList;
    }

    public void setToppingList(List<String> toppingList) {
        this.toppingList = toppingList;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
