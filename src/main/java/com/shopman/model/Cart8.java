package com.shopman.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart8 {
    private Long id;
    private Long customerId;
    private List<CartItem8> items = new ArrayList<>();

    public Integer getTotalItems() { return items.stream().mapToInt(CartItem8::getQuantity).sum(); }
    public BigDecimal getTotalAmount() { return items.stream().map(CartItem8::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add); }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public List<CartItem8> getItems() { return items; }
    public void setItems(List<CartItem8> items) { this.items = items == null ? new ArrayList<>() : items; }
}
