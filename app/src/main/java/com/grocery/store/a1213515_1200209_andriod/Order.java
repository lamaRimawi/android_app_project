package com.grocery.store.a1213515_1200209_andriod;

import java.io.Serializable;

public class Order implements Serializable {
    private int orderId;
    private String userEmail;
    private int productId;
    private String productName;
    private int quantity;
    private String deliveryMethod;
    private long orderDate;
    private String status;
    private double totalPrice;
    private String productThumbnail;

    // Constructor
    public Order() {}

    public Order(int orderId, String userEmail, int productId, String productName,
                 int quantity, String deliveryMethod, long orderDate, String status,
                 double totalPrice, String productThumbnail) {
        this.orderId = orderId;
        this.userEmail = userEmail;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.deliveryMethod = deliveryMethod;
        this.orderDate = orderDate;
        this.status = status;
        this.totalPrice = totalPrice;
        this.productThumbnail = productThumbnail;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public long getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(long orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductThumbnail() {
        return productThumbnail;
    }

    public void setProductThumbnail(String productThumbnail) {
        this.productThumbnail = productThumbnail;
    }
}