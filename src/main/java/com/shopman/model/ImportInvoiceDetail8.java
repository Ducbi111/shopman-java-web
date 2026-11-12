package com.shopman.model;

import java.math.BigDecimal;

public class ImportInvoiceDetail8 {
    private Long id;
    private Long importInvoiceId;
    private Product8 product;
    private Integer quantity;
    private BigDecimal importPrice;

    public BigDecimal getAmount() { return importPrice.multiply(BigDecimal.valueOf(quantity)); }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getImportInvoiceId() { return importInvoiceId; }
    public void setImportInvoiceId(Long importInvoiceId) { this.importInvoiceId = importInvoiceId; }
    public Product8 getProduct() { return product; }
    public void setProduct(Product8 product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getImportPrice() { return importPrice; }
    public void setImportPrice(BigDecimal importPrice) { this.importPrice = importPrice; }
}
