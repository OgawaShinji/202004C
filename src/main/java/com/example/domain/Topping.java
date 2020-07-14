package com.example.domain;

public class Topping {

    private Integer id;
    private String name;
    private Integer priceM;
    private Integer priceL;


    public Topping() {
    }

    public Topping(Integer id, String name, Integer priceM, Integer priceL) {
        this.id = id;
        this.name = name;
        this.priceM = priceM;
        this.priceL = priceL;
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
    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", priceM='" + getPriceM() + "'" +
            ", priceL='" + getPriceL() + "'" +
            "}";
    }


}
