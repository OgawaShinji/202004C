package com.example.domain;

import java.util.List;

public class Item {

    private Integer id;
    private String name;
    private String description;
    private Integer priceM;
    private Integer priceL;
    private String imagePass;
    private Integer categoryId;
    private Boolean deleted;
    private List<Topping> toppingList;


    public Item() {
    }

    public Item(Integer id, String name, String description, Integer priceM, Integer priceL, String imagePass, Integer categoryId,Boolean deleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priceM = priceM;
        this.priceL = priceL;
        this.imagePass = imagePass;
        this.categoryId = categoryId;
        this.deleted = deleted;
    }
    public Item(Integer id, String name, String description, Integer priceM, Integer priceL, String imagePass, Integer categoryId,Boolean deleted, List<Topping> toppingList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priceM = priceM;
        this.priceL = priceL;
        this.imagePass = imagePass;
        this.categoryId = categoryId;
        this.deleted = deleted;
        this.toppingList = toppingList;
    }


    /**
     * @return Integer
     */
    public Integer getId() {
        return this.id;
    }


    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }


    /**
     * @return String
     */
    public String getName() {
        return this.name;
    }


    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return String
     */
    public String getDescription() {
        return this.description;
    }


    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * @return Integer
     */
    public Integer getPriceM() {
        return this.priceM;
    }


    /**
     * @param priceM
     */
    public void setPriceM(Integer priceM) {
        this.priceM = priceM;
    }


    /**
     * @return Integer
     */
    public Integer getPriceL() {
        return this.priceL;
    }


    /**
     * @param priceL
     */
    public void setPriceL(Integer priceL) {
        this.priceL = priceL;
    }


    /**
     * @return String
     */
    public String getImagePass() {
        return this.imagePass;
    }


    /**
     * @param imagePass
     */
    public void setImagePass(String imagePass) {
        this.imagePass = imagePass;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return Boolean
     */
    public Boolean isDeleted() {
        return this.deleted;
    }
    

    /**
     * @return Boolean
     */
    public Boolean getDeleted() {
        return this.deleted;
    }


    /**
     * @param deleted
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    /**
     * @return List<Topping>
     */
    public List<Topping> getToppingList() {
        return this.toppingList;
    }


    /**
     * @param toppingList
     */
    public void setToppingList(List<Topping> toppingList) {
        this.toppingList = toppingList;
    }


    /**
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", priceM='" + getPriceM() + "'" +
            ", priceL='" + getPriceL() + "'" +
            ", imagePass='" + getImagePass() + "'" +
            ", deleted='" + isDeleted() + "'" +
            ", toppingList='" + getToppingList() + "'" +
            "}";
    }

}

