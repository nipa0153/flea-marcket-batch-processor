package com.example.DTO;

public class CategoryDto {

    private String parent;
    private String child;
    private String grandChild;

    public CategoryDto() {
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getGrandChild() {
        return grandChild;
    }

    public void setGrandChild(String grandChild) {
        this.grandChild = grandChild;
    }

    @Override
    public String toString() {
        return "CategoryDto [parent=" + parent + ", child=" + child + ", grandChild=" + grandChild + "]";
    }

}
