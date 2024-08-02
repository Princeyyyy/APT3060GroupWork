package com.example.apt3060groupproject;

public class Product {
    private String id;
    private String name;
    private String description;
    private String branch;
    private double price;
    private int count;

    // Empty constructor needed for Firestore
    public Product() {}

    public Product(String id, String name, String description, String branch, double price, int count) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.branch = branch;
        this.price = price;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
