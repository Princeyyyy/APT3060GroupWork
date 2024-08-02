package com.example.apt3060groupproject;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    private String id;
    private String userId;
    private String userName;
    private String userEmail;
    private String productId;
    private String productName;
    private double productPrice;
    private String productBranch;
    private boolean isConfirmed;
    private Date orderDate;

    // Empty constructor needed for Firestore
    public Order() {}

    public Order(String userId, String userName, String userEmail, String productId, String productName, double productPrice, String productBranch) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.isConfirmed = false;
        this.productBranch = productBranch;
        this.orderDate = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductBranch() {
        return productBranch;
    }

    public void setProductBranch(String productBranch) {
        this.productBranch = productBranch;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}
