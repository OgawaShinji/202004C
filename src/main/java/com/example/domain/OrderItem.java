package com.example.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderItem {

	/** ID */
	private Integer id;
	/** 商品ID */
	private Integer itemId;
	/** 注文ID */
	private Integer orderId;
	/** 数量 */
	private Integer quantity;
	/** サイズ */
	private Character size;
	/** 商品 */
	private Item item;
	/** 注文トッピングのリスト */
	private List<OrderTopping> orderToppingList;
	/** 小計を計算して返すメソッド */
	public int getSubTotal() {
		if (Objects.isNull(this.orderToppingList)) {
			setOrderToppingList(new ArrayList<OrderTopping>());
		}
		if (this.size == 'M') {
			int subTotal = (this.item.getPriceM() + this.orderToppingList.size() * 200) * quantity;
			return subTotal;
		}
		if (this.size == 'L') {
			int subTotal = (this.item.getPriceL() + this.orderToppingList.size() * 300) * quantity;
			return subTotal;
		}
		return 0;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Character getSize() {
		return size;
	}

	public void setSize(Character size) {
		this.size = size;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public List<OrderTopping> getOrderToppingList() {
		return orderToppingList;
	}

	public void setOrderToppingList(List<OrderTopping> orderToppingList) {
		this.orderToppingList = orderToppingList;
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", itemId=" + itemId + ", orderId=" + orderId + ", quantity=" + quantity
				+ ", size=" + size + ", item=" + item + ", orderToppingList=" + orderToppingList + "]";
	}

}
