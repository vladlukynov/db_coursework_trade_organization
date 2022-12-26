package com.coursework.app.entity;

public class RequestProduct {
    private Request request;
    private final Product product;
    private int quantity;

    public RequestProduct(Product product, int quantity) {
        this.request = null;
        this.product = product;
        this.quantity = quantity;
    }

    public RequestProduct(Request request, Product product, int quantity) {
        this.request = request;
        this.product = product;
        this.quantity = quantity;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
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
}
