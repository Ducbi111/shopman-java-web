package com.shopman.model;

import java.math.BigDecimal;

public class InvoiceDetail8 {
    private Long id;
    private Long invoiceId;
    private Product8 product;
    private Integer quantity;
    private BigDecimal unitPrice;

    public BigDecimal getAmount() { return unitPrice.multiply(BigDecimal.valueOf(quantity)); }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getInvoiceId() { return invoiceId; }
    public void setInvoiceId(Long invoiceId) { this.invoiceId = invoiceId; }
    public Product8 getProduct() { return product; }
    public void setProduct(Product8 product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}
