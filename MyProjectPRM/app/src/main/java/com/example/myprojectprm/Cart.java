package com.example.myprojectprm;

import java.io.Serializable;
import java.util.Date;

public class Cart implements Serializable {
    private Product product;
    private int quantity;

    private Date orderDate;


    public Cart(Product product, int quantity, Date orderDate) {
        this.product = product;
        this.quantity = quantity;
        this.orderDate = orderDate;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}
