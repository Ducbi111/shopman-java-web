package com.shopman.model;

import java.math.BigDecimal;

public class CartItem8 {
    private Long id;
    private Long cartId;
    private Product8 product;
    private Integer quantity;
    private BigDecimal unitPrice;

    public BigDecimal getAmount() {
        return unitPrice == null || quantity == null ? BigDecimal.ZERO : unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }
    public Product8 getProduct() { return product; }
    public void setProduct(Product8 product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}
