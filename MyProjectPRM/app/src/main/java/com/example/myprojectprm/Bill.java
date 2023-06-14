package com.example.myprojectprm;

public class Bill {
    private int billId;
    private int productId;
    private int quantity;
    private int userId;
    private double totalPrice;

    public Bill(int billId, int productId, int quantity, int userId, double totalPrice) {
        this.billId = billId;
        this.productId = productId;
        this.quantity = quantity;
        this.userId = userId;
        this.totalPrice = totalPrice;
    }

    public int getBillId() {
        return billId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUserId() {
        return userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
