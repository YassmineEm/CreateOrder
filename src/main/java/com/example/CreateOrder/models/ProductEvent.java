package com.example.CreateOrder.models;

public class ProductEvent {
    private String productId;
    private String orderId;
    private ProductState state;

    
    public ProductEvent() {
    }

    
    public ProductEvent(String productId, String orderId, ProductState state) {
        this.productId = productId;
        this.orderId = orderId;
        this.state = state;
    }

    
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ProductState getState() {
        return state;
    }

    public void setState(ProductState state) {
        this.state = state;
    }

    
    public enum ProductState {
        AVAILABLE,
        OUT_OF_STOCK
    }
}
