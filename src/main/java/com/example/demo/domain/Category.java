package com.example.demo.domain;

public class Category {
    private Integer id;
    private String name;
    private Integer parentId;
    private String nameAll;
    private String parent;
    private String child;
    private String grandChild;

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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getNameAll() {
        return nameAll;
    }

    public void setNameAll(String nameAll) {
        this.nameAll = nameAll;
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
        return "Category [id=" + id + ", name=" + name + ", parentId=" + parentId + ", nameAll=" + nameAll + ", parent="
                + parent + ", child=" + child + ", grandChild=" + grandChild + "]";
    }
}
