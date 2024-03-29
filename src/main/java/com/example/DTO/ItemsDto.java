package com.example.DTO;

import org.springframework.stereotype.Component;

@Component
public class ItemsDto {

    private Integer id;
    private String name;
    private Integer conditionId;
    private Integer category;
    private String nameAll;
    private String brand;
    private Double price;
    private Integer shipping;
    private String description;

    public ItemsDto() {
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

    public Integer getConditionId() {
        return conditionId;
    }

    public void setConditionId(Integer conditionId) {
        this.conditionId = conditionId;
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

    public String getNameAll() {
        return nameAll;
    }

    public void setNameAll(String nameAll) {
        this.nameAll = nameAll;
    }

    @Override
    public String toString() {
        return "ItemsDto [id=" + id + ", name=" + name + ", conditionId=" + conditionId + ", category=" + category
                + ", nameAll=" + nameAll + ", brand=" + brand + ", price=" + price + ", shipping=" + shipping
                + ", description=" + description + "]";
    }
}
