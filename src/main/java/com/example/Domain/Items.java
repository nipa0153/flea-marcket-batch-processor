package com.example.Domain;

public class Items {
    private Integer id;
    private String name;
    private Integer condition;
    private Integer category;
    private String brand;
    private Double price;
    private Integer shipping;
    private String description;

    @Override
    public String toString() {
        return "Items [id=" + id + ", name=" + name + ", condition=" + condition + ", category=" + category + ", brand="
                + brand + ", price=" + price + ", shipping=" + shipping + ", description=" + description + "]";
    }

    public Items() {
    }

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

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getShipping() {
        return shipping;
    }

    public void setShipping(Integer shipping) {
        this.shipping = shipping;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
