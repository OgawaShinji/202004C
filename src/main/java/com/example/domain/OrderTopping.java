package com.example.domain;

public class OrderTopping {
    private Integer id;
    private Integer toppingId;
    private Integer orderItemId;
    private Topping topping;

    
    /** 
     * @return Integer
     */
    public Integer getId() {
        return id;
    }

    
    /** 
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    
    /** 
     * @return Integer
     */
    public Integer getToppingId() {
        return toppingId;
    }

    
    /** 
     * @param toppingId
     */
    public void setToppingId(Integer toppingId) {
        this.toppingId = toppingId;
    }

    
    /** 
     * @return Integer
     */
    public Integer getOrderItemId() {
        return orderItemId;
    }

    
    /** 
     * @param orderItemId
     */
    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }
 
    
    /** 
     * @return Topping
     */
    public Topping getTopping() {
        return topping;
    }

    
    /** 
     * @param topping
     */
    public void setTopping(Topping topping) {
        this.topping = topping;
    }

    
}
