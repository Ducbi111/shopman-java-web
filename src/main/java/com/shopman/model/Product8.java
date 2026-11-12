package com.shopman.model;

import java.math.BigDecimal;

public class Product8 {
    private Long id;
    private String productCode;
    private String name;
    private String productType;
    private String unit;
    private BigDecimal price;
    private Integer stockQuantity;
    private String description;
    private String image;
    private Long createdByManagerId;

    public Product8() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Long getCreatedByManagerId() { return createdByManagerId; }
    public void setCreatedByManagerId(Long createdByManagerId) { this.createdByManagerId = createdByManagerId; }
}
